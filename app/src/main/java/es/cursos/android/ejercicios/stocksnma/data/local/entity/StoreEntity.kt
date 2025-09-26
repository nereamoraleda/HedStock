package es.cursos.android.ejercicios.stocksnma.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity Store - Tabla que representa a una tienda
 *
 * @property id Identificador único de la tienda
 * @property name Nombre de la tienda
 * @property address Dirección de la tienda
 * @property phone Número de teléfono de la tienda
 * @property email Correo electrónico de la tienda
 * @property isActive Indica si la tienda está activa o inactiva en el sistema
 * @property createdAt Fecha y hora de creación de la tienda en el sistema
 *
 * Valores únicos:
 * email - El correo electrónico de la tienda debe ser único en la tabla.
 * phone - El número de teléfono de la tienda debe ser único en la tabla.
 */
@Entity(
    tableName = "store_table",
    indices = [Index(value = ["email", "phone"], unique = true)]
)
data class StoreEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
