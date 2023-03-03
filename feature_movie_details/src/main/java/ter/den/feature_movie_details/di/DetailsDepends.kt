package ter.den.feature_movie_details.di

import android.content.Context
import androidx.annotation.RestrictTo
import retrofit2.Retrofit
import ter.den.config.AppConfig
import kotlin.properties.Delegates

interface DetailsDepends {
    val context: Context
    val retrofit: Retrofit
    val appConfig: AppConfig
}

interface DetailsDependsProvider {

    @get:RestrictTo(RestrictTo.Scope.LIBRARY)
    val dependencies: DetailsDepends

    companion object : DetailsDependsProvider by DetailsDependsStore
}

object DetailsDependsStore : DetailsDependsProvider {

    override var dependencies: DetailsDepends by Delegates.notNull()
}