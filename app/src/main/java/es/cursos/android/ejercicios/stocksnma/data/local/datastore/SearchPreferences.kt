package es.cursos.android.ejercicios.stocksnma.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.searchDataStore by preferencesDataStore("search_preferences")

class SearchPreferences(private val context: Context) {

    // -------------------- CLAVES DE LAS PREFERENCIAS -------------------- //
    companion object {
        private val PRODUCT_SEARCH_HISTORY_KEY = stringSetPreferencesKey("product_search_history")
        private val SUPPLIER_SEARCH_HISTORY_KEY = stringSetPreferencesKey("supplier_search_history")
        private val USER_SEARCH_HISTORY_KEY = stringSetPreferencesKey("user_search_history")
        private val STORE_SEARCH_HISTORY_KEY = stringSetPreferencesKey("store_search_history")
    }


    // -------------------- VALORES DE LAS PREFERENCIAS -------------------- //
    val productSearchHistory: Flow<List<String>> = context.searchDataStore.data.map { prefs ->
        prefs[PRODUCT_SEARCH_HISTORY_KEY]?.toList() ?: emptyList()
    }

    val supplierSearchHistory: Flow<List<String>> = context.searchDataStore.data.map { prefs ->
        prefs[SUPPLIER_SEARCH_HISTORY_KEY]?.toList() ?: emptyList()
    }

    val userSearchHistory: Flow<List<String>> = context.searchDataStore.data.map { prefs ->
        prefs[USER_SEARCH_HISTORY_KEY]?.toList() ?: emptyList()
    }

    val storeSearchHistory: Flow<List<String>> = context.searchDataStore.data.map { prefs ->
        prefs[STORE_SEARCH_HISTORY_KEY]?.toList() ?: emptyList()
    }


    // -------------------- ACCIONES DE LAS PREFERENCIAS -------------------- //
    // GUARDAR EN HISTORIAL DE BÚSQUEDAS
    suspend fun addProductSearchHistory(query: String) {
        context.searchDataStore.edit { prefs ->
            val currentHistory = prefs[PRODUCT_SEARCH_HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            val updatedHistory = (currentHistory.toList() + query).distinct().takeLast(10)

            prefs[PRODUCT_SEARCH_HISTORY_KEY] = updatedHistory.toSet()
        }
    }

    suspend fun addSupplierSearchHistory(query: String) {
        context.searchDataStore.edit { prefs ->
            val currentHistory = prefs[SUPPLIER_SEARCH_HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            val updatedHistory = (currentHistory.toList() + query).distinct().takeLast(10)

            prefs[SUPPLIER_SEARCH_HISTORY_KEY] = updatedHistory.toSet()
        }
    }

    suspend fun addUserSearchHistory(query: String) {
        context.searchDataStore.edit { prefs ->
            val currentHistory = prefs[USER_SEARCH_HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            val updatedHistory = (currentHistory.toList() + query).distinct().takeLast(10)

            prefs[USER_SEARCH_HISTORY_KEY] = updatedHistory.toSet()
        }
    }

    suspend fun addStoreSearchHistory(query: String) {
        context.searchDataStore.edit { prefs ->
            val currentHistory = prefs[STORE_SEARCH_HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            val updatedHistory = (currentHistory.toList() + query).distinct().takeLast(10)

            prefs[STORE_SEARCH_HISTORY_KEY] = updatedHistory.toSet()
        }
    }


    // RESETEAR HISTORIAL DE BÚSQUEDAS
    suspend fun clearProductHistory() {
        context.searchDataStore.edit { it[PRODUCT_SEARCH_HISTORY_KEY] = emptySet() }
    }

    suspend fun clearSupplierHistory() {
        context.searchDataStore.edit { it[SUPPLIER_SEARCH_HISTORY_KEY] = emptySet() }
    }

    suspend fun clearUserHistory() {
        context.searchDataStore.edit { it[USER_SEARCH_HISTORY_KEY] = emptySet() }
    }

    suspend fun clearStoreHistory() {
        context.searchDataStore.edit { it[STORE_SEARCH_HISTORY_KEY] = emptySet() }
    }
}