package ter.den.feature_profile.domain

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import ter.den.feature_profile.data.network.model.account.AccountDetailsDto
import ter.den.feature_profile.domain.model.AccountDetails
import ter.den.feature_profile.domain.model.Movie

interface ProfileRepository {
    suspend fun getAccountDetails(sessionId: String): AccountDetails
    fun getFavorites(sessionId: String): PagingSource<Int, Movie>
    fun getWatchList(sessionId: String): PagingSource<Int, Movie>
    fun getSessionIdFlow(): Flow<String?>
    suspend fun createSessionId(
        login: String,
        password: String,
    ): String

    suspend fun onFavoriteStatusChange(id: String)
}