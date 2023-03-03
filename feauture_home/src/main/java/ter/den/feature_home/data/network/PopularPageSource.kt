package ter.den.feature_home.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import retrofit2.HttpException
import ter.den.config.AppConfig
import ter.den.feature_home.data.db.PopularDao
import ter.den.feature_home.data.db.toPopularDB
import ter.den.feature_home.domain.model.Popular
import javax.inject.Inject

class PopularPageSource @Inject constructor(
    private val homeService: HomeService,
    private val dao: PopularDao,
    private val appConfig: AppConfig,
) : PagingSource<Int, Popular>() {


    override fun getRefreshKey(state: PagingState<Int, Popular>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Popular> {
        try {
            val pageNumber = params.key ?: INITIAL_PAGE_NUMBER
            val response = homeService.getPopularContent(pageNumber)
            return if (response.isSuccessful) {
                val dbContent = dao.getPopular()
                val populars = response.body()?.results ?: emptyList()
                val favorites = appConfig.favorites.stateIn(CoroutineScope(Dispatchers.IO)).value
                populars.map { it.isFavorite = favorites?.contains(it.id.toString()) == true }
                if (dbContent.size < populars.size) dao.insert(populars.map { it.toPopularDB() })
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


    companion object {
        const val INITIAL_PAGE_NUMBER = 1
    }
}