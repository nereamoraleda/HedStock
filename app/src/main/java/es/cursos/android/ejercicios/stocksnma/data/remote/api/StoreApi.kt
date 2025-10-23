package es.cursos.android.ejercicios.stocksnma.data.remote.api

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.store.StoreGeneralViewDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.store.StoreRequestDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.store.StoreResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApi {

    // -------------------- CRUD -------------------- //

    // Endpoint para modificar una tienda (mediante el ID)
    @PUT("api/stores/{id}")
    suspend fun updateStore(
        @Path("id") id: Long,
        @Body store: StoreRequestDto
    ): Response<StoreRequestDto>

    // Endpoint para obtener un listado de tiendas para la Vista General (Campos básicos, filtrados y ordenados)
    @GET("api/stores/all")
    suspend fun getStores(
        @Query("sortBy") sortBy: String = "name",
        @Query("direction") direction: String = "asc",
        @Query("active") active: Boolean? = null
    ): Response<List<StoreGeneralViewDto>>


    // Endpoint para obtener una tienda por su ID (Todos los campos)
    @GET("api/stores/{id}")
    suspend fun getStoreById(@Path("id") id: Long): Response<StoreResponseDto>



    // -------------------- BÚSQUEDA Y COMPROBACIÓN -------------------- //
    @GET("api/stores/search")
    suspend fun searchStores(
        @Query("query") query: String
    ): Response<List<StoreGeneralViewDto>>


    @GET("api/stores/check-name")
    suspend fun checkName(
        @Query("name") name: String
    ): Response<Boolean>

    @GET("api/stores/check-email")
    suspend fun checkEmail(
        @Query("email") email: String
    ): Response<Boolean>

    @GET("api/stores/check-phone")
    suspend fun checkPhone(
        @Query("phone") phone: String
    ): Response<Boolean>
}
