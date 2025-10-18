package es.cursos.android.ejercicios.stocksnma.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.StoreSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierSortOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sortDataStore by preferencesDataStore("sort_preferences")

class SortPreferences(private val context: Context) {

    // -------------------- CLAVES DE LAS PREFERENCIAS -------------------- //
    companion object {
        val SORT_BY_KEY = stringPreferencesKey("sort_by")
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")

        private val PRODUCT_SORT_BY_KEY = stringPreferencesKey("product_sort_by")
        private val SUPPLIER_SORT_BY_KEY = stringPreferencesKey("supplier_sort_by")
        private val STORE_SORT_BY_KEY = stringPreferencesKey("store_sort_by")
    }


    // -------------------- VALORES DE LAS PREFERENCIAS -------------------- //
    val sortBy: Flow<String?> = context.sortDataStore.data.map { it[SORT_BY_KEY] }
    val sortOrder: Flow<String?> = context.sortDataStore.data.map { it[SORT_ORDER_KEY] }

    val productSortBy: Flow<ProductSortOptions> = context.sortDataStore.data.map { prefs ->
        val sortBy = prefs[PRODUCT_SORT_BY_KEY]
        ProductSortOptions.entries.find { it.name == sortBy } ?: ProductSortOptions.NAME_ASC
    }

    val supplierSortBy: Flow<SupplierSortOptions> = context.sortDataStore.data.map { prefs ->
        val sortBy = prefs[SUPPLIER_SORT_BY_KEY]
        SupplierSortOptions.entries.find { it.name == sortBy } ?: SupplierSortOptions.NAME_ASC
    }

    val storeSortBy: Flow<StoreSortOptions> = context.sortDataStore.data.map { prefs ->
        val sortBy = prefs[STORE_SORT_BY_KEY]
        StoreSortOptions.entries.find { it.name == sortBy } ?: StoreSortOptions.NAME_ASC
    }

    /* TODO - Añadir para User */


    // -------------------- ACCIONES DE LAS PREFERENCIAS -------------------- //
    suspend fun saveSort(sortBy: String, sortOrder: String) {
        context.sortDataStore.edit {
            it[SORT_BY_KEY] = sortBy
            it[SORT_ORDER_KEY] = sortOrder
        }
    }

    suspend fun setProductSortOption(sortBy: ProductSortOptions) {
        context.sortDataStore.edit { prefs ->
            prefs[PRODUCT_SORT_BY_KEY] = sortBy.name
        }
    }

    suspend fun setSupplierSortOption(sortBy: SupplierSortOptions) {
        context.sortDataStore.edit { prefs ->
            prefs[SUPPLIER_SORT_BY_KEY] = sortBy.name
        }
    }

    suspend fun setStoreSortOption(sortBy: StoreSortOptions) {
        context.sortDataStore.edit { prefs ->
            prefs[STORE_SORT_BY_KEY] = sortBy.name
        }
    }

    /* TODO - Añadir para User */
}
