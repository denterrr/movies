package ter.den.feature_profile.domain.usecases

import ter.den.feature_profile.domain.ProfileRepository
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    operator fun invoke(sessionId: String) = repository.getFavorites(sessionId)
}