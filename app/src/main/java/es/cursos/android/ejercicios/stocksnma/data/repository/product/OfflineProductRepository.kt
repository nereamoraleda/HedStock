package es.cursos.android.ejercicios.stocksnma.data.repository.product

import es.cursos.android.ejercicios.stocksnma.data.local.dao.ProductDao
import es.cursos.android.ejercicios.stocksnma.data.local.entity.ProductEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.relations.ProductWithSupplierAndCategory
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSortOptions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineProductRepository @Inject constructor(
    private val productDao: ProductDao
): ProductRepository {

    // Funciones CRUD (CREATE, UPDATE, DELETE)
    override suspend fun insertProduct(productEntity: ProductEntity) = productDao.insertProduct(productEntity)
    override suspend fun updateProduct(productEntity: ProductEntity) = productDao.updateProduct(productEntity)
    override suspend fun deleteProduct(productEntity: ProductEntity) = productDao.deleteProduct(productEntity)


    // Eliminar productos
    override suspend fun deleteSelectedProducts(productIds: List<String>) = productDao.deleteSelectedProducts(productIds)
    override suspend fun deleteAllProducts() = productDao.deleteAllProducts()


    // Buscar productos
    override fun searchProductByName(query: String): Flow<List<ProductWithSupplierAndCategory>> = productDao.searchProductByName(query)


    // Obtener productos
    override fun getProductById(id: String): Flow<ProductEntity?> = productDao.getProduct(id)
    override suspend fun existsProductWithName(name: String): Boolean = productDao.existsProductWithName(name)
    override suspend fun getProductByBarcode(barcode: String): ProductEntity? = productDao.getProductByBarcode(barcode)
    override fun getAllProducts(orderType: ProductSortOptions): Flow<List<ProductWithSupplierAndCategory>> {
        return when (orderType) {
            ProductSortOptions.NAME_ASC -> productDao.getAllProductsByNameAsc()
            ProductSortOptions.NAME_DESC -> productDao.getAllProductsByNameDesc()
            ProductSortOptions.CATEGORY -> productDao.getProductsByCategory()
        }
    }
}