package ter.den.feature_movie_details.domain

import ter.den.feature_movie_details.domain.model.Movie

interface DetailsRepository {
    suspend fun getMovieDetails(id: Int): Movie

    suspend fun onFavoriteStatusChange(id: String)
}