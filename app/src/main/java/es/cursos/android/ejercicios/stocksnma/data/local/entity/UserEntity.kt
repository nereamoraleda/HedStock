package es.cursos.android.ejercicios.stocksnma.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity User - Tabla que representa a un usuario/empleado de la tienda o empresa
 *
 * @property id Identificador único del usuario
 * @property name Nombre y apellidos del usuario
 * @property email Correo electrónico del usuario (opcional)
 * @property phone Número de teléfono del usuario (opcional)
 * @property password Contraseña para acceder al sistema del usuario
 * @property role Rol del usuario en la tienda o empresa
 * @property storeId Identificador de la tienda o empresa a la que pertenece el usuario
 * @property photo Foto de perfil del usuario (opcional)
 * @property isActive Indica si el usuario está activo o inactivo en el sistema
 * @property createdAt Fecha y hora de creación del usuario en el sistema
 *
 * Valores únicos:
 * email - El correo electrónico del usuario debe ser único en la tabla.
 * phone - El número de teléfono del usuario debe ser único en la tabla.
 */
@Entity(
    tableName = "user_table",
    indices = [Index(value = ["email", "phone"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = ["id"],
            childColumns = ["store_id"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "role") val role: String,
    @ColumnInfo(name = "store_id") val storeId: String,
    @ColumnInfo(name = "photo") val photo: Int? = null,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long? = null,
    @ColumnInfo(name = "last_login") val lastLogin: Long? = null
)