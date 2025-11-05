package es.cursos.android.ejercicios.stocksnma.ui.screen.supplier

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.mapper.toSupplierDto
import es.cursos.android.ejercicios.stocksnma.data.remote.api.SupplierApi
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.state.CreateUiState
import es.cursos.android.ejercicios.stocksnma.utils.Constants
import es.cursos.android.ejercicios.stocksnma.utils.enums.fields.SupplierFields
import es.cursos.android.ejercicios.stocksnma.utils.validations.SupplierValidationForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierCreationViewModel @Inject constructor(
    private val api: SupplierApi,
    //private val supplierRepository: SupplierRepository
) : ViewModel() {

    var uiState by mutableStateOf(CreateUiState(Supplier()))
        private set

    private val _validationForm = MutableStateFlow(SupplierValidationForm())
    val validationForm: StateFlow<SupplierValidationForm> = _validationForm.asStateFlow()


    fun saveSupplier() {
        if (validateSupplierForm(uiState.newItem)) {
            viewModelScope.launch {
                try {
                    val errors = mutableMapOf<String, String?>()

                    if (api.checkName(uiState.newItem.name).body() == true)
                        errors["name"] = "Ya existe un proveedor con ese nombre"

                    if (api.checkEmail(uiState.newItem.email).body() == true)
                        errors["email"] = "Ya existe un proveedor con ese email"

                    if (api.checkPhone(uiState.newItem.phone).body() == true)
                        errors["phone"] = "Ya existe un proveedor con ese teléfono"

                    // Si hay errores, los mostramos todos y no seguimos
                    if (errors.isNotEmpty()) {
                        _validationForm.value = _validationForm.value.copy(
                            nameErrorMessage = errors["name"],
                            emailErrorMessage = errors["email"],
                            phoneErrorMessage = errors["phone"]
                        )
                        return@launch
                    }

                    val supplierDto = uiState.newItem.toSupplierDto()
                    val response = api.createSupplier(supplierDto)
                    if (response.isSuccessful) {
                        Log.d("SupplierCreationViewModel", "Proveedor creado correctamente")
                        resetUi()  // Limpiar los campos y generar nuevo ID
                    }

                } catch (e: Exception) { Log.e("SupplierCreationViewModel", "Error al crear el proveedor", e) }
            }
        }
    }


    fun resetUi() {
        uiState = uiState.copy(newItem = Supplier(), isFormValid = false)
        _validationForm.value = SupplierValidationForm()
    }


    fun onFieldChange(field: SupplierFields, value: String) {
        val supplierChanged = when (field) {
            SupplierFields.NAME -> uiState.newItem.copy(name = value)
            SupplierFields.CONTACT_NAME -> uiState.newItem.copy(contactName = value)
            SupplierFields.PHONE -> uiState.newItem.copy(phone = value)
            SupplierFields.EMAIL -> uiState.newItem.copy(email = value)
            SupplierFields.ADDRESS -> uiState.newItem.copy(address = value)
            SupplierFields.CITY -> uiState.newItem.copy(city = value)
            SupplierFields.COUNTRY -> uiState.newItem.copy(country = value)
            SupplierFields.ZIP_CODE -> uiState.newItem.copy(zipCode = value)
            else -> uiState.newItem
        }

        uiState = uiState.copy(
            newItem = supplierChanged,
            isFormValid = validateSupplierForm(supplierChanged)
        )
    }


    private fun validateSupplierForm(supplier: Supplier): Boolean {
        _validationForm.value = SupplierValidationForm(
            nameErrorMessage = validateNameSupplier(supplier.name),
            phoneErrorMessage = validatePhoneSupplier(supplier.phone),
            emailErrorMessage = validateEmailSupplier(supplier.email),
            contactInformationErrorMessage = validateContactInformation(supplier.email, supplier.phone),
            cityErrorMessage = validateCitySupplier(supplier.city),
            countryErrorMessage = validateCountrySupplier(supplier.country)
        )

        return listOf(
            validateNameSupplier(supplier.name),
            validatePhoneSupplier(supplier.phone),
            validateEmailSupplier(supplier.email),
            validateContactInformation(supplier.email, supplier.phone),
            validateCitySupplier(supplier.city),
            validateCountrySupplier(supplier.country)
        ).all { it == null }
    }


    private fun validateNameSupplier(name: String): String? {
        if (name.isBlank()) return Constants.MISSING_NAME_ERROR_MESSAGE //"El nombre del proveedor es obligatorio"
        return null
    }

    private fun validatePhoneSupplier(phone: String): String? {
        if (phone.isNotBlank() && !Patterns.PHONE.matcher(phone).matches())
            return Constants.INVALID_PHONE_ERROR_MESSAGE
        return null
    }

    private fun validateEmailSupplier(email: String): String? {
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return Constants.INVALID_EMAIL_ERROR_MESSAGE
        return null
    }

    private fun validateContactInformation(email: String, phone: String): String? {
        if (email.isBlank() && phone.isBlank()) return Constants.MISSING_CONTACT_ERROR_MESSAGE //"El proveedor debe tener información de contacto"
        return null
    }

    private fun validateCitySupplier(city: String): String? {
        if (city.isBlank()) return Constants.MISSING_CITY_ERROR_MESSAGE
        return null
    }

    private fun validateCountrySupplier(country: String): String? {
        if (country.isBlank()) return Constants.MISSING_COUNTRY_ERROR_MESSAGE
        return null
    }
}