package ter.den.feature_profile.data

import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import ter.den.config.AppConfig
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.core.domain.model.CustomThrowable
import ter.den.feature_auth.data.AuthDataSource
import ter.den.feature_movie_details.data.network.DetailsService
import ter.den.feature_movie_details.data.network.model.FavoriteMarkDto
import ter.den.feature_profile.data.network.FavoritePageSource
import ter.den.feature_profile.data.network.ProfileService
import ter.den.feature_profile.data.network.WatchListPageSource
import ter.den.feature_profile.data.network.model.account.toAccountDetails
import ter.den.feature_profile.domain.ProfileRepository
import ter.den.feature_profile.domain.model.AccountDetails
import ter.den.feature_profile.domain.model.Movie
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    @IoDispatcherQualifier private val ioDispatcher: CoroutineDispatcher,
    private val appConfig: AppConfig,
    private val profileService: ProfileService,
    private val favoritePageSource: FavoritePageSource.Factory,
    private val watchListPageSource: WatchListPageSource.Factory,
    private val detailsService: DetailsService,
    private val authDataSource: AuthDataSource,
) : ProfileRepository {

    override suspend fun getAccountDetails(sessionId: String): AccountDetails =
        withContext(ioDispatcher) {
            val response = profileService.getAccountDetails(sessionId)
            if (response.isSuccessful) {
                response.body()?.toAccountDetails() ?: throw CustomThrowable.AccountDetailsNull
            } else throw CustomThrowable.Error(response.message())
        }

    override fun getFavorites(sessionId: String): PagingSource<Int, Movie> =
        favoritePageSource.create(sessionId)

    override fun getWatchList(sessionId: String): PagingSource<Int, Movie> =
        watchListPageSource.create(sessionId)

    override fun getSessionIdFlow() = appConfig.sessionId

    override suspend fun createSessionId(login: String, password: String): String =
        withContext(ioDispatcher) {
            authDataSource.createSessionId(login, password)
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