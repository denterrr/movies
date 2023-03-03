package ter.den.feature_movie_details.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import ter.den.config.AppConfig
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.core.domain.model.CustomThrowable
import ter.den.feature_movie_details.data.network.DetailsService
import ter.den.feature_movie_details.data.network.model.FavoriteMarkDto
import ter.den.feature_movie_details.domain.DetailsRepository
import ter.den.feature_movie_details.domain.model.Movie
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    @IoDispatcherQualifier private val ioDispatcher: CoroutineDispatcher,
    private val detailsService: DetailsService,
    val appConfig: AppConfig,
) : DetailsRepository {

    override suspend fun getMovieDetails(id: Int): Movie = withContext(ioDispatcher) {
        val response = detailsService.getMovieDetails(id)
        if (response.isSuccessful) {
            val movie = response.body()?.toMovie() ?: throw CustomThrowable.MovieNull
            val favorites = appConfig.favorites.stateIn(this).value
            movie.isFavorite = favorites?.contains(movie.id.toString()) == true
            movie
        } else throw CustomThrowable.Error(response.errorBody().toString())
    }

    override suspend fun onFavoriteStatusChange(id: String) = withContext(ioDispatcher) {
        val sessionId = appConfig.sessionId.stateIn(this).value ?: ""
        val favorites = appConfig.favorites.stateIn(this).value
        if (favorites?.contains(id) == true) {
            val response =
                detailsService.markFavorite(sessionId, FavoriteMarkDto(false, id.toInt()))
            if (response.isSuccessful) appConfig.removeFromFavorites(id)
        } else {
            val response = detailsService.markFavorite(sessionId, FavoriteMarkDto(true, id.toInt()))
            if (response.isSuccessful) appConfig.addToFavorites(id)
        }
    }

}