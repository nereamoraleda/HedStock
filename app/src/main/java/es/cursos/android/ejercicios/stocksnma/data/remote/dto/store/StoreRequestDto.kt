package es.cursos.android.ejercicios.stocksnma.data.remote.dto.store

//Se usa para crear/actualizar una tienda (lo que se envía al backend)
data class StoreRequestDto(
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
    val postalCode: String? = null,
    val isActive: Boolean?,   // No para Create - Sí para modificar
    //val createdAt: String? = null
)
