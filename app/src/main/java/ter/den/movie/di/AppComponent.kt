package ter.den.movie.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ter.den.feature_home.di.HomeDepends
import ter.den.feature_movie_details.di.DetailsDepends
import ter.den.feature_profile.di.ProfileDepends
import ter.den.feature_search.di.SearchDepends
import ter.den.movie.di.annotation.AppScope
import ter.den.movie.di.modules.AppModule
import ter.den.movie.ui.MainActivity

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent : HomeDepends, DetailsDepends, SearchDepends, ProfileDepends{


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }

}

