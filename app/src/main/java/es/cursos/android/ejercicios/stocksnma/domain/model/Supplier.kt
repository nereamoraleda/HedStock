package es.cursos.android.ejercicios.stocksnma.domain.model


/**
 * MODEL - General
 *
 * @param id - Identificador único del proveedor
 * @param name - Nombre del proveedor
 * @param contactName - Nombre del contacto
 * @param email - Email del contacto
 * @param phone - Teléfono del contacto
 * @param address - Dirección del proveedor
 * @param country - País del proveedor
 * @param city - Ciudad del proveedor
 * @param zipCode - Código postal del proveedor
 */
data class Supplier(
    val id: Long? = null,
    val name: String = "",
    val contactName: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val country: String = "",
    val city: String = "",
    val zipCode: String = "",
    val isActive: Boolean = true
)


/**
 * MODEL - Para la lista de proveedores en Home
 *
 * @param id - Id del proveedor
 * @param name - Nombre del proveedor
 * @param contactName - Nombre de la persona de contacto
 * @param email - Email del proveedor
 * @param phone - Teléfono del proveedor
 */
data class SupplierHomeView(
    val id: Long,
    val name: String = "",
    val contactName: String = "",
    val email: String = "",
    val phone: String = ""
    //val isActive: Boolean = true
)


/**
 * MODEL - Para la lista de proveedores en un menú de selección
 *
 * @param id - Id del proveedor
 * @param name - Nombre del proveedor
 */
data class SupplierSelectionMenu(
    val id: Long,
    val name: String
)
