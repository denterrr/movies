package ter.den.feature_home.domain.usecases

import ter.den.feature_home.domain.HomeRepository
import javax.inject.Inject

class GetPopularFromDbUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    suspend operator fun invoke() = repository.getPopularFromDatabase()
}