package es.cursos.android.ejercicios.stocksnma.ui.screen.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.mapper.toStore
import es.cursos.android.ejercicios.stocksnma.data.remote.api.StoreApi
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreDetailsViewModel @Inject constructor(
    private val storeApi: StoreApi
): ViewModel() {

    private val _store = MutableStateFlow(Store())
    val store: StateFlow<Store> = _store.asStateFlow()


    fun getDataStore(id: Long) {
        viewModelScope.launch {
            try {
                val response = storeApi.getStoreById(id)
                if (response.isSuccessful) {
                    val storeResponse = response.body()?.toStore() ?: Store()
                    _store.value = storeResponse
                    Log.i("DETAILS-STORE-GET-DATA", "Tienda: ${_store.value}")
                }
            } catch (e: Exception) {
                Log.e("DETAILS-STORE-GET-DATA", "Error: ${e.message}")
            }
        }
    }
}