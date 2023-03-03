package ter.den.feature_home.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ter.den.core.di.annotation.ViewModelKey
import ter.den.feature_home.presentation.HomeViewModel

@Module
internal interface PresentationModule {
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    @Binds
    fun bindHomeViewModel(impl: HomeViewModel): ViewModel

}