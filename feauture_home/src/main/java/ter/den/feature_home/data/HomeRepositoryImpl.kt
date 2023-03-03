package ter.den.feature_home.data

import android.util.Log
import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import ter.den.config.AppConfig
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.feature_home.data.db.toPopular
import ter.den.feature_home.data.network.PopularPageSource
import ter.den.feature_home.domain.HomeRepository
import ter.den.feature_home.domain.model.Popular
import ter.den.feature_movie_details.data.network.DetailsService
import ter.den.feature_movie_details.data.network.model.FavoriteMarkDto
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    @IoDispatcherQualifier private val ioDispatcher: CoroutineDispatcher,
    private val popularPageSource: PopularPageSource,
    private val dataSource: HomeDataSource,
    private val detailsService: DetailsService,
    private val appConfig: AppConfig,
) : HomeRepository {

    override fun getPopularContent(): PagingSource<Int, Popular> = popularPageSource

    override suspend fun getPopularFromDatabase(): List<Popular> =
        dataSource.getPopular().map { it.toPopular() }

    override suspend fun onFavoriteStatusChange(id: String) = withContext(ioDispatcher) {
        val sessionId = appConfig.sessionId.stateIn(this).value ?: ""
        val favorites = appConfig.favorites.stateIn(this).value
        if (favorites?.contains(id) == true) {
            val response = detailsService.markFavorite(sessionId, FavoriteMarkDto(false, id.toInt()))
            if(response.isSuccessful) appConfig.removeFromFavorites(id)
        } else {
            val response = detailsService.markFavorite(sessionId, FavoriteMarkDto(true, id.toInt()))
            if(response.isSuccessful) appConfig.addToFavorites(id)
        }
    }
}