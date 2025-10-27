package es.cursos.android.ejercicios.stocksnma.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(private val context: Context) {

    val session = SessionPreferences(context)
    val sort = SortPreferences(context)
    val search = SearchPreferences(context)

    private val Context.dataStore by preferencesDataStore(name = "hedstock_preferences")

    // -------------------- CLAVES DE LAS PREFERENCIAS -------------------- //
    companion object {
        // Claves del inicio de sesión
        private val USER_ID_KEY = longPreferencesKey("user_id")
        private val STORE_ID_KEY = longPreferencesKey("store_id")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val TOKEN_KEY = stringPreferencesKey("token")

        // Claves de los historiales de búsqueda
        // Claves de las preferencias de ordenación
    }


    // -------------------- VALORES DE LAS PREFERENCIAS -------------------- //
    val userId: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY] ?: -1L
    }
    val storeId: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[STORE_ID_KEY] ?: -1L
    }
    val userRole: Flow<UserRoles> = context.dataStore.data.map { preferences ->
        val savedValue = preferences[USER_ROLE_KEY]
        UserRoles.entries.find { it.name == savedValue } ?: UserRoles.DESCONOCIDO
    }
    val token: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] ?: ""
    }


    // -------------------- ACCIONES DE LAS PREFERENCIAS -------------------- //
    // LOGIN
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveUserRole(role: UserRoles) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role.name
        }
    }

    suspend fun clearSessionData() {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = -1L
            preferences[STORE_ID_KEY] = -1L
            preferences[USER_ROLE_KEY] = ""
            preferences[TOKEN_KEY] = ""
        }
    }


    // ORDENACIÓN
    // HISTORIALES DE BÚSQUEDA
}