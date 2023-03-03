package ter.den.feature_home.domain

import androidx.paging.PagingSource
import ter.den.feature_home.domain.model.Popular

interface HomeRepository {
    fun getPopularContent(): PagingSource<Int, Popular>

    suspend fun getPopularFromDatabase(): List<Popular>
    suspend fun onFavoriteStatusChange(id: String)
}