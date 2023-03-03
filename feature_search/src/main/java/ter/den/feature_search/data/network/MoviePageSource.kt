package ter.den.feature_search.data.network

import android.util.Log
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
import ter.den.feature_search.domain.model.Movie

class MoviePageSource @AssistedInject constructor(
    private val searchService: SearchService,
    private val appConfig: AppConfig,
    @Assisted("query") private val query: String
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        if (query.isBlank()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }
        try {
            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
            val response = searchService.search(query, pageNumber)

            return if (response.isSuccessful) {
                val populars = response.body()?.results ?: emptyList()
                val favorites = appConfig.favorites.stateIn(CoroutineScope(Dispatchers.IO)).value
                populars.map { it.isFavorite = favorites?.contains(it.id.toString()) == true }
                val nextPageNumber =
                    if (pageNumber < (response.body()?.total_pages ?: 0)) pageNumber + 1 else null
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                LoadResult.Page(populars, prevPageNumber, nextPageNumber)
            } else {
                Log.e("DDDD", "http error")
                LoadResult.Error(HttpException(response))
            }
        } catch (e: HttpException) {
            Log.e("DDDD", "http error2")
            return LoadResult.Error(e)
        } catch (e: Exception) {
            Log.e("DDDD", "error")
            return LoadResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {

        fun create(@Assisted("query") query: String): MoviePageSource
    }

    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }
}