package ter.den.feature_search.di

import androidx.lifecycle.ViewModel
import dagger.Component
import ter.den.core.di.annotation.FeatureScope
import ter.den.feature_search.di.modules.DataModule
import ter.den.feature_search.di.modules.DomainModule
import ter.den.feature_search.di.modules.PresentationModule
import ter.den.feature_search.ui.SearchFragment

@[FeatureScope
Component(
    dependencies = [SearchDepends::class],
    modules = [DataModule::class, DomainModule::class, PresentationModule::class]
)]
internal interface SearchComponent {

    fun inject(fragment: SearchFragment)

    @Component.Factory
    interface Factory {
        fun create(authDepends: SearchDepends): SearchComponent
    }
}

internal class SearchComponentViewModel : ViewModel() {

    val searchComponent =
        DaggerSearchComponent.factory().create(SearchDependsProvider.dependencies)
}