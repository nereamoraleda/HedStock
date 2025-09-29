package es.cursos.android.ejercicios.stocksnma.data.remote.dto

/**
 * DTO - Petición de cambio de contraseña
 */
data class ChangePasswordRequest (
    val username: String,
    val oldPassword: String,
    val newPassword: String
)