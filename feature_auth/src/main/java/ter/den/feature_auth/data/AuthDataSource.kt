package ter.den.feature_auth.data

import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import ter.den.config.AppConfig
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.feature_auth.data.network.AuthService

interface AuthDataSource {
    suspend fun createSessionId(
        login: String,
        password: String,
    ): String
}

fun getAuthDataSource(
    @IoDispatcherQualifier ioDispatcherQualifier: CoroutineDispatcher,
    retrofit: Retrofit,
    appConfig: AppConfig
): AuthDataSource =
    AuthDataSourceImpl(ioDispatcherQualifier, retrofit.create(AuthService::class.java), appConfig)