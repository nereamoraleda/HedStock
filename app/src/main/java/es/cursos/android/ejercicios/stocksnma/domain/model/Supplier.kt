package es.cursos.android.ejercicios.stocksnma.domain.model

import java.util.UUID


/**
 * DATA CLASS - Supplier
 * Clase intermedia con SupplierEntity, la BD y la UI (conversiones)
 *
 * @param id Identificador único del proveedor
 * @param name Nombre del proveedor
 * @param contactName Nombre del contacto
 * @param phone Teléfono del contacto
 * @param email Email del contacto
 * @param address Dirección del proveedor
 * @param country País del proveedor
 * @param city Ciudad del proveedor
 * @param zipCode Código postal del proveedor
 */
data class Supplier(
    val id: String = UUID.randomUUID().toString(),
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
