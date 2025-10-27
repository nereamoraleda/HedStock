package es.cursos.android.ejercicios.stocksnma.ui.screen.home.products

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.local.entity.relations.ProductWithSupplierAndCategory
import es.cursos.android.ejercicios.stocksnma.ui.components.ChildCheckBox
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomSearchBarHistory
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomSearchNotFound
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSearchBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NothingCreateScreen
import es.cursos.android.ejercicios.stocksnma.ui.components.ParentCheckBox
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.HomeErrorScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.HomeLoadingScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.ProductHomeUiState
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.ProductSearchTable
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.ProductTableBody
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.ProductTableHeader


@Composable
fun ProductSectionScreen(navigateToProductDetails: (String) -> Unit) {
    val viewModel: ProductSectionViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    when (val uiState = state) {
        is ProductHomeUiState.Loading -> {
            HomeLoadingScreen()
        }

        is ProductHomeUiState.Error -> {
            HomeErrorScreen(messageError = uiState.messageError)
        }

        is ProductHomeUiState.Success -> {
            ProductSectionBodyScreen(
                products = uiState.products,
                navigateToProductDetails = navigateToProductDetails
            )
        }
    }
}


@Composable
fun ProductSectionBodyScreen(
    viewModel: ProductSectionViewModel = hiltViewModel(),
    products: List<ProductWithSupplierAndCategory>,
    navigateToProductDetails: (String) -> Unit,  // Cambiar a (Long) más adelante
    modifier: Modifier = Modifier
) {
    // Variables - Search Bar
    val isSearching by viewModel.isSearching.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchHistory by viewModel.productSearchHistory.collectAsState()
    val searchResults by viewModel.productSearchResults.collectAsState()

    // Variables - CheckBox
    val selectedProducts by viewModel.selectedProducts.collectAsState()
    val allSelected = selectedProducts.size == products.size


    // Variable - Estado del Parent CheckBox
    val parentState = when {
        selectedProducts.size == products.size && products.isNotEmpty() -> ToggleableState.On
        selectedProducts.isEmpty() -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
            //.alpha()
        ) {
            GeneralSearchBar(
                query = searchQuery,
                onQueryChange = {
                    viewModel.onSearchQueryChange(it)
                    viewModel.searchProductByName(it)
                },
                cleanQuery = { viewModel.clearSearchQuery() },
                active = isSearching,
                onActiveChange = { viewModel.onToggleSearch() },
                placeholderText = stringResource(R.string.search_product)
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
                                viewModel.searchProductByName(recentSearch)
                            },
                            onClearHistory = { viewModel.resetProductSearchHistory() }
                        )
                    } else if (searchResults.isEmpty()) {
                        CustomSearchNotFound()
                    } else {
                        ProductSearchTable(
                            products = searchResults,
                            onProductClick = { id, name ->
                                navigateToProductDetails(id)
                                viewModel.addProductSearchHistory(name)
                                viewModel.onToggleSearch()
                            }
                        )
                    }
                }
            }
        }

        //Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        if (products.isEmpty()) {
            NothingCreateScreen(nothingCreateText = stringResource(R.string.no_products))

        } else {
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
                        .padding(end = dimensionResource(R.dimen.padding_16dp))
                ) {

                    // Parent CheckBox
                    ParentCheckBox(
                        state = parentState,
                        onClick = {
                            if (selectedProducts.isEmpty()) {
                                viewModel.toggleAllProductsSelection(
                                    selectAll = !allSelected,
                                    products = products
                                )
                            } else viewModel.unselectAllProducts()
                        },
                        modifier = Modifier.width(80.dp)
                    )

                    // Encabezados de la tabla
                    ProductTableHeader()
                }

                HorizontalDivider()

                // Columna que contiene las filas de la tabla
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(products) { product ->

                        // Fila que contiene los datos de cada producto
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { navigateToProductDetails(product.product.id) }
                                .background(
                                    if (selectedProducts.contains(product.product.id))
                                        MaterialTheme.colorScheme.surfaceVariant
                                    else Color.Transparent
                                )
                                .padding(end = dimensionResource(R.dimen.padding_16dp))

                        ) {
                            ChildCheckBox(
                                checked = selectedProducts.contains(product.product.id),  // O: product.id in selectedProducts
                                onCheckedChange = { viewModel.toggleProductSelection(product.product.id) },
                                modifier = Modifier.width(80.dp)
                            )
                            ProductTableBody(product)
                            HorizontalDivider()
                        }

                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
