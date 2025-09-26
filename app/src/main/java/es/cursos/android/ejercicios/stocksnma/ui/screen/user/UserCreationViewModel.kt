package es.cursos.android.ejercicios.stocksnma.ui.screen.user

import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.util.Patterns.PHONE
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.mapper.toUserDto
import es.cursos.android.ejercicios.stocksnma.data.remote.HedstockApiService
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.ui.state.CreationUiState
import es.cursos.android.ejercicios.stocksnma.utils.UserValidationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCreationViewModel @Inject constructor(
    private val apiService: HedstockApiService
) : ViewModel() {

    var userUiState by mutableStateOf(CreationUiState(User()))
        private set

    //private val _user = MutableStateFlow(User())
    //val user: StateFlow<User> = _user.asStateFlow()

    private val _storeOptions = MutableStateFlow<List<Store>>(emptyList())
    val storeOptions: StateFlow<List<Store>> = _storeOptions.asStateFlow()

    private val _validationState = MutableStateFlow(UserValidationState())
    val validationState: StateFlow<UserValidationState> = _validationState


    init {
        loadStoresList()
    }

    fun createUser() {
        val user = userUiState.item

        if ( !validateInput() ) return
            viewModelScope.launch {
                try {
                    // val userDto = user.toUserDto()
                    // val userDto = userUiState.item.toUserDto()
                    val usernameExists = apiService.checkUsername(user.username).body() ?: false
                    val emailExists = apiService.checkEmail(user.email).body() ?: false
                    val phoneExists = apiService.checkPhone(user.phone).body() ?: false

                    if (usernameExists) {
                        _validationState.value = _validationState.value.copy(usernameError = "El nombre de usuario ya existe")
                        return@launch
                    }

                    if (emailExists) {
                        _validationState.value = _validationState.value.copy(emailError = "El email ya existe")
                        return@launch
                    }

                    if (phoneExists) {
                        _validationState.value = _validationState.value.copy(phoneError = "El teléfono ya existe")
                        return@launch
                    }


                    val response = apiService.createUser(user.toUserDto())
                    if (response.isSuccessful) {
                        Log.d("USER-CREATION", "Usuario creado: ${response.body()}")
                        cleanUserUiState()

                    } else {
                        Log.e("USER-CREATION", "Respuesta API no exitosa: ${response.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("USER-CREATION", "Error: ${e.message}", e)
                }

        }
    }


    fun updateUiState(user: User) {
        userUiState = userUiState.copy(item = user)
        //Log.d("USER-CREATION", "Usuario actualizado: $user")

        val isValid = validateInput(user)
        userUiState = userUiState.copy(isEntryValid = isValid)
    }

    fun cleanUserUiState() {
        userUiState = CreationUiState(User())
        _validationState.value = UserValidationState()
    }

    private fun loadStoresList() {
        viewModelScope.launch {
            try {
                val response = apiService.getAllStores()
                if (response.isSuccessful) {
                    val stores = response.body() ?: emptyList()
                    _storeOptions.value = stores
                    Log.d("STORES", "Recibidos: $stores")
                } else {
                    Log.e("STORES", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("STORES", "Fallo: ${e.message}", e)
            }
        }
    }

    private fun validateInput(user: User = userUiState.item): Boolean {
        _validationState.value = UserValidationState(
            nameError = validateName(user.name),
            usernameError = validateUsername(user.username),
            passwordError = validatePassword(user.password),
            emailError = validateEmail(user.email),
            phoneError = validatePhone(user.phone),
            emailOrPhoneError = validateEmailOrPhone(user.email, user.phone),
            roleError = validateRole(user.role)
        )

        return listOf(
            validateName(user.name),
            validateUsername(user.username),
            validatePassword(user.password),
            validateEmail(user.email),
            validatePhone(user.phone),
            validateEmailOrPhone(user.email, user.phone),
            validateRole(user.role)
        ).all { it == null }
    }

    private fun validateName(nameText: String): String? {
        if (nameText.isBlank()) return "El empleado debe tener un nombre"
        return null
    }

    private fun validateUsername(usernameText: String) : String? {
        if (usernameText.isBlank()) return "El empleado debe tener un nombre de usuario"
        return null
    }

    private fun validatePassword(passwordText: String): String? {
        if (passwordText.isBlank()) return "El empleado debe tener una contraseña"
        return null
    }

    private fun validateEmail(emailText: String): String? {
        //val emailRegex = Regex("^\\S+@\\S+\\.\\S+\$")
        val emailValidator = EMAIL_ADDRESS.matcher(emailText).matches()
        if (emailText.isNotEmpty() && !emailValidator) return "Email no válido"
        return null
    }

    private fun validatePhone(phoneText: String): String? {
        //val phoneRegex = Regex("^\\d{9}\$")
        val phoneValidator = PHONE.matcher(phoneText).matches()
        if (phoneText.isNotEmpty() && !phoneValidator) return "Teléfono no válido"
        return null
    }

    private fun validateEmailOrPhone(emailText: String, phoneText: String): String? {
        if (emailText.isBlank() && phoneText.isBlank()) return "El empleado debe tener un email o teléfono"
        return null
    }

    private fun validateRole(roleText: String): String? {
        if (roleText.isBlank()) return "El empleado debe tener asignado un rol"
        return null
    }
}