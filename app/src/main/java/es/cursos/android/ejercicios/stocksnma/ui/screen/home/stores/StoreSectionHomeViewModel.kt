package es.cursos.android.ejercicios.stocksnma.ui.screen.home.stores

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.mapper.toStoreGeneralView
import es.cursos.android.ejercicios.stocksnma.data.remote.api.StoreApi
import es.cursos.android.ejercicios.stocksnma.domain.model.StoreGeneralView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreSectionHomeViewModel @Inject constructor(
    private val storeApi: StoreApi
): ViewModel() {

    init {
        getStoreList()
    }

    private val _storeList = MutableStateFlow<List<StoreGeneralView>>(emptyList())
    val storeList: StateFlow<List<StoreGeneralView>> = _storeList.asStateFlow()


    private fun getStoreList() {
        viewModelScope.launch {
            try {
                val response = storeApi.getStores()
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
}