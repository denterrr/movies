package ter.den.feature_movie_details.di.modules

import dagger.Binds
import dagger.Module
import ter.den.core.di.annotation.FeatureScope
import ter.den.feature_movie_details.data.DetailsRepositoryImpl
import ter.den.feature_movie_details.domain.DetailsRepository

@Module
interface DomainModule {
    @[Binds FeatureScope]
    fun bindDetailsRepository(impl: DetailsRepositoryImpl): DetailsRepository
}