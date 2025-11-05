package es.cursos.android.ejercicios.stocksnma.ui.screen.home.suppliers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.local.datastore.AppDataStore
import es.cursos.android.ejercicios.stocksnma.data.mapper.toSupplierHomeView
import es.cursos.android.ejercicios.stocksnma.data.remote.api.SupplierApi
import es.cursos.android.ejercicios.stocksnma.domain.model.SupplierHomeView
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.SupplierHomeUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierSortOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplierSectionViewModel @Inject constructor(
    private val api: SupplierApi,
    private val dataStoreManager: AppDataStore
): ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 0)

    // Ordenación de la lista general de proveedores
    val supplierSortOption: StateFlow<SupplierSortOptions> =
        dataStoreManager.sort.supplierSortBy.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SupplierSortOptions.NAME_ASC
        )


    // Estado de la pantalla de la lista general de proveedores (HomeScreen/SupplierSectionHomeScreen)
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = dataStoreManager.sort.supplierSortBy
        .flatMapLatest { sortOption ->
            refreshTrigger
                .onStart { emit(Unit) } // Carga inicial
                .flatMapLatest {
                    flow {
                        emit(SupplierHomeUiState.Loading)

                        val response = api.getSuppliers(
                            sortBy = sortOption.sortBy ?: "name",
                            direction = sortOption.direction ?: "asc"
                        )
                        if (response.isSuccessful) {
                            val suppliers = response.body()?.map { it.toSupplierHomeView() } ?: emptyList()
                            emit(SupplierHomeUiState.Success(suppliers, sortOption))
                        }

                    }.catch { e ->
                        emit(SupplierHomeUiState.Error(e.message ?: "Error al obtener proveedores"))
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SupplierHomeUiState.Loading
        )

    /**
     * FUNCIÓN - Refrescar la lista general de proveedores
     */
    fun refreshSuppliers() {
        viewModelScope.launch { refreshTrigger.emit(Unit) }
    }

    /**
     * FUNCIÓN - Asignar otra ordenación a la lista general de proveedores
     */
    fun setSupplierSortOption(sortOption: SupplierSortOptions) {
        viewModelScope.launch {
            dataStoreManager.sort.setSupplierSortOption(sortOption)
        }
    }



    // -------------------- SEARCH BAR -------------------- //

    // Estado de búsqueda (SearchBar activo/inactivo)
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    // Query de búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Resultados de búsqueda de proveedores
    private val _supplierSearchResults = MutableStateFlow<List<SupplierHomeView>>(emptyList())
    val supplierSearchResults: StateFlow<List<SupplierHomeView>> = _supplierSearchResults.asStateFlow()

    // Historial de búsqueda de proveedores
    val supplierSearchHistory: StateFlow<List<String>> =
        dataStoreManager.search.supplierSearchHistory.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )



    /**
     * FUNCIÓN - Buscar proveedores (Por: NAME - CONTACT_NAME - EMAIL - PHONE - CITY)
     * @param query - Campo del proveedor a buscar
     */
    fun searchSuppliers(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = api.searchSuppliers(query)
                if (response.isSuccessful) {
                    val responseModel = response.body()?.map { it.toSupplierHomeView() }
                    _supplierSearchResults.value = responseModel ?: emptyList()

                } else {
                    Log.e("Search supplier error", "Error al buscar proveedores")
                }

            } catch (e: Exception) { Log.e("Search supplier error", "Error al buscar proveedores", e) }
        }
    }

    /**
     * FUNCIÓN - Actualizar el valor de la query de búsqueda
     * @param newQuery - Nueva query de búsqueda
     */
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    /**
     * FUNCIÓN - Limpiar la query de búsqueda
     */
    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    /**
     * FUNCIÓN - Añadir una nueva query de búsqueda al historial
     * @param query - Nueva query de búsqueda
     */
    fun addSupplierSearchHistory(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.search.addSupplierSearchHistory(query)
        }
    }

    /**
     * FUNCIÓN - Limpiar el historial de búsquedas de proveedores
     */
    fun resetSupplierSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager.search.clearSupplierHistory()
        }
    }

    /**
     * FUNCIÓN - Cambiar el estado de búsqueda
     */
    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value

        if (!_isSearching.value) {
            _searchQuery.value = ""
            _supplierSearchResults.value = emptyList()
        }
    }
}
