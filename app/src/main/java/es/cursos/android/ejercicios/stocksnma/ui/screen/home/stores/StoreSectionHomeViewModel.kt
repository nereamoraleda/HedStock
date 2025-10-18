package es.cursos.android.ejercicios.stocksnma.ui.screen.home.stores

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.local.datastore.AppDataStore
import es.cursos.android.ejercicios.stocksnma.data.mapper.toStoreGeneralView
import es.cursos.android.ejercicios.stocksnma.data.remote.api.StoreApi
import es.cursos.android.ejercicios.stocksnma.domain.model.StoreGeneralView
import es.cursos.android.ejercicios.stocksnma.utils.enums.StoreSortOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreSectionHomeViewModel @Inject constructor(
    private val storeApi: StoreApi,
    private val dataStoreManager: AppDataStore
): ViewModel() {

    private val _storeList = MutableStateFlow<List<StoreGeneralView>>(emptyList())
    val storeList: StateFlow<List<StoreGeneralView>> = _storeList.asStateFlow()

    val storeSortBy: StateFlow<StoreSortOptions> = dataStoreManager.sort.storeSortBy.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StoreSortOptions.NAME_ASC
    )


    init {
        viewModelScope.launch {
            storeSortBy.collectLatest {
                getStoreList()
            }
        }
    }


    private fun getStoreList() {
        viewModelScope.launch {
            try {
                val response = storeApi.getStores(
                    sortBy = storeSortBy.value.sortBy ?: "name",
                    direction = storeSortBy.value.direction ?: "asc",
                )
                if (response.isSuccessful) {
                    val stores = response.body()?.map { it.toStoreGeneralView() } ?: emptyList()
                    _storeList.value = stores
                } else {
                    Log.e("ERROR-STORE-LIST", "Error al obtener la lista de tiendas")
                }


            } catch (e: Exception) {
                Log.e("EXCEPTION-STORE-LIST", "Error al obtener la lista de tiendas")
            }
        }
    }


    fun refreshStores() {
        getStoreList()
        //_storeList.value = _storeList.value.copy(refresh = System.currentTimeMillis())
    }

    fun setStoreSortOption(sortBy: StoreSortOptions) {
        viewModelScope.launch {
            dataStoreManager.sort.setStoreSortOption(sortBy)
        }
    }


    // -------------------- BÚSQUEDA DE TIENDAS -------------------- //
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _storeSearchResults = MutableStateFlow<List<StoreGeneralView>>(emptyList())
    val storeSearchResults: StateFlow<List<StoreGeneralView>> = _storeSearchResults.asStateFlow()

    val storeSearchHistory: StateFlow<List<String>> =
        dataStoreManager.search.storeSearchHistory.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun searchStore(query: String) {
        viewModelScope.launch {
            try {
                val response = storeApi.searchStores(query)
                if (response.isSuccessful) {
                    val stores = response.body()?.map { it.toStoreGeneralView() } ?: emptyList()
                    _storeSearchResults.value = stores
                }

            } catch (e: Exception) {
                Log.e("EXCEPTION-STORE-SEARCH", "Error al buscar tiendas: ${e.message}")
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    fun addSearchHistory(query: String) {
        viewModelScope.launch {
            dataStoreManager.search.addStoreSearchHistory(query)
        }
    }

    fun resetSearchHistory() {
        viewModelScope.launch {
            dataStoreManager.search.clearStoreHistory()
        }
    }

    fun toggleSearch() {
        _isSearching.value = !_isSearching.value

        // Limpiar la búsqueda cuando no se está buscando
        if (!isSearching.value) {
            clearSearchQuery()
            _storeSearchResults.value = emptyList()
        }
    }
}