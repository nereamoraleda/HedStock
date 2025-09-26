package es.cursos.android.ejercicios.stocksnma.data.repository.supplier

import es.cursos.android.ejercicios.stocksnma.data.local.dao.SupplierDao
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierSortOptions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineSupplierRepository @Inject constructor(
    private val supplierDao: SupplierDao
): SupplierRepository {

    // Funciones CRUD (CREATE, UPDATE, DELETE)
    override suspend fun insertSupplier(supplierEntity: SupplierEntity) = supplierDao.insertSupplier(supplierEntity)
    override suspend fun updateSupplier(supplierEntity: SupplierEntity) = supplierDao.updateSupplier(supplierEntity)
    override suspend fun deleteSupplier(supplierEntity: SupplierEntity) = supplierDao.deleteSupplier(supplierEntity)


    // Eliminar proveedores
    override suspend fun deleteSelectedSuppliers(supplierIds: List<String>) = supplierDao.deleteSelectedSuppliers(supplierIds)
    override suspend fun deleteAllSuppliers() = supplierDao.deleteAllSuppliers()


    // Buscar proveedores
    override fun searchSupplierByName(query: String): Flow<List<SupplierEntity>> = supplierDao.searchSupplierByName(query)
    override suspend fun existsSupplierWithName(name: String): Boolean = supplierDao.existsSupplierWithName(name)


    // Obtener proveedores
    override fun getSupplierById(id: String): Flow<SupplierEntity?> = supplierDao.getSupplier(id)
    override fun getAllSuppliers(orderType: SupplierSortOptions): Flow<List<SupplierEntity>> {
        return when (orderType) {
            SupplierSortOptions.NAME_ASC -> supplierDao.getAllSuppliersByNameAsc()
            SupplierSortOptions.NAME_DESC -> supplierDao.getAllSuppliersByNameDesc()
        }
    }
}