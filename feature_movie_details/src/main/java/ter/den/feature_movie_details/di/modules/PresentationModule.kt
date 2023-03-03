package ter.den.feature_movie_details.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ter.den.core.di.annotation.ViewModelKey
import ter.den.feature_movie_details.presentation.DetailsViewModel

@Module
internal interface PresentationModule {
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    @Binds
    fun bindNoteViewModel(impl: DetailsViewModel): ViewModel

}