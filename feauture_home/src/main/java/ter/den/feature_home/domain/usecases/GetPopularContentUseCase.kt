package ter.den.feature_home.domain.usecases

import ter.den.feature_home.domain.HomeRepository
import javax.inject.Inject

class GetPopularContentUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke() = repository.getPopularContent()
}