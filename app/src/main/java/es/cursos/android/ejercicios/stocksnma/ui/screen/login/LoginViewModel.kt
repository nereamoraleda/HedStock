package es.cursos.android.ejercicios.stocksnma.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.remote.api.AuthApi
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.ChangePasswordRequest
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.LoginRequest
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import es.cursos.android.ejercicios.stocksnma.utils.validations.LoginValidationForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: AuthApi,
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun onCredentialsChanged(username: String, password: String) {
        _username.value = username
        _password.value = password

        val validation = validateLoginForm(username, password)
        _isEntryValid.value = validation
    }

    private val _isEntryValid = MutableStateFlow(false)
    val isEntryValid: StateFlow<Boolean> = _isEntryValid

    private val _loginValidationState = MutableStateFlow(LoginValidationForm())
    val loginValidationState: StateFlow<LoginValidationForm> = _loginValidationState

    val showChangePasswordDialog = MutableStateFlow(false)
    private fun onShowChangePasswordDialogChanged(value: Boolean) {
        showChangePasswordDialog.value = value
    }

    private val _userRole = MutableStateFlow(UserRoles.DESCONOCIDO)
    val userRole: StateFlow<UserRoles> = _userRole


    private fun validateLoginForm(username: String, password: String): Boolean {
        // Actualizar el estado de validación
        _loginValidationState.value = LoginValidationForm(
            usernameErrorMessage = validateUsername(username),
            passwordErrorMessage = validatePassword(password)
        )

        // Devolver true si no hay errores de validación
        return listOf(
            validateUsername(username),
            validatePassword(password)
        ).all { it == null}
    }

    private fun validateUsername(usernameText: String): String? {
        return if (usernameText.isBlank()) { "El nombre de usuario no puede estar vacío" }
        else null
    }

    private fun validatePassword(passwordText: String): String? {
        val passwordRegex = Regex(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{8,}\$"
        )// 8 caracteres, 1 mayúscula, 1 minúscula, 1 número y 1 caracter especial

        return if (passwordText.isBlank()) { "La contraseña no puede estar vacía" }
        //else if (passwordText.length < 8) { "La contraseña debe tener al menos 8 caracteres" }
        //else if (!passwordText.any { it.isUpperCase() }) { "La contraseña debe tener al menos 1 letra mayúscula" }
        //else if (!passwordRegex.matches(passwordText)) { "La contraseña debe tener al menos 8 caracteres, 1 mayúscula, 1 minúscula, 1 número y 1 caracter especial" }
        else null
    }


    fun login(username: String, password: String) {
        viewModelScope.launch {
            //if (!validateForm(username, password)) {
            try {
                //_loginState.value = LoginUiState.Loading
                val response = api.login(LoginRequest(username, password))
                Log.d("LoginViewModel", "Response: $response")
                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        _userRole.value =
                            UserRoles.entries.find { authResponse.role == it.name }
                                ?: UserRoles.VENDEDOR


                        if (authResponse.mustChangePassword) {
                            _loginState.value = LoginUiState.Success(authResponse)
                            onShowChangePasswordDialogChanged(true)
                            Log.d("LoginViewModel", "Password must be changed")

                        } else {
                            _loginState.value = LoginUiState.Success(authResponse)
                            sessionManager.saveSession(
                                authResponse.token,
                                _userRole.value
                            )
                        }
                        //Log.d("LoginViewModel", "Login successful")
                    }

                } else {
                    _loginValidationState.value = LoginValidationForm(
                        credentialsErrorMessage = "Credenciales incorrectas"
                    )
                    val msg = response.errorBody()?.string() ?: "Login failed"
                    //_loginState.value = LoginUiState.Error(msg)
                    Log.d("LoginViewModel", "Login failed, $msg")
                }

            } catch (e: Exception) {
                //_loginState.value = LoginUiState.Error("Error: ${Result.failure<Exception>(e)}")
                _loginValidationState.value = LoginValidationForm(
                    credentialsErrorMessage = "Credenciales incorrectas"
                )
                Log.d("LoginViewModel", "Login exception: ${e.message}")
            }
        //}
        }
    }


    fun changePassword(token: String, username: String, currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                //_loginState.value = LoginUiState.Loading
                val response = api.changePassword(token, ChangePasswordRequest(username, currentPassword, newPassword))
                Log.d("LoginViewModel", "Response: $token, $username, $currentPassword, $newPassword, $response")
                onShowChangePasswordDialogChanged(false)
                if (response.isSuccessful) {
                    val loginResponse = api.login(LoginRequest(username, newPassword))
                    if (loginResponse.isSuccessful) {
                        Log.d("LoginViewModel", "Login goin to change password")
                        loginResponse.body()?.let { authResponse ->
                            _userRole.value =
                                UserRoles.entries.find { authResponse.role == it.name }
                                    ?: UserRoles.VENDEDOR
                            _loginState.value = LoginUiState.Success(authResponse)
                            Log.d("LoginViewModel", "Password changed successfully")
                        }
                    }
                } else {
                    val msg = response.errorBody()?.let { Log.d("CHANGE-PWD", "errorBody=${it.string()}") }
                    //_loginState.value = LoginUiState.Error(msg)
                    Log.d("LoginViewModel", "Login failed, $msg")
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Error: ${Result.failure<Exception>(e)}")
                Log.d("LoginViewModel", "Login exception: ${e.message}")
            }
        }
    }


//    fun login(username: String, password: String) {
//        viewModelScope.launch {
//            _loginState.value = LoginUiState.Loading
//            try {
//                //val response = RetrofitClient.authService.login(LoginCredentials(username, password))
//                val response = apiService.login(LoginCredentials(username, password))
//                if (response.isSuccessful) {
//                    response.body()?.let { authResponse ->
//                        _userRole.value = UserRoles.entries.find { authResponse.role == it.name }
//                            ?: UserRoles.VENDEDOR
//                        _loginState.value = LoginUiState.Success(_userRole.value)
//                        sessionManager.saveSession(
//                            authResponse.token,
//                            //authResponse.storeId,
//                            _userRole.value
//                        )
//                        //dataStoreManager.saveUserRole(_userRole.value)
//                    }
//
//                } else {
//                    _loginState.value = LoginUiState.Error("Credenciales incorrectas")
//                    Log.d("LoginViewModel", "Login failed, Credenciales incorrectas")
//                }
//            } catch (e: Exception) {
//                _loginState.value = LoginUiState.Error("Error: ${Result.failure<Exception>(e)}")
//                Log.d("LoginViewModel", "Login exception: ${e.message}")
//            }
//        }
//    }

    fun resetState() {
        _loginState.value = LoginUiState.Idle
    }

    fun logout() {
        viewModelScope.launch {
            _loginState.value = LoginUiState.Idle
            _userRole.value = UserRoles.DESCONOCIDO
            sessionManager.clearSession()
        }
    }
}


