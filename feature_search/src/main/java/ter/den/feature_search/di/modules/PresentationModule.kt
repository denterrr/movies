package ter.den.feature_search.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ter.den.core.di.annotation.ViewModelKey
import ter.den.feature_search.presentation.SearchViewModel

@Module
internal interface PresentationModule {
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    @Binds
    fun bindHomeViewModel(impl: SearchViewModel): ViewModel

}