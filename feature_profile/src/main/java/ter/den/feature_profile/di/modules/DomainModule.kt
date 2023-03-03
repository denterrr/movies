package ter.den.feature_profile.di.modules

import dagger.Binds
import dagger.Module
import ter.den.core.di.annotation.FeatureScope
import ter.den.feature_profile.data.ProfileRepositoryImpl
import ter.den.feature_profile.domain.ProfileRepository

@Module
internal interface DomainModule {
    @[Binds FeatureScope]
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}