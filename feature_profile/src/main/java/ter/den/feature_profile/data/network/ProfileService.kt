package ter.den.feature_profile.data.network

import androidx.annotation.IntRange
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ter.den.core.domain.MovieDB
import ter.den.feature_profile.data.network.model.MovieDto
import ter.den.feature_profile.data.network.model.account.AccountDetailsDto

interface ProfileService {

    @GET("/3/account?api_key=${MovieDB.API_KEY}")
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String,
    ): Response<AccountDetailsDto>

    @GET("/3/account/1/watchlist/movies?api_key=${MovieDB.API_KEY}&language=en-US")
    suspend fun getMoviesWatchList(
        @Query("session_id") sessionId: String,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Response<MovieDto>

    @GET("/3/account/1/favorite/movies?api_key=${MovieDB.API_KEY}&language=en-US")
    suspend fun getFavoriteMovies(
        @Query("session_id") sessionId: String,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Response<MovieDto>

}