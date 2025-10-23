package es.cursos.android.ejercicios.stocksnma.data.remote.dto.store

/** StoreResponseDto - Dto para obtener todos los datos de una tienda
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
 * //@param createdAt - Fecha de creación de la tienda
 */
data class StoreResponseDto(
    val id: Long,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val city: String? = null,
    val country: String? = null,
    val postalCode: String? = null,
    val isActive: Boolean,
    //val createdAt: String?  //- De momento no se usa
)
