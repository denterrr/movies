package ter.den.feature_movie_details.data.network.model

data class FavoriteMarkDto(
    val favorite: Boolean,
    val media_id: Int,
    val media_type: String = "movie"
)