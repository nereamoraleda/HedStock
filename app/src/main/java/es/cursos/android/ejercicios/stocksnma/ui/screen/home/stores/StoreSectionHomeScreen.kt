package es.cursos.android.ejercicios.stocksnma.ui.screen.home.stores

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.mapper.orDefault
import es.cursos.android.ejercicios.stocksnma.domain.model.store.StoreGeneralView
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomSearchBarHistory
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralHorizontalDividerIfLast
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSearchBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NothingCreateScreen
import es.cursos.android.ejercicios.stocksnma.ui.components.SearchNotFoundContent

@Composable
fun StoreSectionHomeScreen(navigateToStoreDetails: (Long) -> Unit) {

    // -------------------- VARIABLES -------------------- //
    val viewModel: StoreSectionHomeViewModel = hiltViewModel()
    val storesList by viewModel.storeList.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    // Search bar
    val isSearching by viewModel.isSearching.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchHistory by viewModel.storeSearchHistory.collectAsState()
    val searchResults by viewModel.storeSearchResults.collectAsState()

    // -------------------- BÚSQUEDA DE TIENDAS -------------------- //
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GeneralSearchBar(
            query = searchQuery,
            onQueryChange = {
                viewModel.onSearchQueryChange(it)
                viewModel.searchStore(it)
            },
            active = isSearching,
            onActiveChange = { viewModel.toggleSearch() },
            cleanQuery = { viewModel.clearSearchQuery() },
            placeholderText = stringResource(R.string.search_store)
        ) {
            if ((searchQuery.isEmpty()) && (searchHistory.isNotEmpty())) {
                CustomSearchBarHistory(
                    searchHistory = searchHistory,
                    onClickSearch = { recentSearch ->
                        viewModel.onSearchQueryChange(recentSearch)
                        viewModel.searchStore(recentSearch)
                    },
                    onClearHistory = { viewModel.resetSearchHistory() }
                )
            } else if (searchResults.isEmpty()) {
                SearchNotFoundContent()
            } else {
                LazyColumn {
                    items(searchResults) { storeFound ->
                        StoreRow(
                            store = storeFound,
                            onStoreClick = {
                                storeFound.id?.let { navigateToStoreDetails(it) }
                                viewModel.addSearchHistory(storeFound.name)
                                viewModel.toggleSearch()
                            }
                        )
                        GeneralHorizontalDividerIfLast(storeFound, searchResults)
                    }
                }
            }
        }


        // -------------------- LISTADO DE TIENDAS -------------------- /
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = {
                isRefreshing = true
                viewModel.refreshStores()
                isRefreshing = false
            }
        ) {
            if (storesList.isEmpty()) NothingCreateScreen(nothingCreateText = stringResource(R.string.no_stores))
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(storesList) { store ->
                        StoreRow(
                            store = store,
                            onStoreClick = { store.id?.let { navigateToStoreDetails(it) } }
                        )
                        GeneralHorizontalDividerIfLast(store, storesList)
                    }
                }
            }
        }
    }
}


@Composable
fun StoreRow(
    store: StoreGeneralView,
    onStoreClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStoreClick() }
            .padding(horizontal = 16.dp)
            .padding(vertical = 12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_store),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )

        Column {
            Text(text = store.name, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = store.city.orDefault(stringResource(R.string.store_city_not_def)),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = store.email.orDefault(stringResource(R.string.store_email_not_def)),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}