package es.cursos.android.ejercicios.stocksnma.domain.model.store

data class Store(
    val id: Long? = null,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val city: String = "",
    val country: String = "",
    val postalCode: String = "",
    val isActive: Boolean = true,
    //val createdAt: String? = null
)
