package ter.den.feature_profile.domain.usecases

import ter.den.feature_profile.domain.ProfileRepository
import javax.inject.Inject

class GetSessionIdFlowUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke() = repository.getSessionIdFlow()
}