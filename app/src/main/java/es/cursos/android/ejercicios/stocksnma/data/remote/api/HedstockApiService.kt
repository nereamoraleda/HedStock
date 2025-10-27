package es.cursos.android.ejercicios.stocksnma.data.remote.api

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.LoginRequest
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.LoginResponse
import es.cursos.android.ejercicios.stocksnma.domain.model.store.Store
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface HedstockApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest) : Response<LoginResponse>

    // --------------------------------------------------------

    @GET("api/stores")
    suspend fun getAllStores() : Response<List<Store>>

    // --------------------------------------------------------
}
