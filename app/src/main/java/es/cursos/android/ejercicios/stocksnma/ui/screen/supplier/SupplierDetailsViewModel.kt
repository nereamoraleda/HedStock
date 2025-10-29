package es.cursos.android.ejercicios.stocksnma.ui.screen.supplier

import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.mapper.toSupplier
import es.cursos.android.ejercicios.stocksnma.data.mapper.toSupplierEntity
import es.cursos.android.ejercicios.stocksnma.data.remote.api.SupplierApi
import es.cursos.android.ejercicios.stocksnma.data.repository.supplier.SupplierRepository
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierFields
import es.cursos.android.ejercicios.stocksnma.utils.validations.SupplierValidationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SupplierDetailsViewModel @Inject constructor(
    private val api: SupplierApi,
    private val supplierRepository: SupplierRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState<Supplier>>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState<Supplier>> = _uiState.asStateFlow()

    private val _validationsSupplierState = MutableStateFlow(SupplierValidationState())
    val validationsSupplierState: StateFlow<SupplierValidationState> = _validationsSupplierState.asStateFlow()


    fun getSupplierById(id: Long) {
        viewModelScope.launch {
            //supplierRepository.getSupplierById(id)
            val response = api.getSupplierById(id)
                //.catch { error -> _uiState.value = DetailsUiState.Error(error.message ?: "Error desconocido") }
                //.collect { supplier ->
            val supplierModel = response.toSupplier()

            _uiState.value = DetailsUiState.Success(
                currentItem = supplierModel,
                editableItem = supplierModel.copy(),
                //isEditing = false
            )

        //}
        }
    }

    fun resetUi() {
        val state = _uiState.value
        if (state is DetailsUiState.Success) {
            _uiState.value = (_uiState.value as DetailsUiState.Success).copy(
                editableItem = state.currentItem,
                isEditing = false
            )
        }
        _validationsSupplierState.value = SupplierValidationState()
    }



    fun saveSupplier() {
        val state = _uiState.value
        if (state is DetailsUiState.Success && state.isFormValid) {
            viewModelScope.launch {
                try {
                    supplierRepository.updateSupplier(state.editableItem.toSupplier())
                    _uiState.value = state.copy(
                        currentItem = state.editableItem.copy(),
                        //isEditing = false
                    )
                } catch (e: Exception) {
                    _uiState.value = DetailsUiState.Error(
                        e.message ?: "Error al actualizar el proveedor"
                    )
                }
            }
        }
    }


    fun deleteSupplier() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state is DetailsUiState.Success) {
                val supplierEntity = state.currentItem.toSupplierEntity()
                try { supplierRepository.deleteSupplier(supplierEntity) }
                catch (e: Exception) { Log.e("SupplierDetailsViewModel", "Error al eliminar el proveedor", e) }
            }
        }
    }


    fun onFieldChange(field: SupplierFields, value: String) {
        val state = _uiState.value
        if (state is DetailsUiState.Success) {
            val updated = when (field) {
                SupplierFields.NAME -> state.editableItem.copy(name = value)
                SupplierFields.CONTACT_NAME -> state.editableItem.copy(contactName = value)
                SupplierFields.EMAIL -> state.editableItem.copy(email = value)
                SupplierFields.PHONE -> state.editableItem.copy(phone = value)
                SupplierFields.ADDRESS -> state.editableItem.copy(address = value)
                SupplierFields.CITY -> state.editableItem.copy(city = value)
                SupplierFields.COUNTRY -> state.editableItem.copy(country = value)
                SupplierFields.ZIP_CODE -> state.editableItem.copy(zipCode = value)
                else -> state.editableItem
            }

            viewModelScope.launch {
                _uiState.value = state.copy(
                    editableItem = updated,
                    isFormValid = validateSupplier(updated)
                )
            }
        }
    }


    fun onFieldChange(field: SupplierFields, value: Boolean) {
        val state = _uiState.value
        if (state is DetailsUiState.Success) {
            val updated = when (field) {
                SupplierFields.IS_ACTIVE -> state.editableItem.copy(isActive = value)
                else -> state.editableItem
            }

            viewModelScope.launch {
                _uiState.value = state.copy(
                    editableItem = updated,
                    isFormValid = validateSupplier(updated)
                )
            }
        }
    }


    fun toggleEdit() {
        val state = _uiState.value
        if (state is DetailsUiState.Success) {
            _uiState.value = state.copy(
                isEditing = !state.isEditing!!,
                editableItem = if (state.isEditing) state.currentItem.copy()  // Cancelar edición -> Restaurar original
                else state.editableItem                                       // Entrar a edición -> Dejar igual
            )
            _validationsSupplierState.value = SupplierValidationState()
        }
    }


    private suspend fun validateSupplier(supplier: Supplier): Boolean {
        val state = _uiState.value
        if (state is DetailsUiState.Success) {
            val errors = SupplierValidationState(
                nameErrorMessage = validateNameSupplier(supplier.name, state.currentItem.name),
                phoneErrorMessage = validatePhoneSupplier(supplier.phone),
                emailErrorMessage =
                if (supplier.phone.isBlank() && supplier.email.isBlank()) {
                    "Debe especificar un número de teléfono o un email"
                } else validateEmailSupplier(supplier.email)
            )


            _validationsSupplierState.value = errors

            return errors.run {
                nameErrorMessage == null && phoneErrorMessage == null && emailErrorMessage == null
            }
        }
        return false
    }


    private suspend fun validateNameSupplier(name: String, initialName: String): String? {
        if (name.isBlank()) return "El nombre del proveedor es obligatorio"
        else if (name != initialName && supplierRepository.existsSupplierWithName(name)) return "Ya existe un proveedor con ese nombre"
        return null
    }

    private fun validatePhoneSupplier(phone: String): String? {
        if (phone.isNotBlank()) {
            return if (!PhoneNumberUtils.isGlobalPhoneNumber(phone)) { "El número de teléfono no es válido" }
            else null
        }
        return null
    }

    private fun validateEmailSupplier(email: String): String? {
        if (email.isNotBlank()) {
            return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { "El email no es válido" }
            else null
        }
        return null
    }
}