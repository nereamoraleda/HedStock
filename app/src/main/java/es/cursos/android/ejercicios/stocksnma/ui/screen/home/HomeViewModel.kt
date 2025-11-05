package es.cursos.android.ejercicios.stocksnma.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.local.datastore.AppDataStore
import es.cursos.android.ejercicios.stocksnma.data.local.entity.relations.ProductWithSupplierAndCategory
import es.cursos.android.ejercicios.stocksnma.data.repository.product.ProductRepository
import es.cursos.android.ejercicios.stocksnma.utils.enums.HomeSections
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles
import es.cursos.android.ejercicios.stocksnma.utils.items.NavDrawerItem
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
    private val dataStoreManager: AppDataStore,
) : ViewModel() {
    //dataStoreManager.session.userRole -> No funciona ya que SessionPreferences no tiene métodos ni se usan donde corresponde (SessionManager)

    init {
        loadHomeData()
    }


    // -------------------- HOME UI STATE -------------------- //
    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private val _userRole = MutableStateFlow(UserRoles.DESCONOCIDO)
    //val userRole: StateFlow<UserRoles> = _userRole.asStateFlow()

    private val _hasPermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = _hasPermission.asStateFlow()

    // Variables - Lista de elementos del Navigation Drawer
    private val _listOfNavDrawerItems = MutableStateFlow(emptyList<NavDrawerItem>())


    // -------------------- CARGAR DATOS PARA LA HOME UI -------------------- //
    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                dataStoreManager.userRole.collectLatest { role ->

                    /**
                     * Según el rol obtenido en el Login se mostrará un listado u otro en el
                     * NavigationDrawer, además de restringir acciones/permisos
                     */
                    _userRole.value = role
                    _hasPermission.value = _userRole.value == UserRoles.ADMIN
                    loadLists(role)

                    _homeUiState.value = HomeUiState.Success(
                        userRole = role,
                        navDrawerSections = _listOfNavDrawerItems.value,
                        selectedSection = HomeSections.SUPPLIERS
                    )
                }

            } catch (e: Exception) {
                _homeUiState.value = HomeUiState.Error(e.message ?: "Error inesperado")
            }
        }
    }


    // -------------------- VARIABLES - OBTENCIÓN DE PRODUCTOS --------------------

    // Variables - Tipos de orden de la tabla Products
    private val _productOrderType = dataStoreManager.sort.productSortBy.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProductSortOptions.NAME_ASC
    )
    //private val _productOrderType = MutableStateFlow(ProductSortOptions.NAME_ASC)
    //val productOrderTypeProducts: StateFlow<ProductSortOptions> = _productOrderType.asStateFlow()


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
            productsUiState.filterIsInstance<ProductHomeUiState.Success>()
        }

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








    // -------------------- VARIABLES - LISTAS ELEMENTOS --------------------
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
    /**
     * Función - Borrar el texto de búsqueda (Search Bar)
     */
    /**
     * Función - Alternar el estado de búsqueda (isSearching)
     */


    // -------------------- FUNCIONES - LISTAS ELEMENTOS --------------------

    private fun loadLists(role: UserRoles = _userRole.value) {
        _listOfNavDrawerItems.value = when (role) {
            UserRoles.ADMIN -> listOf(
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
                    action = { toggleAboutDialog(true) }
                )
            )

            UserRoles.GERENTE -> listOf(
                NavDrawerItem.Item(
                    title = R.string.purchase_order_title,
                    iconSelected = R.drawable.ic_purchase_order_filled,
                    iconUnselected = R.drawable.ic_purchase_order,
                    action = { setSelectedItem(HomeSections.PURCHASE_ORDERS) }
                ),

                NavDrawerItem.Item(
                    title = R.string.supplier_title,
                    iconSelected = R.drawable.ic_supplier_filled,
                    iconUnselected = R.drawable.ic_supplier,
                    //count = if (uiState is HomeUiState.Success) uiState.suppliersList.size else 0,
                    action = { setSelectedItem(HomeSections.SUPPLIERS) }
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
                    action = { /*setSelectedItem(NavDrawerItemSelected.ABOUT) ;*/ toggleAboutDialog(
                        true
                    )
                    }
                )
            )

            UserRoles.VENDEDOR -> listOf(
                NavDrawerItem.Item(
                    title = R.string.product_title,
                    iconSelected = R.drawable.ic_product_filled,
                    iconUnselected = R.drawable.ic_product,
                    //count = if (uiState is HomeUiState.Success) uiState.productsList.size else 0,
                    action = { setSelectedItem(HomeSections.PRODUCTS) }
                ),

                NavDrawerItem.Item(
                    title = R.string.supplier_title,
                    iconSelected = R.drawable.ic_supplier_filled,
                    iconUnselected = R.drawable.ic_supplier,
                    //count = if (uiState is HomeUiState.Success) uiState.suppliersList.size else 0,
                    action = { setSelectedItem(HomeSections.SUPPLIERS) }
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
                    action = { /*setSelectedItem(NavDrawerItemSelected.ABOUT) ;*/ toggleAboutDialog(true)
                    }
                )
            )

            UserRoles.DESCONOCIDO -> emptyList()
        }
    }

    private fun setSelectedItem(section: HomeSections) {
        _homeUiState.value = (_homeUiState.value as HomeUiState.Success).copy(
            selectedSection = section
        )
    }

    fun toggleAboutDialog(show: Boolean) {
        _showAboutDialog.value = show
    }

    fun toggleNavigateSettings(canNavigate: Boolean) {
        _navigateSettings.value = canNavigate
    }
}

