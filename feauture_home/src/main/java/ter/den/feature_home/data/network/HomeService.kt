package ter.den.feature_home.data.network

import androidx.annotation.IntRange
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ter.den.core.domain.MovieDB
import ter.den.feature_home.data.network.model.PopularDto

interface HomeService {

    @GET("/3/movie/popular?api_key=${MovieDB.API_KEY}&language=en-US")
    suspend fun getPopularContent(
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Response<PopularDto>
}