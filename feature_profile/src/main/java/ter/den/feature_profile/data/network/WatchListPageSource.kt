package ter.den.feature_profile.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException
import ter.den.config.AppConfig
import ter.den.feature_profile.domain.model.Movie

class WatchListPageSource @AssistedInject constructor(
    private val service: ProfileService,
    private val appConfig: AppConfig,
    @Assisted("sessionId") private val sessionId: String,
) : PagingSource<Int, Movie>() {


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
            val response = service.getMoviesWatchList(sessionId, pageNumber)
            return if (response.isSuccessful) {
                val populars = response.body()?.results ?: emptyList()
                val favorites = appConfig.favorites.stateIn(CoroutineScope(Dispatchers.IO)).value
                populars.map { it.isFavorite = favorites?.contains(it.id.toString()) == true }
                val nextPageNumber =
                    if (pageNumber < (response.body()?.total_pages ?: 0)) pageNumber + 1 else null
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                LoadResult.Page(populars, prevPageNumber, nextPageNumber)
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(@Assisted("sessionId") sessionId: String): WatchListPageSource
    }


    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }
}