package es.cursos.android.ejercicios.stocksnma.ui.screen.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.remote.api.AuthApi
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.ChangePasswordRequest
import es.cursos.android.ejercicios.stocksnma.data.remote.dto.LoginRequest
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
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

    val showChangePasswordDialog = MutableStateFlow(false)
    private fun onShowChangePasswordDialogChanged(value: Boolean) {
        showChangePasswordDialog.value = value
    }

    private val _userRole = MutableStateFlow(UserRoles.DESCONOCIDO)
    val userRole: StateFlow<UserRoles> = _userRole


    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginUiState.Loading
                val response = api.login(LoginRequest(username, password))

                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        _userRole.value = UserRoles.entries.find { authResponse.role == it.name }
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
                    val msg = response.errorBody()?.string() ?: "Login failed"
                    _loginState.value = LoginUiState.Error(msg)
                    Log.d("LoginViewModel", "Login failed, $msg")
                }

            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Error: ${Result.failure<Exception>(e)}")
                Log.d("LoginViewModel", "Login exception: ${e.message}")
            }
        }
    }


    fun changePassword(username: String, token: String, oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                //_loginState.value = LoginUiState.Loading
                val response = api.changePassword(token, ChangePasswordRequest(username, oldPassword, newPassword))
                Log.d("LoginViewModel", "Response: $response")
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
                }
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error("Error: ${Result.failure<Exception>(e)}")
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


