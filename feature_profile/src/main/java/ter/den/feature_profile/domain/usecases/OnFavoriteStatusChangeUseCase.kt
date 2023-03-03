package ter.den.feature_profile.domain.usecases

import ter.den.feature_profile.domain.ProfileRepository
import javax.inject.Inject

class OnFavoriteStatusChangeUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(id: String) = repository.onFavoriteStatusChange(id)
}