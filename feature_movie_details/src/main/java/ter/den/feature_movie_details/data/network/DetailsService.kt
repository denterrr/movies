package ter.den.feature_movie_details.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ter.den.core.domain.MovieDB
import ter.den.feature_movie_details.data.network.model.FavoriteMarkDto
import ter.den.feature_movie_details.data.network.model.MovieDto
import ter.den.feature_movie_details.data.network.model.favorite.FavoriteMarkResponseDto

interface DetailsService {
    @GET("/3/movie/{movie_id}?api_key=${MovieDB.API_KEY}&language=en-US")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int = 0
    ): Response<MovieDto>

    @POST("https://api.themoviedb.org/3/account/1/favorite?api_key=${MovieDB.API_KEY}")
    suspend fun markFavorite(
        @Query("session_id") sessionId: String,
        @Body body: FavoriteMarkDto
    ): Response<FavoriteMarkResponseDto>
}