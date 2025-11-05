package es.cursos.android.ejercicios.stocksnma.data.remote.dto.supplier

/**
 * DTO - General
 *
 * @param id - Id del proveedor
 * @param name - Nombre del proveedor
 * @param contactName - Nombre de la persona de contacto
 * @param email - Email del proveedor
 * @param phone - Número de teléfono del proveedor
 * @param address - Dirección en la que se encuentra el proveedor
 * @param city - Ciudad en la que se encuentra el proveedor
 * @param country - País en el que se encuentra el proveedor
 * @param zipCode - Código postal en el que se encuentra el proveedor
 * @param isActive - Si el proveedor está disponible o no
 */
data class SupplierDto(
    val id: Long? = null,
    val name: String,
    val contactName: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val city: String,
    val country: String,
    val zipCode: String?,
    val isActive: Boolean
 // val createdAt: String
)


/**
 * DTO - ...
 */
data class SupplierRequest(
    val name: String,
    val contactName: String?,
    val phone: String?,
    val email: String?,
    val address: String?,
    val city: String,
    val country: String,
    val zipCode: String?,
    val isActive: Boolean
    // val createdAt: String
)


/**
 * DTO - Para la lista de proveedores en Home
 *
 * @param id - Id del proveedor
 * @param name - Nombre del proveedor
 * @param contactName - Nombre de la persona de contacto
 * @param email - Email del proveedor
 * @param phone - Teléfono del proveedor
 */
data class SupplierHomeViewDto(
    val id: Long,
    val name: String,
    val contactName: String?,
    val email: String?,
    val phone: String?
 // val isActive: Boolean
)


/**
 * DTO - Para la lista de proveedores en un menú de selección
 *
 * @param id - Id del proveedor
 * @param name - Nombre del proveedor
 */
data class SupplierSelectionMenuDto(
    val id: Long,
    val name: String
)
