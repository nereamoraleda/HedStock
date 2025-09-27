package es.cursos.android.ejercicios.stocksnma.ui.screen.home.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.local.AppDataStore
import es.cursos.android.ejercicios.stocksnma.data.local.entity.relations.ProductWithSupplierAndCategory
import es.cursos.android.ejercicios.stocksnma.data.repository.product.ProductRepository
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.ProductHomeUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSortOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductSectionViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val dataStoreManager: AppDataStore
): ViewModel() {

    private val _productSortOption = MutableStateFlow(ProductSortOptions.NAME_ASC)
    val productSortOption: StateFlow<ProductSortOptions> = _productSortOption.asStateFlow()

    fun setProductSortOption(orderBy: ProductSortOptions) {
        viewModelScope.launch {
            dataStoreManager.setProductOrderBy(orderBy)
        }
    }

    private val _productSearchHistory = MutableStateFlow<List<String>>(emptyList())
    val productSearchHistory: StateFlow<List<String>> = _productSearchHistory.asStateFlow()


    init {
        viewModelScope.launch {
            dataStoreManager.productOrderBy.collect {
                _productSortOption.value = it
            }
        }

        viewModelScope.launch {
            dataStoreManager.productSearchHistory.collect {
                _productSearchHistory.value = it
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProductHomeUiState> = _productSortOption
        .flatMapLatest { sortOption ->
            productRepository.getAllProducts(sortOption)
                .map<List<ProductWithSupplierAndCategory>, ProductHomeUiState> { products ->
                    ProductHomeUiState.Success(products, sortOption)
                }
                .catch { e ->
                    emit(ProductHomeUiState.Error(e.message ?: "Error inesperado"))
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductHomeUiState.Loading
        )


    // -------------------- SEARCH BAR --------------------
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _productSearchResults = MutableStateFlow<List<ProductWithSupplierAndCategory>>(emptyList())
    val productSearchResults: StateFlow<List<ProductWithSupplierAndCategory>> = _productSearchResults.asStateFlow()

    //private val _productSearchHistory = MutableStateFlow<List<String>>(emptyList())
    //val productSearchHistory: StateFlow<List<String>> = _productSearchHistory.asStateFlow()


    fun searchProductByName(nameQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.searchProductByName(nameQuery)
                .catch { exception ->
                    Log.e("Search product error", "Error al buscar productos", exception)
                    _productSearchResults.value = emptyList() // Evita que crashee si hay error
                }
                .collect { results ->
                    _productSearchResults.value = results
                }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    fun addProductSearchHistory(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.addProductSearchHistory(query)
        }
    }

    fun resetProductSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.resetProductSearchHistory()
        }
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value

        if (_selectedProducts.value.isNotEmpty()) _selectedProducts.value = emptySet()

        if (!_isSearching.value) {
            _searchQuery.value = ""
            _productSearchResults.value = emptyList()
        }
    }


    // -------------------- CHECK BOX --------------------
    private val _selectedProducts = MutableStateFlow<Set<String>>(emptySet())
    val selectedProducts: StateFlow<Set<String>> = _selectedProducts.asStateFlow()  // Habrá que modificar a Long más adelante

    fun deleteSelectedProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                productRepository.deleteSelectedProducts(_selectedProducts.value.toList())
                _selectedProducts.value = emptySet()
                Log.d("DELETE-SELECTED-PRODUCTS", "Eliminados: ${_selectedProducts.value}")

            } catch (e: Exception) {
                Log.e("DELETE-SELECTED-PRODUCTS", "Fallo: ${e.message}", e)
            }
        }
    }


    fun toggleProductSelection(productId: String) {
        _selectedProducts.value = _selectedProducts.value.toMutableSet().apply {
            if (contains(productId)) remove(productId) else add(productId)
        }
    }


    fun toggleAllProductsSelection(selectAll: Boolean, products: List<ProductWithSupplierAndCategory>) {
        _selectedProducts.value = if (selectAll) {
            products.map { it.product.id }.toSet()
        } else {
            emptySet()
        }
    }


    fun unselectAllProducts() {
        _selectedProducts.value = emptySet()
    }
}
