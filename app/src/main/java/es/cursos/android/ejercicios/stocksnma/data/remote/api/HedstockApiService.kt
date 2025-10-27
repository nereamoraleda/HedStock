package es.cursos.android.ejercicios.stocksnma.data.remote.api

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.LoginRequest
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.LoginResponse
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.UserDto
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface HedstockApiService {
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest) : Response<LoginResponse>

    // --------------------------------------------------------

    @GET("api/stores")
    suspend fun getAllStores() : Response<List<Store>>

    // --------------------------------------------------------
}
