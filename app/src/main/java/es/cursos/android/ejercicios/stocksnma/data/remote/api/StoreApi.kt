package es.cursos.android.ejercicios.stocksnma.data.remote.api

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.StoreDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.StoreGeneralViewDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApi {

    // -------------------- CRUD -------------------- //

    // Endpoint para obtener un listado de tiendas para la Vista General (Campos básicos, filtrados y ordenados)
    @GET("api/stores/all")
    suspend fun getStores(
        @Query("sortBy") sortBy: String = "name",
        @Query("direction") direction: String = "asc",
        @Query("active") active: Boolean? = null
    ): Response<List<StoreGeneralViewDto>>


    // Endpoint para obtener una tienda por su ID (Todos los campos)
    @GET("api/stores/{id}")
    suspend fun getStoreById(@Path("id") id: Long): Response<StoreDto>



    // -------------------- BÚSQUEDA Y COMPROBACIÓN -------------------- //
    @GET("api/stores/search")
    suspend fun searchStores(
        @Query("query") query: String
    ): Response<List<StoreGeneralViewDto>>

}