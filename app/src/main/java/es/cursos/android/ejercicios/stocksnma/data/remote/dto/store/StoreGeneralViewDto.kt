package es.cursos.android.ejercicios.stocksnma.data.remote.dto.store

/** StoreGeneralViewDto - Dto para obtener los datos necesarios que se muestran en el listado de tiendas
 *
 * @param id - Id de la tienda
 * @param name - Nombre de la tienda
 * @param email - Email de la tienda (Opcional)
 * @param city - Ciudad en la que se ubica la tienda (Opcional)
 */
data class StoreGeneralViewDto(
    val id: Long, /* TODO - Modificar? Debería siempre ser recibido */
    val name: String,
    val email: String?,
    val city: String?,
    val isActive: Boolean
)
