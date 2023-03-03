package ter.den.feature_search.data.network.model

import ter.den.feature_search.domain.model.Movie

data class MovieDto(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)