package es.cursos.android.ejercicios.stocksnma.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore("session_preferences")

class SessionPreferences(private val context: Context) {

    // -------------------- CLAVES DE LAS PREFERENCIAS -------------------- //
    companion object {
        private val USER_ID_KEY = longPreferencesKey("user_id")
        private val STORE_ID_KEY = longPreferencesKey("store_id")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val TOKEN_KEY = stringPreferencesKey("token")
    }


    // -------------------- VALORES DE LAS PREFERENCIAS --------------------
    val userId: Flow<Long> = context.sessionDataStore.data.map { it[USER_ID_KEY] ?: -1L }
    val storeId: Flow<Long> = context.sessionDataStore.data.map { it[STORE_ID_KEY] ?: -1L }
    val userRole: Flow<UserRoles> = context.sessionDataStore.data.map {
        val savedValue = it[USER_ROLE_KEY]
        UserRoles.entries.find { it.name == savedValue } ?: UserRoles.DESCONOCIDO
    }
    val token: Flow<String> = context.sessionDataStore.data.map { it[TOKEN_KEY] ?: "" }


    // ---------- ACCIONES DE LAS PREFERENCIAS ----------
//    suspend fun saveSession(token: String, role: String, username: String) {
//        context.sessionDataStore.edit { prefs ->
//            prefs[TOKEN_KEY] = token
//            prefs[ROLE_KEY] = role
//            prefs[USERNAME_KEY] = username
//            prefs[IS_LOGGED_IN] = true
//        }
//    }

    suspend fun logout() {
        context.sessionDataStore.edit { it.clear() }
    }


//    suspend fun saveToken(token: String) {
//        context.sessionDataStore.edit { preferences ->
//            preferences[TOKEN_KEY] = token
//        }
//    }
//
//    suspend fun saveUserId(userId: Long) {
//        context.sessionDataStore.edit { preferences ->
//            preferences[USER_ID_KEY] = userId
//        }
//    }
//
//    suspend fun saveStoreId(storeId: Long) {
//        context.sessionDataStore.edit { preferences ->
//            preferences[STORE_ID_KEY] = storeId
//        }
//    }
//
//    suspend fun saveUserRole(role: UserRoles) {
//        context.sessionDataStore.edit { preferences ->
//            preferences[USER_ROLE_KEY] = role.name
//        }
//    }
//
//    suspend fun clearSessionData() {
//        context.sessionDataStore.edit { preferences ->
//            preferences[USER_ID_KEY] = -1L
//            preferences[STORE_ID_KEY] = -1L
//            preferences[USER_ROLE_KEY] = ""
//            preferences[TOKEN_KEY] = ""
//        }
//    }
}