package es.cursos.android.ejercicios.stocksnma.ui.screen.user

import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.util.Patterns.PHONE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.mapper.toUser
import es.cursos.android.ejercicios.stocksnma.data.mapper.toUserDto
import es.cursos.android.ejercicios.stocksnma.data.remote.api.UserApi
import es.cursos.android.ejercicios.stocksnma.domain.model.store.StoreSelection
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.validations.UserValidationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val api: UserApi
) : ViewModel() {

    init {
        loadStoresList()
    }

    private val _uiState = MutableStateFlow<DetailsUiState<User>>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState<User>> = _uiState.asStateFlow()
    private val _userOriginal = MutableStateFlow(User())

    private val _userEditable = MutableStateFlow(User())
    val userEditable: StateFlow<User> = _userEditable.asStateFlow()

    private val _resetResult = MutableStateFlow(false)
    val resetResult: StateFlow<Boolean> = _resetResult.asStateFlow()

    private val emailNotChanged = MutableStateFlow("")
    private val phoneNotChanged = MutableStateFlow("")

    private val _storeOptions = MutableStateFlow<List<StoreSelection>>(emptyList())
    val storeOptions: StateFlow<List<StoreSelection>> = _storeOptions.asStateFlow()

    private val _validationState = MutableStateFlow(UserValidationState())
    val validationState: StateFlow<UserValidationState> = _validationState

    fun loadUserDetails(id: Long) {
        viewModelScope.launch {
            try {
                val response = api.getUserById(id)
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        //Log.d("GET-USER-DETAILS-BEFORE", "Información del usuario: ${response.body()}")
                        val user = response.body()?.toUser() ?: User()
                        //Log.d("GET-USER-DETAILS-AFTER", "Información del usuario: $user")

                        emailNotChanged.value = user.email
                        phoneNotChanged.value = user.phone

                        _userOriginal.value = user
                        _userEditable.value = user
                        _uiState.value = DetailsUiState.Success(
                            item = user,
                            isEntryValid = true
                        )

                        Log.d("GET-USER-DETAILS", "Información del usuario obtenida correctamente ${_userEditable.value}")

                    } else {
                        _uiState.value = DetailsUiState.NotFound
                        Log.d("GET-USER-DETAILS", "Información del usuario no encontrada")
                    }
                } else {
                    _uiState.value = DetailsUiState.Error(response.message())
                    Log.e("GET-USER-DETAILS", "Error en la respuesta de la API al obtener la información del usuario")
                }
            } catch (e: Exception) {
                _uiState.value = DetailsUiState.Error(e.message ?: "Error desconocido")
                Log.e("GET-USER-DETAILS", "Error al obtener la información del usuario: ${e.message}")
            }
        }
    }


    fun saveUserUpdates(onResult: (Boolean) -> Unit) {
        val user = _userEditable.value

        if ( !validateInput() ) return
        viewModelScope.launch {
            try {
                val newEmail = user.email
                val newPhone = user.phone

                // Comprobación de duplicados
                if (emailNotChanged.value != newEmail) {
                    if (api.checkEmail(newEmail).body() == true) {
                        _validationState.value = _validationState.value.copy(emailErrorMessage = "El email ya existe")
                        return@launch
                    }
                }

                if (phoneNotChanged.value != newPhone) {
                    if (api.checkPhone(newPhone).body() == true) {
                        _validationState.value = _validationState.value.copy(phoneErrorMessage = "El teléfono ya existe")
                        return@launch
                    }
                }

                // Actualización de Usuario
                val userDto = _userEditable.value.toUserDto()
                val response = api.updateUser(userDto.id!!, userDto)
                onResult(response.isSuccessful)

                if (response.isSuccessful) {
                    _userOriginal.value = _userEditable.value
                    _uiState.value = DetailsUiState.Success(_userEditable.value, true)
                    _validationState.value = UserValidationState()
                    Log.d("UPDATE-USER", "Usuario actualizado: ${response.body()}")

                } else {
                    Log.e("UPDATE-USER", "Respuesta API no exitosa: ${response?.code()}")
                }

            } catch (e: Exception) {
                Log.e("UPDATE-USER", "Error: ${e.message}", e)
            }
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            try {
                val response = api.resetPassword(_userOriginal.value.id!!)
                if (response.isSuccessful) {
                    _resetResult.value = true
                    Log.d("RESET-PASSWORD", "Contraseña reseteada correctamente: ${response.body()}")
                } else {
                    _resetResult.value = false
                    Log.e("RESET-PASSWORD", "Error en reset: ${response.code()} - ${response.message()}")
                }

            } catch (e: Exception) {
                Log.e("RESET-PASSWORD", "Error: ${e.message}", e)
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            try {
                val response = api.deleteUser(_userOriginal.value.id!!)
                if (response.isSuccessful) {
                    Log.d("DELETE-USER", "Usuario eliminado: ${response.body()}")
                } else {
                    Log.e("DELETE-USER", "Respuesta API no exitosa: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("DELETE-USER", "Error: ${e.message}", e)
            }
        }
    }

    fun resetUiState() {
        _userEditable.value = _userOriginal.value
        _validationState.value = UserValidationState()
    }

    private fun validateInput(user: User = _userEditable.value): Boolean {
        _validationState.value = UserValidationState(
            nameErrorMessage = validateName(user.name),
            emailErrorMessage = validateEmail(user.email),
            phoneErrorMessage = validatePhone(user.phone),
            contactInformationErrorMessage = validateEmailOrPhone(user.email, user.phone),
            roleErrorMessage = validateRole(user.role)
        )

        return listOf(
            validateName(user.name),
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


    fun updateUserEditable(field: String, value: Any) {
        _userEditable.update { current ->
            when (field) {
                "name" -> current.copy(name = value as String)
                //"username" -> current.copy(username = value as String)
                "email" -> current.copy(email = value as String)
                "phone" -> current.copy(phone = value as String)
                "role" -> current.copy(role = value as String)
                "storeId" -> current.copy(storeId = value as Long)
                "storeName" -> current.copy(storeName = value as String)
                "isActive" -> current.copy(isActive = value as Boolean)
                else -> current
            }
        }

        // Validar después del cambio
        viewModelScope.launch {
            val validation = validateInput(_userEditable.value)
            Log.d("VALIDATION - DETAILS USER", "Validación: $validation")
            //Log.d("VALIDATION - DETAILS USER", "Error/es: ${_validationState.value}")

            // Actualizar isEntryValid en el UiState
            _uiState.update { current ->
                if (current is DetailsUiState.Success) {
                    current.copy(isEntryValid = validation)
                } else current
            }
        }
    }


    private fun loadStoresList() {
        viewModelScope.launch {
            try {
                val response = api.getStoresForSelection()
                if (response.isSuccessful) {
                    val stores = response.body() ?: emptyList()
                    _storeOptions.value = stores
                    Log.d("GET STORES - DETAILS USER", "Recibidos: $stores")
                } else {
                    Log.e("GET STORES - DETAILS USER", "Error en la respuesta de la API: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("GET STORES - DETAILS USER", "Fallo: ${e.message}", e)
            }
        }
    }
}