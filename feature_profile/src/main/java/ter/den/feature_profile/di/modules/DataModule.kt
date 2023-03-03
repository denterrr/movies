package ter.den.feature_profile.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import ter.den.core.di.annotation.FeatureScope
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.feature_profile.data.network.ProfileService

@Module
internal interface DataModule {

    companion object {

        @[Provides IoDispatcherQualifier]
        fun provideCoroutineDispatcher() = Dispatchers.IO

        @[Provides FeatureScope]
        fun provideProfileService(retrofit: Retrofit): ProfileService =
            retrofit.create(ProfileService::class.java)
    }
}