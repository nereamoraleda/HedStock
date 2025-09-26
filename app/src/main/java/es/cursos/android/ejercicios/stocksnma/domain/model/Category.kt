package es.cursos.android.ejercicios.stocksnma.domain.model

/**
 * DATA CLASS - Category
 * Clase intermedia con CategoryEntity, la BD y la UI (conversiones)
 *
 * @param id Identificador único de la categoría
 * @param name Nombre de la categoría
 */
data class Category(
    val id: Int = 0,
    val name: String = ""
    //val description: String = ""
)
