package ter.den.feature_profile.di

import androidx.lifecycle.ViewModel
import dagger.Component
import ter.den.core.di.annotation.FeatureScope
import ter.den.feature_profile.di.modules.DataModule
import ter.den.feature_profile.di.modules.DomainModule
import ter.den.feature_profile.di.modules.PresentationModule
import ter.den.feature_profile.ui.MoviesFragment
import ter.den.feature_profile.ui.ProfileFragment

@[FeatureScope
Component(
    dependencies = [ProfileDepends::class],
    modules = [DataModule::class, DomainModule::class, PresentationModule::class]
)]
internal interface ProfileComponent {

    fun inject(fragment: ProfileFragment)
    fun inject(fragment: MoviesFragment)

    @Component.Factory
    interface Factory {
        fun create(authDepends: ProfileDepends): ProfileComponent
    }
}

internal class ProfileComponentViewModel : ViewModel() {

    val profileComponent =
        DaggerProfileComponent.factory().create(ProfileDependsProvider.dependencies)
}