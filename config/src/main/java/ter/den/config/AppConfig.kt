package ter.den.config

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AppConfig {
    val sessionId: Flow<String?>
    val favorites: Flow<Set<String>?>

    suspend fun setSessionId(id: String)
    suspend fun setFavorites(set: Set<String>)
    suspend fun removeFromFavorites(id: String)
    suspend fun addToFavorites(id: String)
    suspend fun addAllToFavorites(ids: List<String>)
}

fun getAppConfig(context: Context): AppConfig = AppConfigImpl(context)