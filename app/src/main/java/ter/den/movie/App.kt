package ter.den.movie

import android.app.Application
import android.content.Context
import ter.den.feature_home.di.HomeDependsStore
import ter.den.feature_movie_details.di.DetailsDependsStore
import ter.den.feature_profile.di.ProfileDependsStore
import ter.den.feature_search.di.SearchDependsStore
import ter.den.movie.di.AppComponent
import ter.den.movie.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        HomeDependsStore.dependencies = appComponent
        DetailsDependsStore.dependencies = appComponent
        SearchDependsStore.dependencies = appComponent
        ProfileDependsStore.dependencies = appComponent
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> applicationContext.appComponent
    }
