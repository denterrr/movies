package ter.den.feature_search.domain.usecases

import ter.den.feature_search.domain.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(query: String) = repository.search(query)
}