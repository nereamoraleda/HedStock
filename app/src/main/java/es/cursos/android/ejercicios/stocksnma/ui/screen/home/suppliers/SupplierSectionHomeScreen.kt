package es.cursos.android.ejercicios.stocksnma.ui.screen.home.suppliers

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.SupplierHomeView
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomSearchBarHistory
import es.cursos.android.ejercicios.stocksnma.ui.components.SearchNotFoundContent
import es.cursos.android.ejercicios.stocksnma.ui.components.ErrorContent
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralHorizontalDividerIfLast
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSearchBar
import es.cursos.android.ejercicios.stocksnma.ui.components.LoadingContent
import es.cursos.android.ejercicios.stocksnma.ui.components.NothingCreateScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.SupplierHomeUiState

@Composable
fun SupplierSectionScreen(
    navigateToSupplierDetails: (Long) -> Unit
) {
    // -------------------- VARIABLES -------------------- //
    val viewModel: SupplierSectionViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()         // Estado de la Ui (Loading, Success, Error)
    var isRefreshing by remember { mutableStateOf(false) }  // Estado de SwipeRefresh (si se está cargando o no)



    // -------------------- LAUNCHED EFFECT -------------------- //
    LaunchedEffect(state) {
        isRefreshing = state is SupplierHomeUiState.Loading
    }


    // -------------------- UI -------------------- //
    when (val uiState = state) {
        is SupplierHomeUiState.Loading -> { LoadingContent() }
        is SupplierHomeUiState.Error -> { ErrorContent(uiState.messageError) }
        is SupplierHomeUiState.Success -> {
            SupplierSectionBodyScreen(
                suppliers = uiState.suppliers,
                navigateToSupplierDetails = navigateToSupplierDetails,
                isRefreshing = isRefreshing
            )
        }
    }
}


@Composable
fun SupplierSectionBodyScreen(
    viewModel: SupplierSectionViewModel = hiltViewModel(),
    suppliers: List<SupplierHomeView>,
    navigateToSupplierDetails: (Long) -> Unit,
    isRefreshing: Boolean
) {
    // Variables - Search Bar
    val isSearching by viewModel.isSearching.collectAsState()              // Si estamos en búsqueda o no
    val searchQuery by viewModel.searchQuery.collectAsState()              // Consulta de búsqueda
    val searchHistory by viewModel.supplierSearchHistory.collectAsState()  // Historial de búsquedas de proveedores
    val searchResults by viewModel.supplierSearchResults.collectAsState()  // Listado con los resultados de la búsqueda



    // -------------------- UI -------------------- //
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            GeneralSearchBar(
                query = searchQuery,
                onQueryChange = {
                    viewModel.onSearchQueryChange(it)
                    viewModel.searchSuppliers(it)
                },
                cleanQuery = { viewModel.clearSearchQuery() },
                active = isSearching,
                onActiveChange = { viewModel.onToggleSearch() },
                placeholderText = stringResource(R.string.search_supplier)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    if ((searchQuery.isEmpty()) && (searchHistory.isNotEmpty())) {
                        CustomSearchBarHistory(
                            searchHistory = searchHistory,
                            onClickSearch = { recentSearch ->
                                viewModel.onSearchQueryChange(recentSearch)
                                viewModel.searchSuppliers(recentSearch)
                            },
                            onClearHistory = { viewModel.resetSupplierSearchHistory() }
                        )
                    }

                    else if (searchResults.isEmpty()) { SearchNotFoundContent() }

                    else {
                        LazyColumn {
                            items(searchResults) { result ->
                                SupplierRow(
                                    supplier = result,
                                    onSupplierClick = {
                                        navigateToSupplierDetails(result.id)
                                        viewModel.addSupplierSearchHistory(result.name)
                                        viewModel.onToggleSearch()
                                    }
                                )
                                GeneralHorizontalDividerIfLast(result, searchResults)
                            }
                        }
                    }
                }
            }
        }


        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { viewModel.refreshSuppliers() }
        ) {
            if (suppliers.isEmpty()) {
                NothingCreateScreen(stringResource(R.string.no_suppliers))

            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(suppliers) { supplier ->
                        SupplierRow(
                            supplier = supplier,
                            onSupplierClick = { navigateToSupplierDetails(supplier.id) }
                        )

                        GeneralHorizontalDividerIfLast(supplier, suppliers)
                    }
                }
            }
        }
    }
}


@Composable
fun SupplierRow(
    supplier: SupplierHomeView,
    onSupplierClick: () -> Unit,
    modifier: Modifier = Modifier
) {
//    GeneralCard(
//        modifier = Modifier
//            .padding(horizontal = 16.dp)
//            .padding(vertical = 12.dp)
//    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSupplierClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 12.dp)
            //.background(Color.White)
        ) {
            // Nombre del proveedor (título principal)
            Text(
                text = supplier.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            IconAndTextRow(
                icon = R.drawable.ic_user,
                text = supplier.contactName.ifEmpty { "Sin contacto" }
            )

            // Si el proveedor tiene email, lo mostramos y si no, mostramos el teléfono
            if (supplier.email.isEmpty()) {
                IconAndTextRow(
                    icon = R.drawable.ic_phone,
                    text = supplier.phone.ifEmpty { "Sin teléfono" }
                )

            } else {
                IconAndTextRow(
                    icon = R.drawable.ic_email,
                    text = supplier.email.ifEmpty { "Sin email" }
                )
            }
        }
    }
    //}
}


@Composable
fun IconAndTextRow(
    @DrawableRes icon: Int,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
