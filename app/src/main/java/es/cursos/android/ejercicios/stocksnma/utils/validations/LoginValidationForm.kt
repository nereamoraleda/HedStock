package es.cursos.android.ejercicios.stocksnma.utils.validations

data class LoginValidationForm(
    val usernameErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val credentialsErrorMessage: String? = null
)