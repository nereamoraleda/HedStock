package es.cursos.android.ejercicios.stocksnma.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity ProductCatalog - Tabla que representa a un producto del catálogo
 */
@Entity(
    tableName = "product_catalog_table",
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
data class ProductCatalogEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "brand") val brand: String? = null,
    @ColumnInfo(name = "barcode") val barcode: String? = null,
    @ColumnInfo(name = "cost_price_default") val costPriceDefault: Double,
    @ColumnInfo(name = "price_default") val priceDefault: Double,
    @ColumnInfo(name = "id_category") val idCategory: String? = null,
    @ColumnInfo(name = "id_supplier") val idSupplier: String? = null,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)


/**
 * Entity Product - Tabla que representa a un producto de la tienda
 */
@Entity(
    tableName = "store_product_table",
    indices = [Index(value = ["id_store", "id_product_catalog"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = StoreEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_store"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = ProductCatalogEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_product_catalog"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StoreProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "id_store") val idStore: String,
    @ColumnInfo(name = "id_product_catalog") val idProductCatalog: String,

    @ColumnInfo(name = "stock") val stock: Int = 0,
    @ColumnInfo(name = "min_stock") val minStock: Int = 0,
    @ColumnInfo(name = "max_stock") val maxStock: Int? = null,

    @ColumnInfo(name = "cost_price") val costPrice: Double,
    @ColumnInfo(name = "price") val price: Double,

    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)