package es.cursos.android.ejercicios.stocksnma.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {

    // --- MÉTODOS CRUD (Create, Update, Delete) ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSupplier(supplierEntity: SupplierEntity)

    @Update
    suspend fun updateSupplier(supplierEntity: SupplierEntity)

    @Delete
    suspend fun deleteSupplier(supplierEntity: SupplierEntity)



    // --------------------------------------------------------------------------------------------

    // --- MÉTODOS DE CONSULTAS ---

    // Obtener un proveedor por su ID
    @Query("SELECT * FROM supplier_table WHERE id = :id")
    fun getSupplier(id: String): Flow<SupplierEntity>

    // Buscar un proveedor por su nombre
    @Query("SELECT * FROM supplier_table WHERE name LIKE '%' || :query || '%'")
    fun searchSupplierByName(query: String): Flow<List<SupplierEntity>>

    // Obtener si existe un proveedor con el mismo nombre
    @Query("SELECT EXISTS(SELECT 1 FROM supplier_table WHERE name = :name)")
    suspend fun existsSupplierWithName(name: String): Boolean


    // --------------------------------------------------------------------------------------------

    // --- TIPOS DE VISTAS DE LA TABLA PROVEEDORES ---

    // Obtener todos los proveedores por nombre ascendente
    @Query("SELECT * FROM supplier_table ORDER BY name ASC")
    fun getAllSuppliersByNameAsc(): Flow<List<SupplierEntity>>

    // Obtener todos los proveedores por nombre descendente
    @Query("SELECT * FROM supplier_table ORDER BY name DESC")
    fun getAllSuppliersByNameDesc(): Flow<List<SupplierEntity>>



    // --------------------------------------------------------------------------------------------

    // --- MÉTODOS DE ELIMINACIÓN ---

    // Eliminar proveedores seleccionados (CHECKBOX)
    @Query("DELETE FROM supplier_table WHERE id IN (:suppliersSelectedIds)")
    suspend fun deleteSelectedSuppliers(suppliersSelectedIds: List<String>)

    // Eliminar todos los proveedores (para pruebas)
    @Query("DELETE FROM supplier_table")
    suspend fun deleteAllSuppliers()
}