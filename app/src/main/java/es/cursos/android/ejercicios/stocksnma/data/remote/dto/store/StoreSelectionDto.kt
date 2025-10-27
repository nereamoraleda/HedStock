package es.cursos.android.ejercicios.stocksnma.data.remote.dto.store

/** StoreSelectionDto - Dto para obtener los datos que se necesitan para asignar una tienda a algún item
 *
 * @param id - Id de la tienda
 * @param name - Nombre de la tienda
 *
 * El listado enviado solo contendrá tiendas que estén activas
 */
data class StoreSelectionDto(
    val id: Long,
    val name: String
)
