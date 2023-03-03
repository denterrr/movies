package ter.den.feature_search.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import ter.den.core.di.annotation.FeatureScope
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.feature_search.data.network.SearchService

@Module
internal interface DataModule {


    companion object {
        @[Provides IoDispatcherQualifier]
        fun provideCoroutineDispatcher() = Dispatchers.IO

        @[Provides FeatureScope]
        fun provideHomeService(retrofit: Retrofit): SearchService =
            retrofit.create(SearchService::class.java)
    }
}