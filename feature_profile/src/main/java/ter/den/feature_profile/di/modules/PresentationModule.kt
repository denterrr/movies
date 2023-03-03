package ter.den.feature_profile.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ter.den.core.di.annotation.ViewModelKey
import ter.den.feature_profile.presentation.ProfileViewModel

@Module
internal interface PresentationModule {

    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    @Binds
    fun bindProfileViewModel(impl: ProfileViewModel): ViewModel
}