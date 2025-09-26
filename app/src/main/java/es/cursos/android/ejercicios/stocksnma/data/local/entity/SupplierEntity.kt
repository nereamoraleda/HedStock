package es.cursos.android.ejercicios.stocksnma.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity Supplier - Tabla que representa a un proveedor de productos
 *
 * @property id Identificador único del proveedor
 * @property name Nombre del proveedor
 * @property contactName Nombre del contacto del proveedor (opcional)
 * @property email Correo electrónico del proveedor (opcional, si hay un número de teléfono)
 * @property phone Número de teléfono del proveedor (opcional, si hay un correo electrónico)
 * @property address Dirección del proveedor (opcional)
 *
 * Valores únicos:
 * email - El correo electrónico del proveedor debe ser único en la tabla.
 * phone - El número de teléfono del proveedor debe ser único en la tabla.
 */
@Entity(
    tableName = "supplier_table",
    indices = [Index(value = ["email", "phone"], unique = true)]
)
data class SupplierEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "contact_name") val contactName: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "address") val address: String? = null
)