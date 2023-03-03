package ter.den.feature_profile.di

import android.content.Context
import androidx.annotation.RestrictTo
import retrofit2.Retrofit
import ter.den.config.AppConfig
import ter.den.feature_auth.data.AuthDataSource
import ter.den.feature_movie_details.data.network.DetailsService
import kotlin.properties.Delegates

interface ProfileDepends {
    val context: Context
    val retrofit: Retrofit
    val appConfig: AppConfig
    val detailsService: DetailsService
    val authDataSource: AuthDataSource
}

interface ProfileDependsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dependencies: ProfileDepends

    companion object : ProfileDependsProvider by ProfileDependsStore
}

object ProfileDependsStore : ProfileDependsProvider {

    override var dependencies: ProfileDepends by Delegates.notNull()
}