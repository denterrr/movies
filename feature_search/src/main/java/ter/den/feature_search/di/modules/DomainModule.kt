package ter.den.feature_search.di.modules

import dagger.Binds
import dagger.Module
import ter.den.core.di.annotation.FeatureScope
import ter.den.feature_search.data.SearchRepositoryImpl
import ter.den.feature_search.domain.SearchRepository

@Module
interface DomainModule {
    @[Binds FeatureScope]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}