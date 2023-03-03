package ter.den.feature_search.data.network

import androidx.annotation.IntRange
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ter.den.core.domain.MovieDB
import ter.den.feature_search.data.network.model.MovieDto

interface SearchService {

    @GET("/3/search/movie?api_key=${MovieDB.API_KEY}&language=en-US")
    suspend fun search(
        @Query("query") query: String,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Response<MovieDto>
}