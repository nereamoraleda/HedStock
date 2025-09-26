package es.cursos.android.ejercicios.stocksnma.data.remote

import es.cursos.android.ejercicios.stocksnma.data.dto.UserDto
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
    suspend fun login(@Body request: LoginCredentials) : Response<LoginResponse>

    // --------------------------------------------------------

    @GET("api/stores")
    suspend fun getAllStores() : Response<List<Store>>

    @GET("api/stores/summary")
    suspend fun getStoresSummary() : Response<List<Store>>

    // --------------------------------------------------------

    // Obtener los usuarios según los parámetros de ordenación y filtrado
    @GET("api/users")
    suspend fun getUsers(
        @Query("sortBy") sortBy : String = "name",
        @Query("direction") direction : String = "asc",
        @Query("active") active : Boolean? = null
        //@Query() page : Int = 0,
        //@Query() size : Int = 10
    ) : Response<List<UserDto>>   // Al no añadir Response, solo se obtiene una Lista (no hay que acceder al body)
    /*
                         * Con Response, se incluye toda la respuesta HTTP
                         * .code() -> Código HTTP obtenido (200, 404, 500)
                         * .message -> Mensaje del servidor
                         * .headers -> Cabecera HTTP
                         * .body() -> Cuerpo de la respuesta
                         */


    // Obtener usuario por su ID
    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long) : Response<UserDto>


    // Crear un usuario
    @POST("api/users")
    suspend fun createUser(@Body user: UserDto) : Response<UserDto>


    // Actualizar un usuario
    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: UserDto) : Response<UserDto>


    // Eliminar un usuario
    @DELETE("api/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long) : Response<UserDto>


    @POST("api/users/{id}/reset-password")
    suspend fun resetPassword(@Path("id") id: Long) : Response<Unit>


    @POST("api/users/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ) : Response<Unit>


    // Buscar usuarios por nombre o nombre de usuario
    @GET("api/users/search")
    suspend fun searchUsers(@Query("query") query: String) : Response<List<UserDto>>


    // Comprobar si el nombre de usuario ya existe
    @GET("api/users/check-username")
    suspend fun checkUsername(@Query("username") username: String): Response<Boolean>

    // Comprobar si el email ya existe
    @GET("api/users/check-email")
    suspend fun checkEmail(@Query("email") email: String): Response<Boolean>

    // Comprobar si el teléfono ya existe
    @GET("api/users/check-phone")
    suspend fun checkPhone(@Query("phone") phone: String): Response<Boolean>
}





data class LoginCredentials(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val role: String,
    val mustChangePassword: Boolean,
    //val storeId: Long
)

data class ChangePasswordRequest(
    val username: String,
    val oldPassword: String,
    val newPassword: String
)
