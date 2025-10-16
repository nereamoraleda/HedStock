package es.cursos.android.ejercicios.stocksnma.data.remote.dto

/** StoreDto - Dto para obtener todos los datos de una tienda
 *
 * @param id - Id de la tienda
 * @param name - Nombre de la tienda
 * @param email - Email de la tienda (Opcional)
 * @param phone - Teléfono de la tienda (Opcional)
 * @param address - Dirección en la que se ubica la tienda (Opcional)
 * @param city - Ciudad en la que se ubica la tienda (Opcional)
 * @param country - País en el que se ubica la tienda (Opcional)
 * @param postalCode - C.P en el que se ubica la tienda (Opcional)
 * @param isActive - Opción para saber si la tienda está activa o inactiva (Por defecto: true)
 * @param createdAt - Fecha de creación de la tienda
 */
data class StoreDto(
    val id: Long? = null,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
    val postalCode: String? = null,
    val isActive: Boolean? = true,
    val createdAt: String? = null
)


/** StoreGeneralViewDto - Dto para obtener los datos necesarios que se muestran en el listado de tiendas
 *
 * @param id - Id de la tienda
 * @param name - Nombre de la tienda
 * @param email - Email de la tienda (Opcional)
 * @param city - Ciudad en la que se ubica la tienda (Opcional)
 */
data class StoreGeneralViewDto(
    val id: Long,
    val name: String,
    val email: String? = null,
    val city: String? = null
)


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
    //val isActive: Boolean?
)


// Se usa para crear/actualizar una tienda (lo que se envía al backend)
// data class StoreRequest(
//    val name: String,
//    val code: String,
//    val description: String? = null,
//    val phone: String? = null,
//    val email: String? = null,
//    val address: String? = null,
//    val city: String? = null,
//    val country: String? = null
//)

// Se usa para obtener o listar tiendas (lo que devuelve el backend)
//data class StoreResponse(
//    val id: Long,
//    val name: String,
//    val code: String,
//    val description: String?,
//    val phone: String?,
//    val email: String?,
//    val address: String?,
//    val city: String?,
//    val country: String?,
//    val createdAt: String?,
//    val updatedAt: String?
//)

