package es.cursos.android.ejercicios.stocksnma.ui.screen.product

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.data.mapper.toProduct
import es.cursos.android.ejercicios.stocksnma.data.repository.category.CategoryRepository
import es.cursos.android.ejercicios.stocksnma.data.repository.product.ProductRepository
import es.cursos.android.ejercicios.stocksnma.data.repository.supplier.SupplierRepository
import es.cursos.android.ejercicios.stocksnma.domain.model.Product
import es.cursos.android.ejercicios.stocksnma.utils.Constants.TIMEOUT_MILLIS
import es.cursos.android.ejercicios.stocksnma.utils.validations.ProductValidationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductCreationViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    supplierRepository: SupplierRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // Variable - Lista de las categorías que puede tener un producto
    val categoriesList: StateFlow<List<CategoryEntity>> =
        categoryRepository.getAllCategories()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )


    // Variable - Lista de los proveedores
    val suppliersList: StateFlow<List<SupplierEntity>> =
        supplierRepository.getAllSuppliers()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )


    var productUiState by mutableStateOf(ProductUiState())
        private set  // Solo se puede modificar desde dentro del ViewModel


    fun updateUiState(productItem: Product) {
        productUiState = productUiState.copy(productItem = productItem)

        viewModelScope.launch {
            val isValid = validateInput(productItem)
            productUiState = productUiState.copy(isEntryValid = isValid)
        }
    }
    

    /**
     * FUNCIÓN - Crear una categoría
     *
     * @param category Categoría a crear
     */
    fun createCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.insertCategory(category)
            //cleanCategory()
        }
    }

    fun editCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
        }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
        }
    }


    fun saveProduct() {
        viewModelScope.launch {
            if (validateInput()) {
                productRepository.insertProduct(productUiState.productItem.toProduct())
                cleanUiState()
                Log.d("ProductCreationViewModel", "Producto guardado: ${productUiState.productItem}")
            }
        }
    }


    private val _validationState = MutableStateFlow(ProductValidationState())
    var validationState: StateFlow<ProductValidationState> = _validationState

    private suspend fun validateInput(uiState: Product = productUiState.productItem): Boolean {
        //Log.d("ProductCreationViewModel", "Validando entrada: ${productsList.value.size}")
        val nameErrorMessage = validateName(uiState.name) // Comprobamos que el nombre no esté vacío
        val barcodeErrorMessage = if (uiState.barcode.isNotBlank()) { validateBarcode(uiState.barcode) } else null

        val stockErrorMessage = if (uiState.stock.isNotBlank() && uiState.stock.toInt() < 0) "El stock no puede ser menor que 0" else { null }
        val minStockErrorMessage = if (uiState.minStock.isNotBlank() && uiState.minStock.toInt() < 0) "El stock mínimo no puede ser menor que 0" else null
        val maxStockErrorMessage = if (uiState.maxStock.isNotBlank()) { validateMaxStock(uiState.stock, uiState.minStock, uiState.maxStock) } else null

        val priceErrorMessage = if (uiState.price.isNotBlank()) { validatePrice(uiState.price) } else null
        val costPriceErrorMessage = if (uiState.costPrice.isNotBlank()) { validatePrice(uiState.costPrice) } else null

        //uiState.category.isBlank() || categoriesList.value.any { it.id.toString() == uiState.category }

        _validationState.value = ProductValidationState(
            nameErrorMessage = nameErrorMessage,
            barcodeErrorMessage = barcodeErrorMessage,
            stockErrorMessage = stockErrorMessage,
            minStockErrorMessage = minStockErrorMessage,
            maxStockErrorMessage = maxStockErrorMessage,
            sellingPriceErrorMessage = priceErrorMessage,
            costPriceErrorMessage = costPriceErrorMessage
        )

        return listOf(
            nameErrorMessage,
            stockErrorMessage,
            minStockErrorMessage,
            maxStockErrorMessage,
            priceErrorMessage,
            costPriceErrorMessage
        ).all { it == null }
    }


    private suspend fun validateName(nameText: String): String? {
        if (nameText.isBlank()) return "El nombre no puede estar vacío"
        else if (productRepository.existsProductWithName(nameText)) return "Ya existe un producto con ese nombre"
        return null
    }

    private fun validatePrice(priceText: String): String? {
        if (priceText.isNotBlank()) {
            val price = priceText.toDoubleOrNull() ?: return "Debe ser un número válido"
            if (price < 0.0) return "El precio no puede ser menor que 0"
        }
        return null
    }

    private fun validateMaxStock(
        stockText: String,
        minStockText: String,
        maxStockText: String
    ): String? {
        return if (maxStockText.toInt() < 0) "El stock máximo no puede ser menor que 0"
        else if ((stockText.isNotBlank()) && (stockText.toInt() > maxStockText.toInt())) "El stock máximo no puede ser menor que el stock"
        else if ((minStockText.isNotBlank()) && (minStockText.toInt() > maxStockText.toInt())) "El stock máximo no puede ser menor que el stock mínimo"
        else null
    }

    private fun validateBarcode(barcodeText: String): String? {
        val validLength = setOf(8, 12, 13)
        val digitsOnly = barcodeText.filter { it.isDigit() }

        if (digitsOnly.length !in validLength) return "El código de barras debe tener 8, 12 o 13 dígitos"
        return null
//          else if (barcode.length == 13) { // Para comprobar el checksum de un código de barras de 13 dígitos
//              val digits = barcode.map { it.toString().toInt() }
//              val sumOdd = digits.slice(0..11 step 2).sum()
//                val sumEven = digits.slice(1..12 step 2).sum()
//                val total = sumOdd + sumEven * 3
//                val checkDigit = 10 - (total % 10)
//                ""
//            }
    }


    // FUNCIÓN - Limpiar los campos y generar nuevo ID (una vez creado el nuevo producto)
    fun cleanUiState() {
        productUiState = ProductUiState(productItem = Product())
        _validationState.value = ProductValidationState()
    }


    data class ProductUiState(
        val productItem: Product = Product(),
        val isEntryValid: Boolean = false
    )
}