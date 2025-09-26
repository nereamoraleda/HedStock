package es.cursos.android.ejercicios.stocksnma.ui.screen.supplier

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.mapper.toSupplier
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.data.repository.supplier.SupplierRepository
import javax.inject.Inject


@HiltViewModel
class SupplierCreationViewModel @Inject constructor(
    private val supplierRepository: SupplierRepository
) : ViewModel() {

    var supplierUiState by mutableStateOf(SupplierUiState())
        private set


    suspend fun saveSupplier() {
        if (validateInput()) {
            supplierRepository.insertSupplier(supplierUiState.supplierItem.toSupplier())
            supplierUiState = SupplierUiState(supplierItem = Supplier())  // Limpiar los campos y generar nuevo ID
        }
    }


    fun updateUiState(supplierItem: Supplier) {
        supplierUiState = SupplierUiState(
            supplierItem = supplierItem,
            isEntryValid = validateInput(supplierItem))

    }


    private fun validateInput(uiState: Supplier = supplierUiState.supplierItem): Boolean {
        return listOf(
            uiState.name.isNotBlank(),
            uiState.contactName.isNotBlank(),
            (uiState.phone.isNotBlank() || uiState.email.isNotBlank()),
        ).all { it }
    }


    data class SupplierUiState(
        val supplierItem: Supplier = Supplier(),
        val isEntryValid: Boolean = false
    )
}