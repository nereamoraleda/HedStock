package es.cursos.android.ejercicios.stocksnma.data.remote.dto.supplier

data class SupplierDto(
    val id: Long? = null,
    val name: String,
    val contactName: String?,
    val phone: String?,
    val email: String?,
    val address: String?,
    val zipCode: String?,
    val city: String,
    val country: String,
    val isActive: Boolean
 // val createdAt: String
)
