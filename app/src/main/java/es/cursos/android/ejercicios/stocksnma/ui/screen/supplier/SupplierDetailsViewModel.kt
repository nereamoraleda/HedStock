package es.cursos.android.ejercicios.stocksnma.ui.screen.supplier

import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.data.repository.supplier.SupplierRepository
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.SupplierValidationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SupplierDetailsViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState<SupplierEntity>>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState<SupplierEntity>> = _uiState.asStateFlow()

    private val _tempSupplier = MutableStateFlow<Supplier>(Supplier())
    val tempSupplier: StateFlow<Supplier> = _tempSupplier.asStateFlow()

    private val _validationsSupplierState = MutableStateFlow<SupplierValidationState>(SupplierValidationState())
    val validationsSupplierState: StateFlow<SupplierValidationState> = _validationsSupplierState.asStateFlow()

    private val _initialSupplierName = MutableStateFlow("")


    fun getSupplierById(id: String) {
        viewModelScope.launch {
            supplierRepository.getSupplierById(id)
                .catch { error -> _uiState.value = DetailsUiState.Error(error.message ?: "Error desconocido") }
                .collect { supplier ->
                    if (supplier != null) {
                        setInitialSupplier(supplier)
                        _initialSupplierName.value = supplier.name
                        _uiState.value = DetailsUiState.Success(supplier, true)
                    } else {
                        _uiState.value = DetailsUiState.NotFound
                    }
                }
        }
    }


     fun setInitialSupplier(supplierEntity: SupplierEntity) {
        _tempSupplier.value = Supplier(
            id = supplierEntity.id,
            name = supplierEntity.name,
            contactName = supplierEntity.contactName ?: "",
            phone = supplierEntity.phone ?: "",
            email = supplierEntity.email ?: "",
            address = supplierEntity.address ?: ""
        )
    }


    fun updateSupplier(supplierEntity: SupplierEntity) {
        viewModelScope.launch {
            if (validateSupplier()) {
                try { supplierRepository.updateSupplier(supplierEntity) }
                catch (e: Exception) { Log.e("SupplierDetailsViewModel", "Error al actualizar el proveedor", e) }
            }
        }
    }

    fun deleteSupplier(supplierEntity: SupplierEntity) {
        viewModelScope.launch {
            try { supplierRepository.deleteSupplier(supplierEntity) }
            catch (e: Exception) { Log.e("SupplierDetailsViewModel", "Error al eliminar el proveedor", e) }
        }
    }


    fun updateSupplierFields(field: String, value: String) {
        _tempSupplier.update { current ->
            when (field) {
                "name" -> current.copy(name = value)
                "contactName" -> current.copy(contactName = value)
                "phone" -> current.copy(phone = value)
                "email" -> current.copy(email = value)
                "address" -> current.copy(address = value)
                else -> current
            }
        }

        // Validar después del cambio
        viewModelScope.launch {
            val validation = validateSupplier(_tempSupplier.value)
            Log.d("SupplierDetailsViewModel", "Validación: $validation")
            Log.d("SupplierDetailsViewModel", "Error Stock: ${_validationsSupplierState.value}")

            // Actualizar isEntryValid en el UiState
            _uiState.update { current ->
                if (current is DetailsUiState.Success) {
                    current.copy(isEntryValid = validation)
                } else current
            }
        }
    }


    private suspend fun validateSupplier(supplier: Supplier = _tempSupplier.value): Boolean {
        val errors = SupplierValidationState(
            nameError = validateNameSupplier(supplier.name, _initialSupplierName.value),
            phoneError = validatePhoneSupplier(supplier.phone),
            emailError =
            if (supplier.phone.isBlank() && supplier.email.isBlank()) {
                "Debe especificar un número de teléfono o un email"
            } else validateEmailSupplier(supplier.email)
        )

        _validationsSupplierState.value = errors

        return errors.run {
            nameError == null &&
            phoneError == null &&
            emailError == null
        }
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