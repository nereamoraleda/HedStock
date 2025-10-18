package es.cursos.android.ejercicios.stocksnma.ui.screen.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.local.entity.CategoryEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.ProductEntity
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.data.repository.category.CategoryRepository
import es.cursos.android.ejercicios.stocksnma.domain.model.Product
import es.cursos.android.ejercicios.stocksnma.data.repository.product.ProductRepository
import es.cursos.android.ejercicios.stocksnma.data.repository.supplier.SupplierRepository
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.validations.ProductValidationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val supplierRepository: SupplierRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // Estado de la Ui
    private val _uiState = MutableStateFlow<DetailsUiState<ProductEntity>>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState<ProductEntity>> = _uiState.asStateFlow()

    // Lista de proveedores
    private val _suppliers = MutableStateFlow<List<SupplierEntity>>(emptyList())
    val suppliers: StateFlow<List<SupplierEntity>> = _suppliers.asStateFlow()

    // Lista de categorías
    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories.asStateFlow()

    private val _tempProduct = MutableStateFlow(Product())
    val tempProduct: StateFlow<Product> = _tempProduct

    private val _productName = MutableStateFlow("")

    // Estado de validación
    private val _validationState = MutableStateFlow(ProductValidationState())
    val validationState: StateFlow<ProductValidationState> = _validationState


    /**
     * Obtener un producto por su ID de la base de datos
     *
     * @param id ID del producto a buscar en la base de datos
     */
    fun getProductById(id: String) {
        viewModelScope.launch {
            productRepository.getProductById(id)
                .catch { e -> _uiState.value = DetailsUiState.Error(e.message ?: "Error desconocido") }
                .collect { product ->
                    if (product != null) {
                        setInitialValues(product)
                        _productName.value = product.name
                        _uiState.value = DetailsUiState.Success(product, isEntryValid = true)
                    } else {
                        _uiState.value = DetailsUiState.NotFound
                    }
                }
        }
    }


    /**
     * Obtener todos los proveedores de la base de datos
     */
    fun getSuppliers() {
        viewModelScope.launch {
            supplierRepository.getAllSuppliers()
                .catch { e -> _uiState.value = DetailsUiState.Error(e.message ?: "Error desconocido") }
                .collect { suppliers ->
                    _suppliers.value = suppliers
                }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories()
                .catch { e -> _uiState.value = DetailsUiState.Error(e.message ?: "Error desconocido") }
                .collect { categories ->
                    _categories.value = categories
                }
        }
    }


    /**
     * Actualizar un producto existente en la base de datos
     */
    fun updateProduct(productEntity: ProductEntity) {
        viewModelScope.launch {
            if (validateFields()) {
                try {
                    productRepository.updateProduct(productEntity)
                } catch (e: Exception) {
                    Log.e("ProductsInfoViewModel", "Error al actualizar el producto", e)
                }
            }
        }
    }

    fun deleteProduct(productEntity: ProductEntity) {
        viewModelScope.launch {
            productRepository.deleteProduct(productEntity)
        }
    }



    /**
     * Validar los campos del producto antes de guardar
     *
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private suspend fun validateFields(product: Product = _tempProduct.value): Boolean {

        val errors = ProductValidationState(
            nameErrorMessage = validateName(product.name, _productName.value),
            //supplierError = if (product.supplierId.isBlank()) "Debe seleccionar un proveedor" else null,
            //categoryError = if (product.category.isBlank()) "Debe seleccionar una categoría" else null,
            barcodeErrorMessage = if (product.barcode.isNotBlank()) { validateBarcode(product.barcode) } else null,
            costPriceErrorMessage = validatePrice(product.costPrice),
            sellingPriceErrorMessage = validatePrice(product.price),
            stockErrorMessage = validateStock(product.stock),
            minStockErrorMessage = validateMinStock(product.minStock),
            maxStockErrorMessage = if (product.maxStock.isNotBlank()) { validateMaxStock(product.stock, product.minStock, product.maxStock) } else null,
        )

        _validationState.value = errors   // Actualizar el estado de validación

        return errors.run {
            nameErrorMessage == null &&
                    //supplierError == null &&
                    //categoryError == null &&
                    barcodeErrorMessage == null &&
                    costPriceErrorMessage == null &&
                    sellingPriceErrorMessage == null &&
                    stockErrorMessage == null &&
                    minStockErrorMessage == null &&
                    maxStockErrorMessage == null
        }
    }


    private suspend fun validateName(nameText: String, originalName: String): String? {
        if (nameText.isBlank()) return "El nombre no puede estar vacío"
        else if (nameText != originalName && productRepository.existsProductWithName(nameText)) return "Ya existe un producto con ese nombre"
        return null
    }

    private fun validatePrice(priceText: String): String? {
        if (priceText.isNotBlank()) {
            val price = priceText.toDoubleOrNull() ?: return "Debe ser un número válido"
            if (price < 0.0) return "El precio no puede ser menor que 0"
        }
        return null
    }

    private fun validateStock(stockText: String): String? {
        if (stockText.isNotBlank()) {
            val stock = stockText.toIntOrNull() ?: return "Debe ser un número válido"
            if (stock < 0) return "El stock no puede ser menor que 0"
        }
        return null
    }

    private fun validateMinStock(minStockText: String): String? {
        if (minStockText.isNotBlank()) {
            val minStock = minStockText.toIntOrNull() ?: return "Debe ser un número válido"
            if (minStock < 0) return "El stock mínimo no puede ser menor que 0"
        }
        return null
    }

    private fun validateMaxStock(stockText: String, minStockText: String, maxStockText: String): String? {
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
    }


    /**
     * Inicializar los valores del producto temporal desde un objeto ProductEntity
     *
     * @param product Objeto ProductEntity que contiene los valores iniciales
     */
    fun setInitialValues(product: ProductEntity) {
        _tempProduct.value = Product(
            id = product.id,
            name = product.name,
            brand = product.brand ?: "",
            description = product.description ?: "",
            barcode = product.barcode ?: "",
            costPrice = String.format(Locale.US, "%.2f", product.costPrice),
            price = String.format(Locale.US, "%.2f", product.price),
            stock = product.stock.toString(),
            minStock = product.minStock.toString(),
            maxStock = product.maxStock.toString(),
            supplierId = product.supplierId ?: "",
            category = product.categoryId.toString(),
            image = product.image ?: "",
            isActive = product.isActive
        )
    }


    /**
     * Actualizar un campo específico del producto
     *
     * @param field Nombre del campo a actualizar
     * @param value Nuevo valor del campo
     */
    fun updateField(field: String, value: String) {
        _tempProduct.update { current ->
            when (field) {
                "name" -> current.copy(name = value)
                "description" -> current.copy(description = value)
                "brand" -> current.copy(brand = value)
                "supplierId" -> current.copy(supplierId = value)
                "category" -> current.copy(category = value)
                "barcode" -> current.copy(barcode = value)
                "costPrice" -> current.copy(costPrice = value)
                "price" -> current.copy(price = value)
                "stock" -> current.copy(stock = value)
                "minStock" -> current.copy(minStock = value)
                "maxStock" -> current.copy(maxStock = value)
                else -> current
            }
        }

        // Validar después del cambio
        viewModelScope.launch {
            val validation = validateFields(_tempProduct.value)
            Log.d("ProductDetailsViewModel", "Validación: $validation")
            Log.d("ProductDetailsViewModel", "Error Stock: ${_validationState.value}")

            // Actualizar isEntryValid en el UiState
            _uiState.update { current ->
                if (current is DetailsUiState.Success) {
                    current.copy(isEntryValid = validation)
                } else current
            }
        }
    }

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


//    private fun validateName(nameText: String, currentId: Int? = null): String? {
//        if (nameText.isBlank()) return "El nombre no puede estar vacío"
//
//        val normalizedName = nameText.trim().lowercase()
//
//        val exists = productsList.value.any {
//            it.product.name.trim().lowercase() == normalizedName &&
//                    (currentId == null || it.product.id != currentId)
//        }
//
//        return if (exists) "Ya existe un producto con ese nombre" else null
//    }



    /*
        init {

            viewModelScope.launch {
                try {
                    repository.deleteAllProducts()  // Llama al método en el repositorio
                    Log.d("ProductsInfoViewModel", "Todos los productos fueron eliminados")
                } catch (e: Exception) {
                    Log.e("ProductsInfoViewModel", "Error al eliminar todos los productos", e)
                }
            }
        }
    */
}