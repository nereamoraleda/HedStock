package es.cursos.android.ejercicios.stocksnma.ui.screen.home.suppliers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.mapper.toSupplier
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.components.ChildCheckBox
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomSearchBarHistory
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomSearchNotFound
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSearchBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NothingCreateScreen
import es.cursos.android.ejercicios.stocksnma.ui.components.ParentCheckBox
import es.cursos.android.ejercicios.stocksnma.ui.components.TableCell
import es.cursos.android.ejercicios.stocksnma.ui.components.TableHeader
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.HomeErrorScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.HomeLoadingScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.SupplierHomeUiState
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.SupplierSearchTable


@Composable
fun SupplierSectionScreen(navigateToSupplierDetails: (String) -> Unit) {
    val viewModel: SupplierSectionViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    when (val uiState = state) {
        is SupplierHomeUiState.Loading -> {
            HomeLoadingScreen()
        }

        is SupplierHomeUiState.Error -> {
            HomeErrorScreen(messageError = uiState.messageError)
        }

        is SupplierHomeUiState.Success -> {
            SupplierSectionBodyScreen(
                suppliers = uiState.suppliers.map { it.toSupplier() },
                navigateToSupplierDetails = navigateToSupplierDetails
            )
        }
    }
}



@Composable
fun SupplierSectionBodyScreen(
    viewModel: SupplierSectionViewModel = hiltViewModel(),
    suppliers: List<Supplier>,
    navigateToSupplierDetails: (String) -> Unit
) {
    // Variables - Search Bar
    val isSearching by viewModel.isSearching.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchHistory by viewModel.supplierSearchHistory.collectAsState()
    val searchResults by viewModel.supplierSearchResults.collectAsState()

    // Variables - CheckBoxes
    val selectedSuppliers by viewModel.selectedSuppliers.collectAsState()
    val allSelected = selectedSuppliers.size == suppliers.size

    // Variable - Estado del Parent CheckBox
    val parentCheckBoxState = when {
        selectedSuppliers.size == suppliers.size -> ToggleableState.On
        selectedSuppliers.isEmpty() -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
            //.alpha()
        ) {
            GeneralSearchBar(
                query = searchQuery,
                onQueryChange = {
                    viewModel.onSearchQueryChange(it)
                    viewModel.searchSupplierByName(it)
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
                                viewModel.searchSupplierByName(recentSearch)
                            },
                            onClearHistory = { viewModel.resetSupplierSearchHistory() }
                        )
                    }

                    else if (searchResults.isEmpty()) { CustomSearchNotFound() }

                    else {
                        SupplierSearchTable(
                            suppliersList = searchResults,
                            onSupplierClick = { id, name ->
                                navigateToSupplierDetails(id)
                                viewModel.addSupplierSearchHistory(name)
                                viewModel.onToggleSearch()
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(dimensionResource(R.dimen.padding_16dp))
                                .horizontalScroll(rememberScrollState())  // Scroll horizontal para toda la tabla
                        )
                    }
                }
            }
        }

        //Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))

        if (suppliers.isEmpty()) {
            NothingCreateScreen(stringResource(R.string.no_suppliers))
        } else {
            // Columna que contiene la tabla de proveedores (debe ser scrollable)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState())
            ) {

                // Fila que contiene el encabezado de la tabla
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    ParentCheckBox(
                        state = parentCheckBoxState,
                        onClick = {
                            if (selectedSuppliers.isEmpty()) {
                                viewModel.selectAllSuppliers(
                                    selectAll = allSelected,
                                    suppliers = suppliers
                                )
                            }
                            else viewModel.unselectAllSuppliers()
                        },
                        modifier = Modifier.width(80.dp)
                    )
                    //TableHeader("¿NIF?", Modifier.width(100.dp))
                    TableHeader("Proveedor", Modifier.width(160.dp))
                    TableHeader("Contacto ", Modifier.width(120.dp))
                    TableHeader("Teléfono", Modifier.width(100.dp))
                    TableHeader("Email", Modifier.width(120.dp))
                    TableHeader("Dirección", Modifier.width(200.dp))
                }

                HorizontalDivider()

                // Columna que contiene las filas de la tabla
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(suppliers) { supplier ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navigateToSupplierDetails(supplier.id) }
                        ) {
                            ChildCheckBox(
                                checked = supplier.id in selectedSuppliers,
                                onCheckedChange = { viewModel.toggleSupplierSelection(supplier.id) },
                                modifier = Modifier.width(80.dp)
                            )
                            //TableCell(supplier.id, Modifier.width(100.dp))
                            TableCell(supplier.name, Modifier.width(160.dp))
                            TableCell(supplier.contactName ?: "", Modifier.width(120.dp))
                            TableCell(supplier.phone ?: "", Modifier.width(100.dp))
                            TableCell(supplier.email ?: "", Modifier.width(120.dp))
                            TableCell(supplier.address ?: "", Modifier.width(200.dp))
                        }

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
