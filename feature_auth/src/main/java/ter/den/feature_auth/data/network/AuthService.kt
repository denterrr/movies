package ter.den.feature_auth.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ter.den.core.domain.MovieDB
import ter.den.feature_auth.data.network.model.RequestLoginAndPasswordDto
import ter.den.feature_auth.data.network.model.RequestTokenDto
import ter.den.feature_auth.data.network.model.RequestTokenForSessionDto
import ter.den.feature_auth.data.network.model.SessionDto

internal interface AuthService {

    @GET("/3/authentication/token/new?api_key=${MovieDB.API_KEY}")
    suspend fun getRequestToken(): Response<RequestTokenDto>

    @POST("/3/authentication/token/validate_with_login?api_key=${MovieDB.API_KEY}")
    suspend fun authTokenWithLoginAndPassword(
        @Body body: RequestLoginAndPasswordDto,
    ): Response<RequestTokenDto>

    @POST("/3/authentication/session/new?api_key=${MovieDB.API_KEY}")
    suspend fun createSessionId(
        @Body body: RequestTokenForSessionDto,
    ): Response<SessionDto>

}