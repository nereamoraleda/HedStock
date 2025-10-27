package es.cursos.android.ejercicios.stocksnma.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "hedstock_preferences")

    // ---------- CLAVES DE LAS PREFERENCIAS ----------
    companion object {
        // Claves del inicio de sesión
        private val USER_ID_KEY = longPreferencesKey("user_id")
        private val STORE_ID_KEY = longPreferencesKey("store_id")
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val TOKEN_KEY = stringPreferencesKey("token")

        // Claves de los historiales de búsqueda
        private val PRODUCT_SEARCH_HISTORY_KEY = stringSetPreferencesKey("products_search_history")
        private val SUPPLIER_SEARCH_HISTORY_KEY =
            stringSetPreferencesKey("suppliers_search_history")
        private val USER_SEARCH_HISTORY_KEY = stringSetPreferencesKey("users_search_history")

        // Claves de las preferencias de ordenación
        private val PRODUCT_SORT_BY_KEY = stringPreferencesKey("product_sort_by")
        private val SUPPLIER_SORT_BY_KEY = stringPreferencesKey("supplier_sort_by")
    }


    // ---------- VALORES DE LAS PREFERENCIAS ----------
    val userId: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY] ?: -1L
    }
    val storeId: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[STORE_ID_KEY] ?: -1L
    }
    val userRole: Flow<UserRoles> = context.dataStore.data.map { preferences ->
        val savedValue = preferences[USER_ROLE_KEY]
        UserRoles.entries.find { it.name == savedValue } ?: UserRoles.DESCONOCIDO
    }
    val token: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY] ?: ""
    }

    val productSearchHistory: Flow<List<String>> = context.dataStore.data.map { preferences ->
        (preferences[PRODUCT_SEARCH_HISTORY_KEY]?.toList() ?: emptyList())
    }

    val supplierSearchHistory: Flow<List<String>> = context.dataStore.data.map { preferences ->
        (preferences[SUPPLIER_SEARCH_HISTORY_KEY]?.toList() ?: emptyList())
    }

    val userSearchHistory: Flow<List<String>> = context.dataStore.data.map { preferences ->
        (preferences[USER_SEARCH_HISTORY_KEY]?.toList() ?: emptyList())
    }


    val productOrderBy: Flow<ProductSortOptions> = context.dataStore.data.map { preferences ->
        val savedValue = preferences[PRODUCT_SORT_BY_KEY]
        ProductSortOptions.entries.find { it.name == savedValue } ?: ProductSortOptions.NAME_ASC
    }

    val supplierOrderBy: Flow<SupplierSortOptions> = context.dataStore.data.map { preferences ->
        val savedValue = preferences[SUPPLIER_SORT_BY_KEY]
        SupplierSortOptions.entries.find { it.name == savedValue } ?: SupplierSortOptions.NAME_ASC
    }


    // ---------- ACCIONES DE LAS PREFERENCIAS ----------
    // LOGIN
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun saveStoreId(storeId: Long) {
        context.dataStore.edit { preferences ->
            preferences[STORE_ID_KEY] = storeId
        }
    }


    suspend fun saveUserRole(role: UserRoles) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role.name
        }
    }

    suspend fun clearSessionData() {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = -1L
            preferences[STORE_ID_KEY] = -1L
            preferences[USER_ROLE_KEY] = ""
            preferences[TOKEN_KEY] = ""
        }
    }


    // ORDENACIÓN
    suspend fun setProductOrderBy(orderBy: ProductSortOptions) {
        context.dataStore.edit { preferences ->
            preferences[PRODUCT_SORT_BY_KEY] = orderBy.name
        }
    }

    suspend fun setSupplierOrderBy(orderBy: SupplierSortOptions) {
        context.dataStore.edit { preferences ->
            preferences[SUPPLIER_SORT_BY_KEY] = orderBy.name
        }
    }


    // HISTORIALES DE BÚSQUEDA
    suspend fun addProductSearchHistory(query: String) {
        context.dataStore.edit { preferences ->
            val currentHistory = preferences[PRODUCT_SEARCH_HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            currentHistory.add(query)
            preferences[PRODUCT_SEARCH_HISTORY_KEY] = currentHistory
        }
    }

    suspend fun resetProductSearchHistory() {
        context.dataStore.edit { preferences ->
            preferences[PRODUCT_SEARCH_HISTORY_KEY] = emptySet()
        }
    }


    suspend fun addSupplierSearchHistory(query: String) {
        context.dataStore.edit { preferences ->
            val currentHistory = preferences[SUPPLIER_SEARCH_HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            currentHistory.add(query)
            preferences[SUPPLIER_SEARCH_HISTORY_KEY] = currentHistory
        }
    }

    suspend fun resetSupplierSearchHistory() {
        context.dataStore.edit { preferences ->
            preferences[SUPPLIER_SEARCH_HISTORY_KEY] = emptySet()
        }
    }


    suspend fun addUserSearchHistory(query: String) {
        context.dataStore.edit { preferences ->
             val currentHistory = preferences[USER_SEARCH_HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            currentHistory.add(query)
            preferences[USER_SEARCH_HISTORY_KEY] = currentHistory
        }
    }

    suspend fun resetUserSearchHistory() {
        context.dataStore.edit { preferences ->
            preferences[USER_SEARCH_HISTORY_KEY] = emptySet()
        }
    }
}