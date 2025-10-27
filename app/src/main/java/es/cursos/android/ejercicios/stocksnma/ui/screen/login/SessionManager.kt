package es.cursos.android.ejercicios.stocksnma.ui.screen.login

import es.cursos.android.ejercicios.stocksnma.data.local.datastore.AppDataStore
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
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


class AuthInterceptor( // network o di/newtwork
    private val dataStoreManager: AppDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // ✅ No añadir token en /login
        if (originalRequest.url().encodedPath().contains("/api/login")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking { dataStoreManager.token.firstOrNull() }

        return if (!token.isNullOrBlank()) {
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }
}
