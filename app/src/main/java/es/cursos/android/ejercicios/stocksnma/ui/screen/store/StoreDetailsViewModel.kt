package es.cursos.android.ejercicios.stocksnma.ui.screen.store

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.local.datastore.AppDataStore
import es.cursos.android.ejercicios.stocksnma.data.mapper.toStore
import es.cursos.android.ejercicios.stocksnma.data.mapper.toStoreRequest
import es.cursos.android.ejercicios.stocksnma.data.remote.api.StoreApi
import es.cursos.android.ejercicios.stocksnma.domain.model.store.Store
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import es.cursos.android.ejercicios.stocksnma.utils.validations.StoreValidationForm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreDetailsViewModel @Inject constructor(
    private val storeApi: StoreApi,
    dataStore: AppDataStore
): ViewModel() {

    // -------------------- DATOS DE LA TIENDA -------------------- //
    private val _currentStore = MutableStateFlow(Store())

    private val _editableStore = MutableStateFlow(Store())
    val editableStore: StateFlow<Store> = _editableStore.asStateFlow()

    private val currentName = MutableStateFlow("")
    private val currentEmail = MutableStateFlow("")
    private val currentPhone = MutableStateFlow("")

    private val _validationState = MutableStateFlow(StoreValidationForm())
    val validationState: StateFlow<StoreValidationForm> = _validationState.asStateFlow()

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid.asStateFlow()


    // -------------------- ROL DEL USUARIO -------------------- //
    private val _userRole: Flow<UserRoles> = dataStore.userRole

    val hasPermission: StateFlow<Boolean> = _userRole
        .map { role -> role == UserRoles.ADMIN }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )


    // -------------------- FUNCIONES -------------------- //
    fun getStoreDetails(id: Long) {
        viewModelScope.launch {
            try {
                val response = storeApi.getStoreById(id)
                if (response.isSuccessful) {
                    val storeResponse = response.body()?.toStore() ?: Store()
                    _currentStore.value = storeResponse
                    _editableStore.value = storeResponse.copy()

                    currentName.value = _currentStore.value.name
                    currentEmail.value = _currentStore.value.email
                    currentPhone.value = _currentStore.value.phone

                    Log.i("DETAILS-STORE-GET-DATA", "Tienda: ${_currentStore.value}")
                }
            } catch (e: Exception) {
                Log.e("DETAILS-STORE-GET-DATA", "Error: ${e.message}")
            }
        }
    }


    fun saveChanges(onResult: (Boolean) -> Unit) {
        if (!validateStoreForm()) return
            viewModelScope.launch {
                try {

                    val updateName = _editableStore.value.name
                    if (currentName.value != updateName) {
                        if (storeApi.checkName(updateName).body() == true) {
                            _validationState.value = _validationState.value.copy(nameMessageError = "Ya existe una tienda con ese nombre")
                            return@launch
                        }
                    }

                    val updateEmail = _editableStore.value.email
                    if (currentEmail.value != updateEmail) {
                        if (storeApi.checkEmail(updateEmail).body() == true) {
                            _validationState.value = _validationState.value.copy(emailMessageError = "Ya existe una tienda con ese email")
                            return@launch
                        }
                    }

                    val updatePhone = _editableStore.value.phone
                    if (currentPhone.value != updatePhone) {
                        if (storeApi.checkPhone(updatePhone).body() == true) {
                            _validationState.value = _validationState.value.copy(phoneMessageError = "Ya existe una tienda con ese teléfono")
                            return@launch
                        }
                    }

                    // Actualización de la tienda (sin fallos en el formulario)
                    val storeRequest = _editableStore.value.toStoreRequest()
                    val response = storeApi.updateStore(_editableStore.value.id!!, storeRequest)
                    onResult(response.isSuccessful)

                    if (response.isSuccessful) {
                        _currentStore.value = _editableStore.value.copy()
                        _validationState.value = StoreValidationForm()
                        Log.i("DETAILS-STORE-SAVE", "Tienda actualizada: ${response.body()}")
                    } else {
                        Log.e("DETAILS-STORE-SAVE", "Error en la respuesta de la API: ${response.code()}")
                    }

                } catch (e: Exception) {
                    Log.e("DETAILS-STORE-SAVE", "Error: ${e.message}")
                }
        }
    }


    fun onFieldChange(field: StoreFields, value: String) {
        _editableStore.update {
            when (field) {
                StoreFields.NAME -> it.copy(name = value)
                StoreFields.EMAIL -> it.copy(email = value)
                StoreFields.PHONE -> it.copy(phone = value)
                StoreFields.ADDRESS -> it.copy(address = value)
                StoreFields.CITY -> it.copy(city = value)
                StoreFields.COUNTRY -> it.copy(country = value)
                StoreFields.POSTAL_CODE -> it.copy(postalCode = value)
                else -> it
            }
        }

        _isFormValid.value = validateStoreForm()
    }

    fun onFieldChange(field: StoreFields, value: Boolean) {
        _editableStore.update {
            when (field) {
                StoreFields.IS_ACTIVE -> it.copy(isActive = value)
                else -> it
            }
        }

        _isFormValid.value = validateStoreForm()
    }


    fun resetUi() {
        _editableStore.value = _currentStore.value.copy()
        _validationState.value = StoreValidationForm()
    }



    private fun validateStoreForm(store: Store = _editableStore.value): Boolean {
        _validationState.value = StoreValidationForm(
            nameMessageError = validateNameStore(store.name),
            emailMessageError = validateEmailStore(store.email),
            phoneMessageError = validatePhoneStore(store.phone),
            contactInformationErrorMessage = validateContactInformation(store.email, store.phone)
        )

        return listOf(
            validateNameStore(store.name),
            validateEmailStore(store.email),
            validatePhoneStore(store.phone),
            validateContactInformation(store.email, store.phone)
        ).all { it == null }
    }


    private fun validateNameStore(name: String): String? {
        if (name.isBlank()) return "La tienda debe tener un nombre"
        return null
    }

    private fun validateEmailStore(email: String): String? {
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return "Email no válido"
        return null

    }

    private fun validatePhoneStore(phone: String): String? {
        if (phone.isNotBlank() && !Patterns.PHONE.matcher(phone).matches())
            return "Teléfono no válido"
        return null
    }

    private fun validateContactInformation(email: String, phone: String): String? {
        if (email.isBlank() && phone.isBlank()) return "La tienda debe tener información de contacto"
        return null
    }
}


//data class StoreUiState(
//    val currentStore: Store? = null,
//    val editableStore: Store = Store(),
//    val isEditing: Boolean = false,
//    val isValid: Boolean = false
//)
