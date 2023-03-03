package ter.den.movie.di.modules

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ter.den.config.AppConfig
import ter.den.config.getAppConfig
import ter.den.core.di.annotation.FeatureScope
import ter.den.core.di.annotation.IoDispatcherQualifier
import ter.den.core.domain.MovieDB
import ter.den.core.domain.model.CustomThrowable
import ter.den.feature_auth.data.AuthDataSource
import ter.den.feature_auth.data.getAuthDataSource
import ter.den.feature_movie_details.data.network.DetailsService

@Module
interface DataModule {

    companion object {

        @[Provides IoDispatcherQualifier]
        fun provideCoroutineDispatcher() = Dispatchers.IO

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            .build()

        @Provides
        fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .baseUrl(MovieDB.BASE_URL)
            .client(okHttpClient)
            .build()
            ?: throw CustomThrowable.RetrofitNull

        @Provides
        fun provideAppConfig(context: Context): AppConfig = getAppConfig(context)

        @Provides
        fun provideDetailsService(retrofit: Retrofit): DetailsService =
            retrofit.create(DetailsService::class.java)

        @Provides
        fun provideAuthDataSource(
            @IoDispatcherQualifier ioDispatcherQualifier: CoroutineDispatcher,
            retrofit: Retrofit,
            appConfig: AppConfig
        ): AuthDataSource = getAuthDataSource(ioDispatcherQualifier, retrofit, appConfig)

    }
}