package es.cursos.android.ejercicios.stocksnma.data.remote.api

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.StoreDto
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.StoreGeneralViewDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApi {

    @GET("api/stores/all")
    suspend fun getStores(
        @Query("active") active: Boolean? = null
    ): Response<List<StoreGeneralViewDto>>


    @GET("api/stores/{id}")
    suspend fun getStoreById(@Path("id") id: Long): Response<StoreDto>
}