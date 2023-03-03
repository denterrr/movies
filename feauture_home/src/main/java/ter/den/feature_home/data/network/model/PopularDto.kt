package ter.den.feature_home.data.network.model

import ter.den.feature_home.domain.model.Popular

data class PopularDto(
    val page: Int,
    val results: List<Popular>,
    val total_pages: Int,
    val total_results: Int
)