package ter.den.feature_profile.domain.usecases

import ter.den.feature_profile.domain.ProfileRepository
import javax.inject.Inject

class GetAccountDetailsUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(sessionId: String) = repository.getAccountDetails(sessionId)
}