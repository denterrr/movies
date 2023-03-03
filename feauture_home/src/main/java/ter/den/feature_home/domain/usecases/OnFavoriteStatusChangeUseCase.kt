package ter.den.feature_home.domain.usecases

import ter.den.feature_home.domain.HomeRepository
import javax.inject.Inject

class OnFavoriteStatusChangeUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(id: String) = repository.onFavoriteStatusChange(id)
}