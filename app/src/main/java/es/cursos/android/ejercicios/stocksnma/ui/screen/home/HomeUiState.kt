package es.cursos.android.ejercicios.stocksnma.ui.screen.home

import es.cursos.android.ejercicios.stocksnma.data.local.entity.relations.ProductWithSupplierAndCategory
import es.cursos.android.ejercicios.stocksnma.domain.model.SupplierHomeView
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.utils.enums.ActiveFilters
import es.cursos.android.ejercicios.stocksnma.utils.enums.HomeSections
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserGroupOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.items.NavDrawerItem

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(
        val userRole: UserRoles,
        val navDrawerSections: List<NavDrawerItem>,
        val selectedSection: HomeSections
    ) : HomeUiState()
    data class Error(val messageError: String) : HomeUiState()
}

/*
 * SEALED CLASS - Manejar el estado de la pantalla de Home por Sections
 *
 * DATA OBJECT - Loading (Pantalla de carga)
 * DATA CLASS - Success (Pantalla de éxito)
 * DATA CLASS - Error (Pantalla de error)
 */
sealed class ProductHomeUiState {
    data object Loading : ProductHomeUiState()
    data class Success(
        val products: List<ProductWithSupplierAndCategory> = emptyList(),
        val sortOption: ProductSortOptions = ProductSortOptions.NAME_ASC
    ) : ProductHomeUiState()
    data class Error(val messageError: String) : ProductHomeUiState()
}

sealed class SupplierHomeUiState {
    data object Loading : SupplierHomeUiState()
    data class Success(
        val suppliers: List<SupplierHomeView> = emptyList(),
        val sortOption: SupplierSortOptions = SupplierSortOptions.NAME_ASC
    ) : SupplierHomeUiState()
    data class Error(val messageError: String) : SupplierHomeUiState()
}

sealed class UserHomeUiState {
    data object Loading : UserHomeUiState()
    data class Success(
        val users: List<User> = emptyList(),
        val sortOption: UserSortOptions = UserSortOptions.NAME_ASC,
        val groupOption: UserGroupOptions? = null,
        val activeFilter: ActiveFilters = ActiveFilters.ALL
    ) : UserHomeUiState()
    data class Error(val messageError: String) : UserHomeUiState()
}