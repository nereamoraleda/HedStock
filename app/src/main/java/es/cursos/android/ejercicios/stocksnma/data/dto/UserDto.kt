package es.cursos.android.ejercicios.stocksnma.data.dto

data class UserDto(
    val id: Long? = null,
    val name: String,
    val email: String?,
    val phone: String?,
    val username: String,
    val password: String?,
    val role: String,
    val active: Boolean = true,
    val createdAt: String? = null, // LocalDateTime viene como String en JSON (ej. "2025-08-07T14:52:00")
    val storeId: Long?,
    val storeName: String?
)
