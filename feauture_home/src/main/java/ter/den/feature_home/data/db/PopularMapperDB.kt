package ter.den.feature_home.data.db

import ter.den.feature_home.data.db.model.PopularDb
import ter.den.feature_home.domain.model.Popular

fun PopularDb.toPopular() = Popular(
    adult,
    backdrop_path,
    genre_ids.split(",").map { it.trim().toInt() },
    id,
    original_language,
    original_title,
    overview,
    popularity,
    poster_path,
    release_date,
    title,
    video,
    vote_average,
    vote_count
)

fun Popular.toPopularDB() = PopularDb(
    adult,
    backdrop_path,
    genre_ids.joinToString(","),
    id,
    original_language,
    original_title,
    overview,
    popularity,
    poster_path,
    release_date,
    title,
    video,
    vote_average,
    vote_count
)