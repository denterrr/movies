package ter.den.feature_search.data

import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import ter.den.config.AppConfig
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.feature_movie_details.data.network.DetailsService
import ter.den.feature_movie_details.data.network.model.FavoriteMarkDto
import ter.den.feature_search.data.network.MoviePageSource
import ter.den.feature_search.domain.SearchRepository
import ter.den.feature_search.domain.model.Movie
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    @IoDispatcherQualifier private val ioDispatcher: CoroutineDispatcher,
    private val moviePageSource: MoviePageSource.Factory,
    private val appConfig: AppConfig,
    private val detailsService: DetailsService,
) : SearchRepository {
    override fun search(query: String): PagingSource<Int, Movie> =
        moviePageSource.create(query)

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