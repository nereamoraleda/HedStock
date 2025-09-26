package es.cursos.android.ejercicios.stocksnma.data.local.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.ProductEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity


/**
 * DATA CLASS - Relaciones de la tabla product_table
 *
 * @param product - Objeto de ProductEntity
 * @param supplier - Objeto de SupplierEntity (opcional)
 * @param category - Objeto de CategoryEntity (opcional)
 *
 * Con Embedded, inserta los datos de una tabla dentro de otra,
 * en este caso, los datos del proveedor y la categoría dentro de la tabla de producto
 */
data class ProductWithSupplierAndCategory(
    @Embedded val product: ProductEntity,

    @Relation(
        parentColumn = "supplier",  // Clave foránea en la tabla de productos
        entityColumn = "id"            // Clave primaria en la tabla de proveedores
    )
    val supplier: SupplierEntity?,


    @Relation(
        parentColumn = "category",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)
