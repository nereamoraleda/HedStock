package es.cursos.android.ejercicios.stocksnma.ui.screen.login

import es.cursos.android.ejercicios.stocksnma.data.remote.LoginResponse

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val auth: LoginResponse) : LoginUiState()
    data class Error(val messageError: String) : LoginUiState()
}