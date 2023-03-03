package ter.den.feature_movie_details.domain.usecases

import ter.den.feature_movie_details.domain.DetailsRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: DetailsRepository
) {
    suspend operator fun invoke(id: Int) = repository.getMovieDetails(id)
}