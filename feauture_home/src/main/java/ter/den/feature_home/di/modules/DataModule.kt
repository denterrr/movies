package ter.den.feature_home.di.modules

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import ter.den.core.di.annotation.FeatureScope
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.feature_home.data.HomeDataSource
import ter.den.feature_home.data.HomeDataSourceImpl
import ter.den.feature_home.data.db.PopularDao
import ter.den.feature_home.data.db.PopularDatabase
import ter.den.feature_home.data.network.HomeService

@Module
internal interface DataModule {

    @[Binds FeatureScope]
    fun bindHomeDataSource(homeDataSourceImpl: HomeDataSourceImpl): HomeDataSource

    companion object {

        @[Provides FeatureScope]
        fun provideDao(context: Context): PopularDao = PopularDatabase.getInstance(context)

        @[Provides FeatureScope]
        fun provideHomeService(retrofit: Retrofit): HomeService =
            retrofit.create(HomeService::class.java)

        @[Provides IoDispatcherQualifier]
        fun provideCoroutineDispatcher() = Dispatchers.IO
    }
}