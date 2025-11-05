package es.cursos.android.ejercicios.stocksnma.ui.screen.store

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.mapper.toStoreRequest
import es.cursos.android.ejercicios.stocksnma.data.remote.api.StoreApi
import es.cursos.android.ejercicios.stocksnma.domain.model.store.Store
import es.cursos.android.ejercicios.stocksnma.ui.state.CreateUiState
import es.cursos.android.ejercicios.stocksnma.utils.validations.StoreValidationForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreCreationViewModel @Inject constructor(
    private val api: StoreApi
): ViewModel() {

    // -------------------- STATE UI -------------------- //
    var uiState by mutableStateOf(CreateUiState(Store()))
        private set


    // -------------------- DATOS DE LA TIENDA -------------------- //
    private val _newStore = MutableStateFlow(Store())
    val newStore: StateFlow<Store> = _newStore.asStateFlow()

    private val _validations = MutableStateFlow(StoreValidationForm())
    val validations: StateFlow<StoreValidationForm> = _validations.asStateFlow()



    // -------------------- FUNCIONES DE CREACIÓN -------------------- //
    fun createStore() {
        if (validateStoreForm()) {  // Si la validación del formulario es exitosa
            viewModelScope.launch {
                try {
                    // Validación de campos únicos, antes de crear la nueva tienda
                    val errors = mutableMapOf<String, String?>()
                    val store = uiState.newItem

                    if (api.checkName(store.name).body() == true)
                        errors["name"] = "Ya existe una tienda con ese nombre"

                    if (api.checkEmail(store.email).body() == true)
                        errors["email"] = "Ya existe un proveedor con ese email"

                    if (api.checkPhone(store.phone).body() == true)
                        errors["phone"] = "Ya existe un proveedor con ese teléfono"

                    // Si hay errores, los mostramos todos y no seguimos
                    if (errors.isNotEmpty()) {
                        _validations.value = _validations.value.copy(
                            nameMessageError = errors["name"],
                            emailMessageError = errors["email"],
                            phoneMessageError = errors["phone"]
                        )
                        return@launch
                    }

                    // Si no hay errores, creamos la tienda
                    val storeRequest = _newStore.value.toStoreRequest()  // Convertir el objeto Store (obtenido del formulario CREATION) a StoreRequest
                    val response = api.createStore(storeRequest)         // Crear la tienda en la API

                    if (response.isSuccessful) {
                        Log.i("STORE-CREATION", "Tienda creada: ${response.body()}")
                        resetUi()
                    }

                } catch (e: Exception) {
                    Log.e("STORE-CREATION", "Error al crear la tienda: ${e.message}")
                }
            }
        }
    }


    fun resetUi() {
        _newStore.value = Store()
        _validations.value = StoreValidationForm()
    }



    // -------------------- FUNCIONES DE EDICIÓN -------------------- //
    fun onFieldChange(field: StoreFields, value: String) {
        _newStore.update {
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

        uiState = uiState.copy(isFormValid = validateStoreForm())
    }



    // -------------------- FUNCIONES DE VALIDACIÓN -------------------- //
    private fun validateStoreForm(store: Store = _newStore.value): Boolean {
        _validations.value = StoreValidationForm(
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