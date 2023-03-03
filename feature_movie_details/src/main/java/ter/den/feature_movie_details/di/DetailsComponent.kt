package ter.den.feature_movie_details.di

import androidx.lifecycle.ViewModel
import dagger.Component
import ter.den.core.di.annotation.FeatureScope
import ter.den.feature_movie_details.di.modules.DataModule
import ter.den.feature_movie_details.di.modules.DomainModule
import ter.den.feature_movie_details.di.modules.PresentationModule
import ter.den.feature_movie_details.ui.DetailsFragment

@[FeatureScope
Component(
    dependencies = [DetailsDepends::class],
    modules = [DataModule::class, DomainModule::class, PresentationModule::class]
)]
internal interface DetailsComponent {

    fun inject(fragment: DetailsFragment)

    @Component.Factory
    interface Factory {
        fun create(detailsDepends: DetailsDepends): DetailsComponent
    }
}

internal class DetailsComponentViewModel : ViewModel() {

    val detailsComponent =
        DaggerDetailsComponent.factory().create(DetailsDependsProvider.dependencies)
}