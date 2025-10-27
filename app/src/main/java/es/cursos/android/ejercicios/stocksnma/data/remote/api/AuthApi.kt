package es.cursos.android.ejercicios.stocksnma.data.remote.api

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.ChangePasswordRequest
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.LoginRequest
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    // Endpoint para iniciar sesión
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest) : Response<LoginResponse>

    // Endpoint para cambiar la contraseña
    @POST("api/users/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ) : Response<Unit>
}