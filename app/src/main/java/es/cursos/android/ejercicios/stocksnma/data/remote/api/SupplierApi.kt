package es.cursos.android.ejercicios.stocksnma.data.remote.api

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.supplier.SupplierDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.supplier.SupplierHomeViewDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SupplierApi {

    // -------------------- CRUD -------------------- //

    // Endpoint para crear un nuevo proveedor
    @POST("api/suppliers")
    suspend fun createSupplier(
        @Body supplier: SupplierDto
    ): Response<SupplierDto>


    // Endpoint para modificar un proveedor (pasando el ID y el objeto SupplierDto)
    @PUT("api/suppliers/{id}")
    suspend fun updateSupplier(
        @Path("id") id: Long,
        @Body supplier: SupplierDto
    ): Response<SupplierDto>


    // Endpoint para eliminar un proveedor (pasando el ID)
    @DELETE("api/suppliers/{id}")
    suspend fun deleteSupplier(@Path("id") id: Long)


    // Endpoint para obtener un proveedor (pasando su ID y obteniendo todos sus campos)
    @GET("api/suppliers/{id}")
    suspend fun getSupplierById(
        @Path("id") id: Long
    ): Response<SupplierDto>


    // Endpoint para obtener un listado de proveedores para la vista en Home (campos básicos, filtrados y ordenados)
    @GET("api/suppliers")
    suspend fun getSuppliers(
        @Query("sortBy") sortBy: String = "name",
        @Query("direction") direction: String = "asc"
    ): Response<List<SupplierHomeViewDto>>


//    @GET("api/suppliers/selection")
//    suspend fun getSuppliersForSelection(): Response<List<SupplierForSelectionDto>> // TODO - Mover a CatalogProducts



    // -------------------- BÚSQUEDA -------------------- //
    @GET("api/suppliers/search")
    suspend fun searchSuppliers(
        @Query("query") query: String
    ): Response<List<SupplierHomeViewDto>>



    // -------------------- COMPROBACIÓN -------------------- //
    @GET("api/suppliers/check-name")
    suspend fun checkName(
        @Query("name") name: String
    ): Response<Boolean>

    @GET("api/suppliers/check-email")
    suspend fun checkEmail(
        @Query("email") email: String
    ): Response<Boolean>

    @GET("api/suppliers/check-phone")
    suspend fun checkPhone(
        @Query("phone") phone: String
    ): Response<Boolean>
}