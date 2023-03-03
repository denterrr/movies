package ter.den.feature_search.domain

import androidx.paging.PagingSource
import ter.den.feature_search.domain.model.Movie

interface SearchRepository {
    fun search(query: String): PagingSource<Int, Movie>

    suspend fun onFavoriteStatusChange(id: String)
}