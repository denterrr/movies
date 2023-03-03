package ter.den.feature_movie_details.domain.usecases

import ter.den.feature_movie_details.domain.DetailsRepository
import javax.inject.Inject

class OnFavoriteStatusChangeUseCase @Inject constructor(
    private val repository: DetailsRepository
) {
    suspend operator fun invoke(id: String) = repository.onFavoriteStatusChange(id)
}