package es.cursos.android.ejercicios.stocksnma.data.remote.api

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.supplier.SupplierDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SupplierApi {

    @GET("api/suppliers")
    suspend fun getAllSuppliers(): List<SupplierDto>

    @GET("api/suppliers/{id}")
    suspend fun getSupplierById(@Path("id") id: Long): SupplierDto
}