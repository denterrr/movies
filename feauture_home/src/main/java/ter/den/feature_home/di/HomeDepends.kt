package ter.den.feature_home.di

import android.content.Context
import androidx.annotation.RestrictTo
import retrofit2.Retrofit
import ter.den.config.AppConfig
import ter.den.feature_movie_details.data.network.DetailsService
import kotlin.properties.Delegates

interface HomeDepends {
    val context: Context
    val retrofit: Retrofit
    val appConfig: AppConfig
    val detailsService: DetailsService
}

interface HomeDependsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dependencies: HomeDepends

    companion object : HomeDependsProvider by HomeDependsStore
}

object HomeDependsStore : HomeDependsProvider {

    override var dependencies: HomeDepends by Delegates.notNull()
}