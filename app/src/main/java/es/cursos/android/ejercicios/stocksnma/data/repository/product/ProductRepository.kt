package es.cursos.android.ejercicios.stocksnma.data.repository.product

import es.cursos.android.ejercicios.stocksnma.data.local.entity.ProductEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.relations.ProductWithSupplierAndCategory
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSortOptions
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    // Funciones CRUD (CREATE, UPDATE, DELETE)
    suspend fun insertProduct(productEntity: ProductEntity)
    suspend fun updateProduct(productEntity: ProductEntity)
    suspend fun deleteProduct(productEntity: ProductEntity)


    // Eliminar productos
    suspend fun deleteSelectedProducts(productIds: List<String>)
    suspend fun deleteAllProducts()


    // Buscar productos
    fun searchProductByName(query: String): Flow<List<ProductWithSupplierAndCategory>>


    // Obtener productos
    fun getProductById(id: String): Flow<ProductEntity?>
    suspend fun getProductByBarcode(barcode: String): ProductEntity?
    fun getAllProducts(orderType: ProductSortOptions): Flow<List<ProductWithSupplierAndCategory>>
    suspend fun existsProductWithName(name: String): Boolean
}