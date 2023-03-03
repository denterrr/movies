package ter.den.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AppConfigImpl.DATA_STORE_NAME)

internal class AppConfigImpl(private val context: Context) : AppConfig {


    private val SESSION_ID_PREFERENCES = stringPreferencesKey(SESSION_ID_KEY)
    private val FAVORITES = stringSetPreferencesKey(FAVORITES_KEY)

    override val sessionId = context.dataStore.data.map { preferences ->
        preferences[SESSION_ID_PREFERENCES]
    }

    override val favorites = context.dataStore.data.map { preferences ->
        preferences[FAVORITES]
    }

    override suspend fun setSessionId(id: String) {
        context.dataStore.edit { preferences ->
            preferences[SESSION_ID_PREFERENCES] = id
        }
    }

    override suspend fun setFavorites(set: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITES] = set
        }
    }

    override suspend fun removeFromFavorites(id: String) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITES]?.toMutableSet()?.remove(id)
        }
    }

    override suspend fun addToFavorites(id: String) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITES]?.toMutableSet()?.add(id)
        }
    }

    override suspend fun addAllToFavorites(ids: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITES]?.toMutableSet()?.addAll(ids)
        }
    }


    companion object {
        const val SESSION_ID_KEY = "SESSION_ID_KEY"
        const val FAVORITES_KEY = "FAVORITES_KEY"
        const val DATA_STORE_NAME = "DATA_STORE_NAME"
    }
}