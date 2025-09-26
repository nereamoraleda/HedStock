package es.cursos.android.ejercicios.stocksnma.domain.model

data class Store(
    val id: Long,
    val name: String,
    val phone: String?,
    val email: String?,
    val address: String?,
    val city: String?,
    val country: String?,
    val postalCode: String?,
    val isActive: Boolean?,
    val createdAt: String?
)


data class StoreSummary(
    val id: Long,
    val name: String,
    val isActive: Boolean?,
    val createdAt: String?
)