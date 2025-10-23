package es.cursos.android.ejercicios.stocksnma.data.remote.api

import es.cursos.android.ejercicios.stocksnma.data.remote.dto.UserDto
import es.cursos.android.ejercicios.stocksnma.domain.model.store.StoreSelection
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    // -------------------- CRUD -------------------- //

    // Endpoint para crear un nuevo usuario
    @POST("api/users")
    suspend fun createUser(@Body user: UserDto) : Response<UserDto>

    // Endpoint para actualizar un usuario
    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Long, @Body user: UserDto) : Response<UserDto>

    // Endpoint para eliminar un usuario
    @DELETE("api/users/{id}")
    suspend fun deleteUser(@Path("id") id: Long) : Response<UserDto>

    // Endpoint para obtener un usuario por su ID
    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long) : Response<UserDto>

    // Endpoint para obtener todos los usuarios (con parámetros de ordenación y filtrado)
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
    *  .message -> Mensaje del servidor
    *  .headers -> Cabecera HTTP
    *  .body() -> Cuerpo de la respuesta
    */

    // Endpoint para obtener todas las tiendas activas (ID y nombre)
    @GET("api/stores/selection")
    suspend fun getStoresForSelection() : Response<List<StoreSelection>> /*TODO - Modificar a StoreSelectinDto */



    // -------------------- Búsqueda y comprobación -------------------- //

    // Endpoint para buscar usuarios por nombre o nombre de usuario
    @GET("api/users/search")
    suspend fun searchUsers(@Query("query") query: String) : Response<List<UserDto>>

    // Endpoint para comprobar si el nombre de usuario ya existe
    @GET("api/users/check-username")
    suspend fun checkUsername(@Query("username") username: String): Response<Boolean>

    // Endpoint para comprobar si el email ya existe
    @GET("api/users/check-email")
    suspend fun checkEmail(@Query("email") email: String): Response<Boolean>

    // Endpoint para comprobar si el teléfono ya existe
    @GET("api/users/check-phone")
    suspend fun checkPhone(@Query("phone") phone: String): Response<Boolean>



    // -------------------- Contraseña -------------------- //

    // Endpoint para restablecer la contraseña de un usuario
    @POST("api/users/{id}/reset-password")
    suspend fun resetPassword(@Path("id") id: Long) : Response<Unit>
}