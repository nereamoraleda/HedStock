package es.cursos.android.ejercicios.stocksnma.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.local.AppDataStore
import es.cursos.android.ejercicios.stocksnma.data.local.entity.relations.ProductWithSupplierAndCategory
import es.cursos.android.ejercicios.stocksnma.data.repository.product.ProductRepository
import es.cursos.android.ejercicios.stocksnma.data.repository.supplier.SupplierRepository
import es.cursos.android.ejercicios.stocksnma.utils.enums.ActiveFilters
import es.cursos.android.ejercicios.stocksnma.utils.items.NavDrawerItem
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserFilter
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import es.cursos.android.ejercicios.stocksnma.utils.enums.HomeSections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val supplierRepository: SupplierRepository,
    private val dataStoreManager: AppDataStore,
) : ViewModel() {

    init {
        viewModelScope.launch {
            dataStoreManager.userRole.collect {
                _userRole.value = it
                loadLists()
            }
        }

        viewModelScope.launch {
            dataStoreManager.productOrderBy.collect {
                _productOrderType.value = it
            }
        }

        viewModelScope.launch {
            dataStoreManager.supplierOrderBy.collect {
                _supplierOrderType.value = it
            }
        }
    }

    private val _userRole = MutableStateFlow(UserRoles.DESCONOCIDO)
    val userRole: StateFlow<UserRoles> = _userRole.asStateFlow()



    // -------------------- VARIABLES - OBTENCIÓN DE PRODUCTOS --------------------

    // Variables - Tipos de orden de la tabla Products
    private val _productOrderType = MutableStateFlow(ProductSortOptions.NAME_ASC)
    val productOrderTypeProducts: StateFlow<ProductSortOptions> = _productOrderType.asStateFlow()

    private val _supplierOrderType = MutableStateFlow(SupplierSortOptions.NAME_ASC)
    val supplierOrderTypeSuppliers: StateFlow<SupplierSortOptions> = _supplierOrderType.asStateFlow()

    private val _userFilter = MutableStateFlow(
        UserFilter(
            sortOption = UserSortOptions.NAME_ASC,
            groupOption = null,
            ActiveFilters.ALL
        )
    )
    val userFilter: StateFlow<UserFilter> = _userFilter.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val productsUiState: StateFlow<ProductHomeUiState> = _productOrderType
        .flatMapLatest { sortOption ->
            productRepository.getAllProducts(sortOption)
                .map<List<ProductWithSupplierAndCategory>, ProductHomeUiState> { products ->
                    ProductHomeUiState.Success(products, sortOption)
                }
                .catch { e ->
                    emit(ProductHomeUiState.Error(e.message ?: "Error inesperado"))
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProductHomeUiState.Loading
        )

    init {
        // Cargar listas al iniciar el ViewModel
        /**
         * (hay que poner el init debajo de la declaración de la variable homeUiState
         * para que cuando se ejecute ya esté home inicializada, si no da un null pointer)
         */
        viewModelScope.launch {
            productsUiState
                .filterIsInstance<ProductHomeUiState.Success>()
                .collectLatest { loadLists() }
        }

        // Cargar historial de búsqueda al iniciar el ViewModel
//        viewModelScope.launch {
//            dataStoreManager.productSearchHistory.collect {
//                _productSearchHistory.value = it
//            }
//        }

//        viewModelScope.launch {
//            dataStoreManager.supplierSearchHistory.collect {
//                _supplierSearchHistory.value = it
//            }
//        }

        /* Manera más clean?
        dataStoreManager.searchHistory
            .onEach { _searchHistory.value = it }
            .launchIn(viewModelScope)

        homeUiState
            .filterIsInstance<HomeUiState.Success>()
            .onEach { loadLists() }
            .launchIn(viewModelScope)

         */
    }

//    private fun getUsers() {
//        viewModelScope.launch {
//            try {
//                val response = apiService.getUsers(
//                    sortBy = _userFilter.value.sortOption.sortBy ?: "name",
//                    direction = _userFilter.value.sortOption.direction ?: "asc",
//                    active = _userFilter.value.activeFilter.value
//                )
//
//                if (response.isSuccessful) {
//                    val users = response.body() ?: emptyList()
//                    _usersList.value = users.map { it.toUser() }
//                    Log.d("GET-ALL-USERS", "Recibidos: $users")
//                } else {
//                    Log.e("GET-ALL-USERS", "Error: ${response.code()}")
//                }
//            } catch (e: Exception) {
//                Log.e("USERS", "Fallo: ${e.message}", e)
//            }
//        }
//    }

    fun refreshUsers() {
        _isRefreshing.value = true
        //getUsers()
        _isRefreshing.value = false
    }


    // -------------------- VARIABLES - SEARCH BAR --------------------

    // Variables - Está buscando (Boolean)
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()


    // Variables - Texto escrito en el Search Bar (String)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Variables - Resultados de búsqueda de productos
    //private val _productSearchResults = MutableStateFlow<List<ProductWithSupplierAndCategory>>(emptyList())
    //val productSearchResults: StateFlow<List<ProductWithSupplierAndCategory>> = _productSearchResults.asStateFlow()


    // Variables - Resultados de búsqueda de proveedores
//    private val _supplierSearchResults = MutableStateFlow<List<SupplierEntity>>(emptyList())
//    val supplierSearchResults: StateFlow<List<SupplierEntity>> = _supplierSearchResults.asStateFlow()


    // Variables - Historial de búsqueda
//    private val _productSearchHistory = MutableStateFlow<List<String>>(emptyList())
//    val productSearchHistory: StateFlow<List<String>> = _productSearchHistory.asStateFlow()

//    private val _supplierSearchHistory = MutableStateFlow<List<String>>(emptyList())
//    val supplierSearchHistory: StateFlow<List<String>> = _supplierSearchHistory.asStateFlow()


    // -------------------- VARIABLES - CHECKBOX --------------------

    // Variables - Lista de productos seleccionados
//    private val _selectedProducts = MutableStateFlow<Set<String>>(emptySet()) // IDs seleccionados
//    val selectedProducts: StateFlow<Set<String>> = _selectedProducts.asStateFlow()


    // Variables - Lista de proveedores seleccionados
//    private val _selectedSuppliers = MutableStateFlow<Set<String>>(emptySet()) // IDs seleccionados
//    val selectedSuppliers: StateFlow<Set<String>> = _selectedSuppliers.asStateFlow()


    // -------------------- VARIABLES - LISTAS ELEMENTOS --------------------

    // Variables - Lista de elementos del Navigation Drawer
    private val _listOfNavDrawerItems = MutableStateFlow(emptyList<NavDrawerItem>())
    val listOfNavDrawerItems: StateFlow<List<NavDrawerItem>> = _listOfNavDrawerItems


    // Variables - Índice del elemento seleccionado en el Navigation Drawer
    private val _selectedHomeSection = MutableStateFlow(HomeSections.PRODUCTS)
    val selectedHomeSection: StateFlow<HomeSections> = _selectedHomeSection


    private val _showAboutDialog = MutableStateFlow(false)
    val showAboutDialog: StateFlow<Boolean> = _showAboutDialog


    private val _navigateSettings = MutableStateFlow(false)
    val navigateSettings: StateFlow<Boolean> = _navigateSettings

    // -------------------- FUNCIONES - OBTENCIÓN DE PRODUCTOS --------------------
    private val _productFoundByBarcode = MutableStateFlow<String?>(null)
    val productFoundByBarcode: StateFlow<String?> = _productFoundByBarcode.asStateFlow()

    private val _scanError = MutableStateFlow<String?>(null)
    val scanError: StateFlow<String?> = _scanError.asStateFlow()

    /**
     * Función - Obtener el ID de un producto por su código de barras
     * @param barcode - Código de barras del producto
     * @return - ID del producto
     */
    fun findProductByBarcode(barcode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = productRepository.getProductByBarcode(barcode)
            if (product != null) {
                _productFoundByBarcode.value = product.id
            } else {
                _scanError.value = "No se ha encontrado ningún código de barras"
            }
        }
    }

    fun clearProductFoundByBarcode() {
        _productFoundByBarcode.value = null
        _scanError.value = null
    }


    // -------------------- FUNCIONES - SEARCH BAR --------------------

    /**
     * Función - Cambiar el texto de búsqueda (Search Bar)
     * @param newQuery - Nuevo texto de búsqueda
     */
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }


    /**
     * Función - Borrar el texto de búsqueda (Search Bar)
     */
    fun onSearchQueryDelete() {
        _searchQuery.value = ""
    }


    /**
     * Función - Alternar el estado de búsqueda (isSearching)
     */
//    fun onToggleSearch() {
//        _isSearching.value = !_isSearching.value
//
//        if (_selectedProducts.value.isNotEmpty()) _selectedProducts.value = emptySet()
//
//        if (!_isSearching.value) {
//            _searchQuery.value = ""
//            _productSearchResults.value = emptyList()
//            _supplierSearchResults.value = emptyList()
//        }
//    }


    // -------------------- FUNCIONES - CHECKBOX --------------------



    // -------------------- FUNCIONES - LISTAS ELEMENTOS --------------------

    private fun loadLists() {
        //val uiState = homeUiState.value

        when (userRole.value) {
            UserRoles.ADMIN -> { _listOfNavDrawerItems.value = listOf(
                    NavDrawerItem.Item(
                        title = R.string.purchase_order_title,
                        iconSelected = R.drawable.ic_purchase_order_filled,
                        iconUnselected = R.drawable.ic_purchase_order,
                        action = { setSelectedItem(HomeSections.PURCHASE_ORDERS) }
                    ),
                    NavDrawerItem.Item(
                        title = R.string.product_title,
                        iconSelected = R.drawable.ic_product_filled,
                        iconUnselected = R.drawable.ic_product,
                        //count = if (uiState is HomeUiState.Success) uiState.productsList.size else 0,
                        action = { setSelectedItem(HomeSections.PRODUCTS) }
                    ),
                    NavDrawerItem.Item(
                        title = R.string.discount_title,
                        iconSelected = R.drawable.ic_discount_filled,
                        iconUnselected = R.drawable.ic_discount,
                        action = { setSelectedItem(HomeSections.DISCOUNTS) }
                    ),
                    NavDrawerItem.Item(
                        title = R.string.supplier_title,
                        iconSelected = R.drawable.ic_supplier_filled,
                        iconUnselected = R.drawable.ic_supplier,
                        //count = if (uiState is HomeUiState.Success) uiState.suppliersList.size else 0,
                        action = { setSelectedItem(HomeSections.SUPPLIERS) }
                    ),
                    NavDrawerItem.Item(
                        title = R.string.user_title,
                        iconSelected = R.drawable.ic_user_filled,
                        iconUnselected = R.drawable.ic_user,
                        action = { setSelectedItem(HomeSections.USERS) }
                    ),
                    NavDrawerItem.Item(
                        title = R.string.store_title,
                        iconSelected = R.drawable.ic_store_filled,
                        iconUnselected = R.drawable.ic_store,
                        action = { setSelectedItem(HomeSections.STORES) }
                    ),

                    NavDrawerItem.Divider,

                    NavDrawerItem.Item(
                        title = R.string.settings_title,
                        iconSelected = R.drawable.ic_settings_filled,
                        iconUnselected = R.drawable.ic_settings,
                        action = { setSelectedItem(HomeSections.SETTINGS) /*toggleNavigateSettings(true)*/ }
                    ),
                    NavDrawerItem.Item(
                        title = R.string.about_title,
                        iconSelected = R.drawable.ic_info_filled,
                        iconUnselected = R.drawable.ic_info,
                        action = { /*setSelectedItem(NavDrawerItemSelected.ABOUT) ;*/ toggleAboutDialog(true) }
                    )
                ) }
            UserRoles.GERENTE -> { _listOfNavDrawerItems.value = listOf(
                    NavDrawerItem.Item(
                        title = R.string.purchase_order_title,
                        iconSelected = R.drawable.ic_purchase_order_filled,
                        iconUnselected = R.drawable.ic_purchase_order,
                        action = { setSelectedItem(HomeSections.PURCHASE_ORDERS) }
                    ),

                    NavDrawerItem.Item(
                        title = R.string.user_title,
                        iconSelected = R.drawable.ic_user_filled,
                        iconUnselected = R.drawable.ic_user,
                        action = { setSelectedItem(HomeSections.USERS) }
                    ),

                    NavDrawerItem.Divider,

                    NavDrawerItem.Item(
                        title = R.string.settings_title,
                        iconSelected = R.drawable.ic_settings_filled,
                        iconUnselected = R.drawable.ic_settings,
                        action = { setSelectedItem(HomeSections.SETTINGS) /*toggleNavigateSettings(true)*/ }
                    ),
                    NavDrawerItem.Item(
                        title = R.string.about_title,
                        iconSelected = R.drawable.ic_info_filled,
                        iconUnselected = R.drawable.ic_info,
                        action = { /*setSelectedItem(NavDrawerItemSelected.ABOUT) ;*/ toggleAboutDialog(true) }
                    )
                ) }
            UserRoles.VENDEDOR -> { _listOfNavDrawerItems.value = listOf(
                    NavDrawerItem.Item(
                        title = R.string.product_title,
                        iconSelected = R.drawable.ic_product_filled,
                        iconUnselected = R.drawable.ic_product,
                        //count = if (uiState is HomeUiState.Success) uiState.productsList.size else 0,
                        action = { setSelectedItem(HomeSections.PRODUCTS) }
                    ),

                    NavDrawerItem.Divider,

                    NavDrawerItem.Item(
                        title = R.string.settings_title,
                        iconSelected = R.drawable.ic_settings_filled,
                        iconUnselected = R.drawable.ic_settings,
                        action = { setSelectedItem(HomeSections.SETTINGS) /*toggleNavigateSettings(true)*/ }
                    ),
                    NavDrawerItem.Item(
                        title = R.string.about_title,
                        iconSelected = R.drawable.ic_info_filled,
                        iconUnselected = R.drawable.ic_info,
                        action = { /*setSelectedItem(NavDrawerItemSelected.ABOUT) ;*/ toggleAboutDialog(true) }
                    )
                ) }
            UserRoles.DESCONOCIDO -> { _listOfNavDrawerItems.value = emptyList() }
        }
    }

     private fun setSelectedItem(section: HomeSections) {
        _selectedHomeSection.value = section
    }

    fun toggleAboutDialog(show: Boolean) {
        _showAboutDialog.value = show
    }

    fun toggleNavigateSettings(canNavigate: Boolean) {
        _navigateSettings.value = canNavigate
    }
}

