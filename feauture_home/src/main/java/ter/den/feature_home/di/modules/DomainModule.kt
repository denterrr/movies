package ter.den.feature_home.di.modules

import dagger.Binds
import dagger.Module
import ter.den.core.di.annotation.FeatureScope
import ter.den.feature_home.data.HomeRepositoryImpl
import ter.den.feature_home.domain.HomeRepository

@Module
interface DomainModule {
    @[Binds FeatureScope]
    fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository
}