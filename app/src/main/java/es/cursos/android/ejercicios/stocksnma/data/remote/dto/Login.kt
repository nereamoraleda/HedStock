package es.cursos.android.ejercicios.stocksnma.data.remote.dto

/**
 * DTO - Petición de inicio de sesión
 */
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * DTO - Respuesta del inicio de sesión
 */
data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val role: String,
    val mustChangePassword: Boolean
    //val storeId: Long
)