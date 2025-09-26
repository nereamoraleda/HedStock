package es.cursos.android.ejercicios.stocksnma.domain.model


data class User(
    val id: Long? = null,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val username: String = "",
    val password: String = "",
    val role: String = "",
    val isActive: Boolean = true,
    val createdAt: String? = null,
    val storeId: Long? = null,
    val storeName: String? = null
)
