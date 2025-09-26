package es.cursos.android.ejercicios.stocksnma.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


/**
 * ENTITY - Tabla de productos
 */
@Entity(
    tableName = "product_table",
    foreignKeys = [
        ForeignKey(
            entity = SupplierEntity::class,
            parentColumns = ["id"],
            childColumns = ["supplier"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE,
        ),

        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE,
        )
    ]
)
data class ProductEntity(
    @PrimaryKey val id: String,

    // Información
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "brand") val brand: String? = null,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "barcode") val barcode: String? = null,
    @ColumnInfo(name = "image") val image: String? = null,

    // Precios
    @ColumnInfo(name = "cost_price") val costPrice: Double,
    @ColumnInfo(name = "price") val price: Double,

    // Inventario
    @ColumnInfo(name = "stock") val stock: Int,
    @ColumnInfo(name = "min_stock") val minStock: Int,
    @ColumnInfo(name = "max_stock") val maxStock: Int? = null,

    // Relaciones
    @ColumnInfo(name = "supplier") val supplierId: String? = null,
    @ColumnInfo(name = "category") val categoryId: Int? = null,

    // Estado
    @ColumnInfo(name = "is_active") val isActive: Boolean = true
)

//@ColumnInfo(name = "id_discount") val discount: String/Int? = null,
//@ColumnInfo(name = "sku") val sku: String? = null, referencia del producto en la empresa