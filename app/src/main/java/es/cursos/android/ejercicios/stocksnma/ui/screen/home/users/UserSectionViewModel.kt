package es.cursos.android.ejercicios.stocksnma.ui.screen.home.users

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.data.datastore.AppDataStore
import es.cursos.android.ejercicios.stocksnma.data.mapper.toUser
import es.cursos.android.ejercicios.stocksnma.data.remote.HedstockApiService
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.UserHomeUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.ActiveFilters
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserFilter
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserGroupOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserSortOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserSectionViewModel @Inject constructor(
    private val apiService: HedstockApiService,
    private val dataStoreManager: AppDataStore
): ViewModel() {

    private val _userSearchHistory = MutableStateFlow<List<String>>(emptyList())
    val userSearchHistory: StateFlow<List<String>> = _userSearchHistory.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreManager.userSearchHistory.collect {
                _userSearchHistory.value = it
            }
        }
    }

    private val _userFilter = MutableStateFlow(
        UserFilter(
            sortOption = UserSortOptions.NAME_ASC,
            groupOption = null,
            ActiveFilters.ALL
        )
    )
    val userFilter: StateFlow<UserFilter> = _userFilter.asStateFlow()

    fun setUserSortOption(sort: UserSortOptions) {
        _userFilter.value = _userFilter.value.copy(sortOption = sort)
        _userFilter.value = _userFilter.value.copy(groupOption = null)
    }

    fun setUserGroupOption(group: UserGroupOptions?) {
        _userFilter.value = _userFilter.value.copy(groupOption = group)
    }

    fun setUserFilterActive(filter: ActiveFilters) {
        _userFilter.value = _userFilter.value.copy(activeFilter = filter)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UserHomeUiState> = _userFilter
        .flatMapLatest { filter ->
            flow {
                emit(UserHomeUiState.Loading)

                val response = apiService.getUsers(
                    sortBy = filter.sortOption.sortBy ?: "name",
                    direction = filter.sortOption.direction ?: "asc",
                    active = filter.activeFilter.value
                )

                if (response.isSuccessful) {
                    val users = response.body()?.map { it.toUser() } ?: emptyList()

                    val groupedOrSorted = when (filter.groupOption) {
                        UserGroupOptions.STORE -> users.groupBy { it.storeName ?: "Sin tienda" }
                            .flatMap { it.value }

                        UserGroupOptions.ROLE -> users.groupBy { it.role }
                            .flatMap { it.value }

                        null -> users
                    }

                    emit(
                        UserHomeUiState.Success(
                            users = groupedOrSorted,
                            sortOption = filter.sortOption,
                            groupOption = filter.groupOption,
                            activeFilter = filter.activeFilter
                        )
                    )

                } else {
                    emit(UserHomeUiState.Error("Error: ${response.code()}"))
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserHomeUiState.Loading
        )
    // El flatMapLatest sirve para que cuando se cambie el filtro, se actualice la lista de usuarios

    fun refreshUsers() {
        _userFilter.value = _userFilter.value.copy(refresh = System.currentTimeMillis())
    }


    // -------------------- SEARCH BAR --------------------
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _userSearchResults = MutableStateFlow<List<User>>(emptyList())
    val userSearchResults: StateFlow<List<User>> = _userSearchResults.asStateFlow()

    fun searchUserByName(query: String) {
        viewModelScope.launch {
            try {
                val response = apiService.searchUsers(query)
                if (response.isSuccessful) {
                    val users = response.body()?.map { it.toUser() } ?: emptyList()
                    _userSearchResults.value = users
                } else {
                    _userSearchResults.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("SEARCH-USERS", "Error al buscar usuarios", e)
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    fun addSearchHistory(query: String) {
        viewModelScope.launch {
            dataStoreManager.addUserSearchHistory(query)
        }
    }

    fun resetSearchHistory() {
        viewModelScope.launch {
            dataStoreManager.resetUserSearchHistory()
        }
    }

    fun toggleSearch() {
        _isSearching.value = !isSearching.value
        if (!isSearching.value) {
            clearSearchQuery()
            _userSearchResults.value = emptyList()
        }
    }
}