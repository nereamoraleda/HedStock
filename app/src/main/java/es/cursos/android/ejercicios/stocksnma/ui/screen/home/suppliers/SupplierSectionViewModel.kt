package es.cursos.android.ejercicios.stocksnma.ui.screen.home.suppliers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.local.datastore.AppDataStore
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.data.mapper.toSupplier
import es.cursos.android.ejercicios.stocksnma.data.remote.api.SupplierApi
import es.cursos.android.ejercicios.stocksnma.data.repository.supplier.SupplierRepository
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.SupplierHomeUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierSortOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierSectionViewModel @Inject constructor(
    private val api: SupplierApi,
    private val supplierRepository: SupplierRepository,
    private val dataStoreManager: AppDataStore
): ViewModel() {

//    private val _supplierSearchHistory = MutableStateFlow<List<String>>(emptyList())
//    val supplierSearchHistory: StateFlow<List<String>> = _supplierSearchHistory.asStateFlow()
    val supplierSearchHistory: StateFlow<List<String>> =
        dataStoreManager.search.supplierSearchHistory.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

//    private val _supplierSortOption = MutableStateFlow(SupplierSortOptions.NAME_ASC)
//    val supplierSortOption: StateFlow<SupplierSortOptions> = _supplierSortOption.asStateFlow()
    val supplierSortOption: StateFlow<SupplierSortOptions> =
        dataStoreManager.sort.supplierSortBy.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SupplierSortOptions.NAME_ASC
        )

    fun setSupplierSortOption(orderBy: SupplierSortOptions) {
        viewModelScope.launch {
            dataStoreManager.sort.setSupplierSortOption(orderBy)
        }
    }

//    init {
//        viewModelScope.launch {
//            dataStoreManager.supplierOrderBy.collect {
//                _supplierSortOption.value = it
//            }
//        }
//
//        viewModelScope.launch {
//            dataStoreManager.supplierSearchHistory.collect {
//                _supplierSearchHistory.value = it
//            }
//        }
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SupplierHomeUiState> =
        dataStoreManager.sort.supplierSortBy
            .flatMapLatest { sortOption ->
                flow {
                    emit(SupplierHomeUiState.Loading)
                    val suppliers = api.getAllSuppliers().map { it.toSupplier() }

//                    val sorted = when (sortOption) {
//                        SupplierSortOption.NAME_ASC -> suppliers.sortedBy { it.name.lowercase() }
//                        SupplierSortOption.NAME_DESC -> suppliers.sortedByDescending { it.name.lowercase() }
//                        SupplierSortOption.RECENT -> suppliers.sortedByDescending { it.createdAt }
//                        else -> suppliers
//                    }

                    //emit(SupplierHomeUiState.Success(sorted, sortOption))
                    emit(SupplierHomeUiState.Success(suppliers, sortOption))
                }.catch { e ->
                    emit(SupplierHomeUiState.Error(e.message ?: "Error al obtener proveedores"))
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SupplierHomeUiState.Loading
            )


//    @OptIn(ExperimentalCoroutinesApi::class)
//    val uiState: StateFlow<SupplierHomeUiState> = dataStoreManager.sort.supplierSortBy //_supplierSortOption
//        .flatMapLatest { sortOption ->
//            supplierRepository.getAllSuppliers(sortOption)
//                .map<List<SupplierEntity>, SupplierHomeUiState> { suppliers ->
//                    val sup = suppliers.map { it.toSupplier() }
//                    SupplierHomeUiState.Success(sup, sortOption)
//                }
//                .catch { e ->
//                    emit(SupplierHomeUiState.Error(e.message ?: "Error inesperado"))
//                }
//        }.stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = SupplierHomeUiState.Loading
//        )
//
//
//    fun getSuppliers() {
//        viewModelScope.launch {
//            try {
//                val suppliers = api.getAllSuppliers().map { it.toSupplier() }
//                val state = uiState.value
//                if (state is SupplierHomeUiState.Success) {
//                    state.copy(suppliers = suppliers)
//                }
//            } catch (e: Exception) {
//                Log.i("ERROR", "Error al obtener la lista de proveedores")
//            }
//        }
//    }


    // -------------------- SEARCH BAR --------------------
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _supplierSearchResults = MutableStateFlow<List<SupplierEntity>>(emptyList())
    val supplierSearchResults: StateFlow<List<SupplierEntity>> = _supplierSearchResults.asStateFlow()

    fun searchSupplierByName(nameQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            supplierRepository.searchSupplierByName(nameQuery)
                .catch { exception ->
                    Log.e("Search supplier error", "Error al buscar proveedores", exception)
                    _supplierSearchResults.value = emptyList() // Evita que crashee si hay error
                }
                .collect { results ->
                    _supplierSearchResults.value = results
                }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    fun addSupplierSearchHistory(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.search.addSupplierSearchHistory(query)
        }
    }

    fun resetSupplierSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.search.clearSupplierHistory()
        }
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value

        if (_selectedSuppliers.value.isNotEmpty()) _selectedSuppliers.value = emptySet()

        if (!_isSearching.value) {
            _searchQuery.value = ""
            _supplierSearchResults.value = emptyList()
        }
    }


    // -------------------- CHECK BOX --------------------
    private val _selectedSuppliers = MutableStateFlow<Set<String>>(emptySet()) // IDs seleccionados
    val selectedSuppliers: StateFlow<Set<String>> = _selectedSuppliers.asStateFlow()

    fun deleteSelectedSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                supplierRepository.deleteSelectedSuppliers(_selectedSuppliers.value.toList())
                unselectAllSuppliers() // Limpiar selección tras la eliminación
            } catch (e: Exception) {
                Log.e(
                    "Eliminar proveedores error",
                    "Error al eliminar los proveedores seleccionados", e
                )
            }
        }
    }

    fun toggleSupplierSelection(supplierId: String) {
        _selectedSuppliers.value = _selectedSuppliers.value.toMutableSet().apply {
            if (contains(supplierId)) remove(supplierId) else add(supplierId)
        }
    }

    fun selectAllSuppliers(selectAll: Boolean, suppliers: List<Supplier>) {
        _selectedSuppliers.value = if (selectAll) {
            suppliers.map { it.id }.toSet()
        } else {
            emptySet()
        }
    }

    fun unselectAllSuppliers() {
        _selectedSuppliers.value = emptySet()
    }
}