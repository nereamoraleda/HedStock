package es.cursos.android.ejercicios.stocksnma.domain.model

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
    val createdAt: String? = null
)


data class StoreGeneralView(
    val id: Long? = null,
    val name: String = "",
    val email: String = "",
    val city: String = ""
)



data class StoreSelection(
    val id: Long,
    val name: String
    //val isActive: Boolean?
)