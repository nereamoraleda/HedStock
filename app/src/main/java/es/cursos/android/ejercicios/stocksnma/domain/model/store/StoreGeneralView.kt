package es.cursos.android.ejercicios.stocksnma.domain.model.store

data class StoreGeneralView(
    val id: Long? = null,  /* TODO - Modificar? Siempre se recibe? Debería ser no null?*/
    val name: String,
    val email: String?,
    val city: String?
)
