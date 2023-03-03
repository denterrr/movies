package ter.den.feature_search.di

import android.content.Context
import androidx.annotation.RestrictTo
import retrofit2.Retrofit
import ter.den.config.AppConfig
import ter.den.feature_movie_details.data.network.DetailsService
import kotlin.properties.Delegates

interface SearchDepends {
    val context: Context
    val retrofit: Retrofit
    val appConfig: AppConfig
    val detailsService: DetailsService
}

interface SearchDependsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dependencies: SearchDepends

    companion object : SearchDependsProvider by SearchDependsStore
}

object SearchDependsStore : SearchDependsProvider {

    override var dependencies: SearchDepends by Delegates.notNull()
}