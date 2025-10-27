package es.cursos.android.ejercicios.stocksnma.ui.screen.home.users

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomSearchBarHistory
import es.cursos.android.ejercicios.stocksnma.ui.components.SearchNotFoundContent
import es.cursos.android.ejercicios.stocksnma.ui.components.ErrorContent
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralHorizontalDividerIfLast
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSearchBar
import es.cursos.android.ejercicios.stocksnma.ui.components.LoadingContent
import es.cursos.android.ejercicios.stocksnma.ui.components.NothingCreateScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.UserHomeUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.ActiveFilters
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserGroupOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles

@Composable
fun UserSectionScreen(navigateToUserDetails: (Long) -> Unit) {
    val viewModel: UserSectionViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    when (val uiState = state) {
        is UserHomeUiState.Loading -> { LoadingContent() }
        is UserHomeUiState.Error -> { ErrorContent(uiState.messageError) }
        is UserHomeUiState.Success -> {
            UserSectionBodyScreen(
                users = uiState.users,
                navigateToUserDetails = navigateToUserDetails
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserSectionBodyScreen(
    viewModel: UserSectionViewModel = hiltViewModel(),
    users: List<User>,
    navigateToUserDetails: (Long) -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }

    // Variables - Search Bar
    val isSearching by viewModel.isSearching.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchHistory by viewModel.userSearchHistory.collectAsState()
    val searchResults by viewModel.userSearchResults.collectAsState()


    // Variables - Filtros
    val userFilter by viewModel.userFilter.collectAsState()

    // -------------------- BÚSQUEDA DE USUARIOS -------------------- //
    Column(
        modifier = Modifier
            .fillMaxSize() //.alpha()
    ) {
        GeneralSearchBar(
            query = searchQuery,
            onQueryChange = {
                viewModel.onSearchQueryChange(it)
                viewModel.searchUserByName(it)
            },
            cleanQuery = { viewModel.clearSearchQuery() },
            active = isSearching,
            onActiveChange = { viewModel.toggleSearch() },
            placeholderText = stringResource(R.string.search_user)
        ) {
            if ((searchQuery.isEmpty()) && (searchHistory.isNotEmpty())) {
                CustomSearchBarHistory(
                    searchHistory = searchHistory,
                    onClickSearch = { recentSearch ->
                        viewModel.onSearchQueryChange(recentSearch)
                        viewModel.searchUserByName(recentSearch)
                    },
                    onClearHistory = { viewModel.resetSearchHistory() }
                )
            }
            else if (searchResults.isEmpty()) { SearchNotFoundContent() }
            else {
                LazyColumn {
                    items(searchResults) { user ->
                        UserRows(
                            user = user,
                            onUserClick = {
                                user.id?.let { navigateToUserDetails(it) }
                                viewModel.addSearchHistory(user.name)
                                viewModel.toggleSearch()
                            }
                        )
                        GeneralHorizontalDividerIfLast(user, searchResults)
                    }
                }
            }
        }



        // -------------------- FILTER CHIPS -------------------- //
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            GeneralFilterChip(
                label = "Todos",
                isFilterActive = userFilter.activeFilter == ActiveFilters.ALL,
                onClickFilter = { viewModel.setUserFilterActive(ActiveFilters.ALL) }
            )
            GeneralFilterChip(
                label = "Activos",
                isFilterActive = userFilter.activeFilter == ActiveFilters.ACTIVE,
                onClickFilter = { viewModel.setUserFilterActive(ActiveFilters.ACTIVE) }
            )
            GeneralFilterChip(
                label = "Inactivos",
                isFilterActive = userFilter.activeFilter == ActiveFilters.INACTIVE,
                onClickFilter = { viewModel.setUserFilterActive(ActiveFilters.INACTIVE) }
            )
        }


        // -------------------- LISTADO DE USUARIOS -------------------- //
        if (users.isEmpty()) NothingCreateScreen(nothingCreateText = stringResource(R.string.no_users))
        else {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    viewModel.refreshUsers()
                    isRefreshing= false
                }
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (userFilter.groupOption) {
                        UserGroupOptions.ROLE -> {
                            val usersGroupByRole = users.groupBy { user ->
                                try { UserRoles.valueOf(user.role).name
                                } catch (e: IllegalArgumentException) { UserRoles.DESCONOCIDO.name }
                            }.toSortedMap(compareBy { UserRoles.valueOf(it).ordinal })

                            usersGroupByRole.forEach { (group, users) ->
                                stickyHeader { StickyHeader(group) }
                                items(users) { user ->
                                    UserRows(
                                        user = user,
                                        onUserClick = { user.id?.let { navigateToUserDetails(it) } }
                                    )
                                    GeneralHorizontalDividerIfLast(user, users)
                                }
                            }
                        }
                        UserGroupOptions.STORE -> {
                            val usersGroupByStore = users
                                .groupBy { it.storeName ?: "Sin tienda" }
                                .mapValues { it.value.sortedBy { it.role } }
                                .toSortedMap(compareBy { if (it == "Sin tienda") "AAA" else it })  // Añadir sin tiendas al inicio

                            usersGroupByStore.forEach { (group, users) ->
                                stickyHeader { StickyHeader(group) }
                                items(users) { user ->
                                    UserRows(
                                        user = user,
                                        onUserClick = { user.id?.let { navigateToUserDetails(it) } }
                                    )
                                    GeneralHorizontalDividerIfLast(user, users)
                                }
                            }
                        }
                        else -> {
                            val usersSortByName = users.groupBy { user -> user.name.first().toString() }

                            usersSortByName.forEach { (group, users) ->
                                stickyHeader { StickyHeader(group) }
                                items(users) { user ->
                                    UserRows(
                                        user = user,
                                        onUserClick = { user.id?.let { navigateToUserDetails(it) } }
                                    )
                                    GeneralHorizontalDividerIfLast(user, users)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UserRows(
    user: User,
    onUserClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClick() }
            .padding(horizontal = dimensionResource(R.dimen.padding_16dp))
            .padding(vertical = dimensionResource(R.dimen.padding_8dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_user_no_photo/*user.photo ?: */),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = user.storeName ?: "Sin tienda",
                style = MaterialTheme.typography.bodySmall
            )
//            user.storeName?.let {
//                Text(
//                    text = it,
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
        }

        Text(
            text = user.role,
            style = MaterialTheme.typography.labelMedium
        )
    }
}


@Composable
fun StickyHeader(group: String) {
    Text(
        text = group,
        color = MaterialTheme.colorScheme.background,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
    )
}


@Composable
fun GeneralFilterChip(
    label: String,
    isFilterActive: Boolean,
    onClickFilter: () -> Unit
) {
    val colorsChip = FilterChipDefaults.filterChipColors(
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        selectedLabelColor = Color.White,
        containerColor = MaterialTheme.colorScheme.surface,
        //labelColor = MaterialTheme.colorScheme.primary
    )

    val borderChip = FilterChipDefaults.filterChipBorder(
        enabled = true,
        selected = true
    )

    FilterChip(
        selected = isFilterActive,
        onClick = { onClickFilter() },
        label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) },
        elevation = FilterChipDefaults.filterChipElevation(16.dp),
        shape = CircleShape,
        border = borderChip,
        colors = colorsChip
    )
}