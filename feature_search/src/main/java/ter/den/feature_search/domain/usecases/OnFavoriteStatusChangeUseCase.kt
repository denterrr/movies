package ter.den.feature_search.domain.usecases

import ter.den.feature_search.domain.SearchRepository
import javax.inject.Inject

class OnFavoriteStatusChangeUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(id: String) = repository.onFavoriteStatusChange(id)
}