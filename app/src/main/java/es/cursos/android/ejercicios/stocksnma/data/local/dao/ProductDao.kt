package es.cursos.android.ejercicios.stocksnma.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import es.cursos.android.ejercicios.stocksnma.data.local.entity.ProductEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.relations.ProductWithSupplierAndCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // --- MÉTODOS CRUD (Create, Update, Delete) ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(productEntity: ProductEntity)

    @Update
    suspend fun updateProduct(productEntity: ProductEntity)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)



    // --------------------------------------------------------------------------------------------

    // --- MÉTODOS DE CONSULTAS ---

    // Obtener un producto por su ID
    @Query("SELECT * FROM product_table WHERE id = :id")
    fun getProduct(id: String): Flow<ProductEntity>

    // Obtener un producto por su código de barras (Escaneado)
    @Query("SELECT * FROM product_table WHERE barcode = :barcode LIMIT 1")
    suspend fun getProductByBarcode(barcode: String): ProductEntity?

    // Obtener si existe un producto con el mismo nombre
    @Query("SELECT EXISTS(SELECT 1 FROM product_table WHERE name = :name)")
    suspend fun existsProductWithName(name: String): Boolean

    // Buscar un producto por su nombre
    @Transaction
    @Query("SELECT * FROM product_table WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchProductByName(query: String): Flow<List<ProductWithSupplierAndCategory>>



    // --------------------------------------------------------------------------------------------

    // --- TIPOS DE VISTAS DE LA TABLA PRODUCTOS ---

    /**
     * Obtener todos los productos con sus proveedores (TABLE PRODUCTS - HOME)
     * Ordenados por nombre ascendente
     */
    @Transaction
    @Query("SELECT * FROM product_table ORDER BY name ASC")
    fun getAllProductsByNameAsc(): Flow<List<ProductWithSupplierAndCategory>>


    /**
     * Obtener todos los productos con sus proveedores (TABLE PRODUCTS - HOME)
     * Ordenados por nombre descendente
     */
    @Transaction
    @Query("SELECT * FROM product_table ORDER BY name DESC")
    fun getAllProductsByNameDesc(): Flow<List<ProductWithSupplierAndCategory>>


    /**
     * Obtener todos los productos con sus proveedores (TABLE PRODUCTS - HOME)
     * Ordenados por categoría
     */
    @Transaction
    @Query("SELECT * FROM product_table ORDER BY category")
    fun getProductsByCategory(): Flow<List<ProductWithSupplierAndCategory>>



    // --------------------------------------------------------------------------------------------

    // --- MÉTODOS DE ELIMINACIÓN ---

    // Eliminar productos seleccionados (CHECKBOX)
    @Query("DELETE FROM product_table WHERE id IN (:productsSelectedIds)")
    suspend fun deleteSelectedProducts(productsSelectedIds: List<String>)

    // Eliminar todos los productos de la tabla (para pruebas)
    @Query("DELETE FROM product_table")
    suspend fun deleteAllProducts()
}