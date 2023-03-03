package ter.den.feature_profile.data.network.model

import ter.den.feature_profile.domain.model.Movie

data class MovieDto(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)