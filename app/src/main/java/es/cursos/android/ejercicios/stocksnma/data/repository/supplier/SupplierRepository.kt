package es.cursos.android.ejercicios.stocksnma.data.repository.supplier

import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierSortOptions
import kotlinx.coroutines.flow.Flow

interface SupplierRepository {

    // Funciones CRUD (CREATE, UPDATE, DELETE)
    suspend fun insertSupplier(supplierEntity: SupplierEntity)
    suspend fun updateSupplier(supplierEntity: SupplierEntity)
    suspend fun deleteSupplier(supplierEntity: SupplierEntity)


    // Eliminar proveedores
    suspend fun deleteSelectedSuppliers(supplierIds: List<String>)
    suspend fun deleteAllSuppliers()


    // Buscar proveedores
    fun searchSupplierByName(query: String): Flow<List<SupplierEntity>>


    // Obtener proveedores
    fun getSupplierById(id: String): Flow<SupplierEntity?>
    fun getAllSuppliers(orderType: SupplierSortOptions = SupplierSortOptions.NAME_ASC): Flow<List<SupplierEntity>>
    suspend fun existsSupplierWithName(name: String): Boolean

}