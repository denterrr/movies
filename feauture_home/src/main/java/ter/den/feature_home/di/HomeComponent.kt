package ter.den.feature_home.di

import androidx.lifecycle.ViewModel
import dagger.Component
import ter.den.core.di.annotation.FeatureScope
import ter.den.feature_home.di.modules.DataModule
import ter.den.feature_home.di.modules.DomainModule
import ter.den.feature_home.di.modules.PresentationModule
import ter.den.feature_home.ui.HomeFragment

@[FeatureScope
Component(
    dependencies = [HomeDepends::class],
    modules = [DataModule::class, DomainModule::class, PresentationModule::class]
)]
internal interface HomeComponent {

    fun inject(fragment: HomeFragment)

    @Component.Factory
    interface Factory {
        fun create(authDepends: HomeDepends): HomeComponent
    }
}

internal class HomeComponentViewModel : ViewModel() {

    val homeComponent =
        DaggerHomeComponent.factory().create(HomeDependsProvider.dependencies)
}