package ter.den.feature_profile.domain.usecases

import ter.den.feature_profile.domain.ProfileRepository
import javax.inject.Inject

class CreateSessionIdUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(login: String, password: String) =
        repository.createSessionId(login, password)
}