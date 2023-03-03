package ter.den.feature_auth.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ter.den.config.AppConfig
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.core.domain.model.CustomThrowable
import ter.den.feature_auth.data.network.AuthService
import ter.den.feature_auth.data.network.model.RequestLoginAndPasswordDto
import ter.den.feature_auth.data.network.model.RequestTokenForSessionDto
import javax.inject.Inject

internal class AuthDataSourceImpl @Inject constructor(
    @IoDispatcherQualifier private val ioDispatcher: CoroutineDispatcher,
    private val authService: AuthService,
    private val appConfig: AppConfig
) : AuthDataSource {
    override suspend fun createSessionId(
        login: String,
        password: String,
    ): String = withContext(ioDispatcher) {
        val requestTokenResponse = authService.getRequestToken()
        if (requestTokenResponse.isSuccessful) {
            val requestToken = requestTokenResponse.body()?.request_token.toString()
            val authTokenResponse = authService.authTokenWithLoginAndPassword(
                RequestLoginAndPasswordDto(
                    login,
                    password,
                    requestToken
                )
            )
            if (authTokenResponse.isSuccessful) {
                val sessionResponse =
                    authService.createSessionId(RequestTokenForSessionDto(requestToken))
                if (sessionResponse.isSuccessful) {
                    val sessionId =
                        sessionResponse.body()?.session_id ?: throw CustomThrowable.SessionIdNull
                    appConfig.setSessionId(sessionId)
                    sessionId
                } else throw CustomThrowable.Error(sessionResponse.toString())
            } else throw CustomThrowable.Error(authTokenResponse.toString())
        } else throw CustomThrowable.Error(requestTokenResponse.toString())

    }
}