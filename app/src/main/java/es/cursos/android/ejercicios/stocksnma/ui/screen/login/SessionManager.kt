package es.cursos.android.ejercicios.stocksnma.ui.screen.login

import es.cursos.android.ejercicios.stocksnma.data.local.datastore.AppDataStore
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val dataStoreManager: AppDataStore
) {
    val isLoggedIn: Flow<Boolean> = dataStoreManager.token.map { it.isNotBlank() }

    suspend fun saveSession(token: String, /*userId: Long,*/ role: UserRoles) {
        dataStoreManager.saveToken(token)
        //dataStoreManager.saveUserId(userId)
        //dataStoreManager.saveStoreId(storeId)
        dataStoreManager.saveUserRole(role)
    }

    suspend fun clearSession() {
        dataStoreManager.clearSessionData()
    }

    fun getUserRole() : Flow<UserRoles> = dataStoreManager.userRole
    fun getToken() : Flow<String> = dataStoreManager.token
}