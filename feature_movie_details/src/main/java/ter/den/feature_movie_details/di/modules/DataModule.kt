package ter.den.feature_movie_details.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import ter.den.core.di.annotation.FeatureScope
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.feature_movie_details.data.network.DetailsService

@Module
internal interface DataModule {

//    @[Binds FeatureScope]
//    fun bindHomeDataSource(homeDataSourceImpl: HomeDataSourceImpl): HomeDataSource

    companion object {

        @[Provides FeatureScope]
        fun provideDetailsService(retrofit: Retrofit): DetailsService =
            retrofit.create(DetailsService::class.java)

        @[Provides IoDispatcherQualifier]
        fun provideCoroutineDispatcher() = Dispatchers.IO
    }
}