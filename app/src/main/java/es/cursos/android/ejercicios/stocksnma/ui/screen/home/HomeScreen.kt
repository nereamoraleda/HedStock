package es.cursos.android.ejercicios.stocksnma.ui.screen.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.ui.components.AboutAppDialog
import es.cursos.android.ejercicios.stocksnma.ui.components.AppNavigationDrawer
import es.cursos.android.ejercicios.stocksnma.ui.components.ConfirmationDialog
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomFAB
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomFABChild
import es.cursos.android.ejercicios.stocksnma.ui.components.DobleDropDownMenu
import es.cursos.android.ejercicios.stocksnma.ui.components.FABContainer
import es.cursos.android.ejercicios.stocksnma.ui.components.FilterDropDownMenu
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralIconButton
import es.cursos.android.ejercicios.stocksnma.ui.components.HomeTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.products.ProductSectionScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.products.ProductSectionViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.suppliers.SupplierSectionScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.suppliers.SupplierSectionViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.users.UserSectionScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.users.UserSectionViewModel
import es.cursos.android.ejercicios.stocksnma.utils.PortraitCaptureActivity
import es.cursos.android.ejercicios.stocksnma.utils.enums.HomeSections
import es.cursos.android.ejercicios.stocksnma.utils.enums.ProductSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserGroupOptions
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserSortOptions
import es.cursos.android.ejercicios.stocksnma.utils.items.DropDownMenuItem
import es.cursos.android.ejercicios.stocksnma.utils.items.FABItem
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateToSettings: () -> Unit,
    navigateToProductCreation: (String?) -> Unit,
    navigateToSupplierCreation: () -> Unit,
    navigateToProductDetails: (String) -> Unit,
    navigateToSupplierDetails: (String) -> Unit,
    navigateToUserCreation: () -> Unit,
    navigateToUserDetails: (Long) -> Unit,
) {

    // --- VARIABLES ---
    val context = LocalContext.current // Para Logs

    val productViewModel: ProductSectionViewModel = hiltViewModel()
    val supplierViewModel: SupplierSectionViewModel = hiltViewModel()
    val userViewModel: UserSectionViewModel = hiltViewModel()

    val isSearchingProducts by productViewModel.isSearching.collectAsState()
    val isSearchingSuppliers by supplierViewModel.isSearching.collectAsState()
    val isSearchingUsers by userViewModel.isSearching.collectAsState()

    //val state by viewModel.homeUiState.collectAsState()                          // Estado de la pantalla (Loading, Success, Error)
    val navigateSettings by viewModel.navigateSettings.collectAsState()          // Navegación a la pantalla de configuración
    val snackbarHostState = remember { SnackbarHostState() }
    val userRole by viewModel.userRole.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    //Log.i("Nerea", "UserRole: $userRole")

    // Variables - Dialog
    val showAboutDialog by viewModel.showAboutDialog.collectAsState()               // Muestra el diálogo "Acerca de" (Elemento del Navigation Drawer)
    var showDialogScanner by remember { mutableStateOf(false) }               // Muestra el diálogo de creacion de producto con el código de barras escaneado
    // Muestra el diálogo de confirmación de eliminación de productos/proveedores
    // Muestra el diálogo de confirmación de eliminación de productos/proveedores
    var showNewFunction by remember { mutableStateOf(false) }

    // Variables - Navigation Drawer
    val navDrawerOptions by viewModel.listOfNavDrawerItems.collectAsState()  // Lista de elementos del Navigation Drawer
    val selectedHomeSection by viewModel.selectedHomeSection.collectAsState()             // Índice del elemento seleccionado
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)     // Estado del Navigation Drawer
    val scope = rememberCoroutineScope()


    // Variables - Búsqueda en Search Bar
    //val isSearching by viewModel.isSearching.collectAsState()                     // Si estamos en búsqueda o no
    val searchQuery by viewModel.searchQuery.collectAsState()                     // Consulta de búsqueda
    // Historial de búsquedas
    // Historial de búsquedas
    // Lista de resultados de búsqueda de productos
    // Lista de resultados de búsqueda de proveedores


    // Variables - CheckBoxes
    // Lista de productos seleccionados (solo Ids)
    // Lista de proveedores seleccionados (solo Ids)


    // Variables - Floating Action Button
    val fabOptions = listOf(
        FABItem(
            title = R.string.add_purchase_order,
            icon = R.drawable.ic_purchase_order_add,
            action = { showNewFunction = true }
        ),

        FABItem(
            title = R.string.add_product,
            icon = R.drawable.ic_product_add,
            action = { navigateToProductCreation(null) }
        ),

        FABItem(
            title = R.string.add_supplier,
            icon = R.drawable.ic_supplier_add,
            action = { navigateToSupplierCreation() }
        )
    )                                              // Lista de elementos del Floating Action Button
    var isButtonsAddVisible by remember { mutableStateOf(false) }           // Si se muestran o no los botones de añadir en el Floating Action Button

    val transition = updateTransition(targetState = isButtonsAddVisible, label = "")
    val rotation by transition.animateFloat(label = "") { if (it) 315f else 0f }
    val alpha = if (isButtonsAddVisible) 0.3f else 1f


    // Variables - DropDownMenu
    var isMenuExpanded by remember { mutableStateOf(false) }                // Si se muestra o no el menú desplegable

    val productSortOption by productViewModel.productSortOption.collectAsState()          // Ordenación de productos
    val supplierSortOption by supplierViewModel.supplierSortOption.collectAsState()        // Ordenación de proveedores
    val userSortOption by userViewModel.userFilter.collectAsState()

    val productSortMenuItems = listOf(
        DropDownMenuItem(
            title = R.string.order_by_name_asc,
            selected = productSortOption == ProductSortOptions.NAME_ASC,
            action = { productViewModel.setProductSortOption(ProductSortOptions.NAME_ASC) }
        ),

        DropDownMenuItem(
            title = R.string.order_by_name_desc,
            selected = productSortOption == ProductSortOptions.NAME_DESC,
            action = { productViewModel.setProductSortOption(ProductSortOptions.NAME_DESC) }
        ),

        DropDownMenuItem(
            title = R.string.group_by_categories,
            selected = productSortOption == ProductSortOptions.CATEGORY,
            action = { productViewModel.setProductSortOption(ProductSortOptions.CATEGORY) }
        )
    )    // Lista de elementos del menú desplegable de productos
    val supplierSortMenuItems = listOf(
        DropDownMenuItem(
            title = R.string.order_by_name_asc,
            selected = supplierSortOption == SupplierSortOptions.NAME_ASC,
            action = { supplierViewModel.setSupplierSortOption(SupplierSortOptions.NAME_ASC) }
        ),

        DropDownMenuItem(
            title = R.string.order_by_name_desc,
            selected = supplierSortOption == SupplierSortOptions.NAME_DESC,
            action = { supplierViewModel.setSupplierSortOption(SupplierSortOptions.NAME_DESC) }
        ),
    )   // Lista de elementos del menú desplegable de proveedores
    val userSortMenuItems = listOf(
        DropDownMenuItem(
            title = R.string.order_by_name_asc,
            selected = userSortOption.sortOption == UserSortOptions.NAME_ASC,
            action = { userViewModel.setUserSortOption(UserSortOptions.NAME_ASC) }
        ),

        DropDownMenuItem(
            title = R.string.order_by_name_desc,
            selected = userSortOption.sortOption == UserSortOptions.NAME_DESC,
            action = { userViewModel.setUserSortOption(UserSortOptions.NAME_DESC) }
        ),

        DropDownMenuItem(
            title = R.string.sort_by_date_oldest,
            selected = userSortOption.sortOption == UserSortOptions.DATE_OLDEST,
            action = { userViewModel.setUserSortOption(UserSortOptions.DATE_OLDEST) }
        ),

        DropDownMenuItem(
            title = R.string.sort_by_date_newest,
            selected = userSortOption.sortOption == UserSortOptions.DATE_NEWEST,
            action = { userViewModel.setUserSortOption(UserSortOptions.DATE_NEWEST) }
        )
    )       // Lista de elementos del menú desplegable de usuarios
    val userGroupMenuItems = listOf(
        DropDownMenuItem(
            title = R.string.group_by_role,
            selected = userSortOption.groupOption == UserGroupOptions.ROLE,
            action = { userViewModel.setUserGroupOption(UserGroupOptions.ROLE) }
        ),
        DropDownMenuItem(
            title = R.string.group_by_store,
            selected = userSortOption.groupOption == UserGroupOptions.STORE,
            action = { userViewModel.setUserGroupOption(UserGroupOptions.STORE) }
        )
    )


    // Variables - Barcode Scanner
    var barcodeScanner by remember { mutableStateOf("") }                  // Código de barras escaneado
    val productFound by viewModel.productFoundByBarcode.collectAsState()        // ID del producto encontrado con el código de barras escaneado
    val scanError by viewModel.scanError.collectAsState()                       // Error del escaneo del código de barras

    val barcodeLauncher =
        rememberLauncherForActivityResult(ScanContract()) { result -> // En este punto obtenemos información del escaneo como el formato del código de barras, el raw bytes, el contenido (el dato que queremos)...
            result.contents?.let { scannedBarcode ->
                barcodeScanner = scannedBarcode
                viewModel.findProductByBarcode(scannedBarcode)
                //Toast.makeText(context, "Código de barras escaneado: $scannedBarcode", Toast.LENGTH_SHORT).show()
                //Log.i("Nerea", "Código de barras escaneado: $scannedBarcode")
            }
        }


    // Se lanza si se ha encontrado un producto con el código de barras escaneado
    LaunchedEffect(productFound) {
        productFound?.let {
            navigateToProductDetails(it)
            viewModel.clearProductFoundByBarcode()
        }
    }


    // Se lanza si no se ha encontrado un producto con el código de barras escaneado
    LaunchedEffect(scanError) {
        scanError?.let {
            if (barcodeScanner.isNotEmpty()) showDialogScanner = true
            //Toast.makeText(context, scanError, Toast.LENGTH_SHORT).show()
        }
    }


    // -------------------- DIALOGS --------------------
    if (showNewFunction) {
        Dialog(onDismissRequest = { showNewFunction = false }) {
            Text(text = "Próximamente...")
        }
    }


    // Si hemos escaneado un código de barras que no está en la base de datos, muestra el diálogo
    if (showDialogScanner) {
        ConfirmationDialog(
            title = stringResource(R.string.confirm_create_new_product_with_barcode_title),
            message = stringResource(
                R.string.confirm_create_new_product_with_barcode,
                barcodeScanner
            ),
            confirmButtonText = stringResource(R.string.button_create),
            onDismissRequest = { showDialogScanner = false },
            onConfirmAction = {
                showDialogScanner = false
                navigateToProductCreation(barcodeScanner)
                viewModel.clearProductFoundByBarcode()
            }
        )
    }

    // Si hemos seleccionado en el Navigation Drawer Acerca de, muestra el diálogo
    if (showAboutDialog) {
        AboutAppDialog(onDismissRequest = { viewModel.toggleAboutDialog(false) })
    }


    // Si hemos seleccionado algún producto y hemos pulsado el botón Borrar/Papelera en el Bottom Bar, muestra el diálogo
//    if (showDeleteProductsConfirmation) {
//        ConfirmationDialog(
//            title = stringResource(R.string.confirm_delete_selected_products_title),
//            message = stringResource(R.string.confirm_delete_selected_products),
//            confirmButtonText = stringResource(R.string.button_text_accept),
//            onDismissRequest = { showDeleteProductsConfirmation = false; viewModel.unselectAllProducts() },
//            onConfirmAction = {
//                viewModel.deleteSelectedProducts()
//                scope.launch {
//                    snackbarHostState.showSnackbar(
//                        message = "Los productos se han elminado",
//                        withDismissAction = true,
//                        duration = SnackbarDuration.Short
//                    )
//                }
//                showDeleteProductsConfirmation = false
//            }
//        )
//    }
//
//
//    // Si hemos seleccionado algún proveedor y hemos pulsado el botón Borrar/Papelera en el Bottom Bar, muestra el diálogo
//    if (showDeleteSuppliersConfirmation) {
//        ConfirmationDialog(
//            title = stringResource(R.string.confirm_delete_selected_suppliers_title),
//            message = stringResource(R.string.confirm_delete_selected_suppliers),
//            confirmButtonText = stringResource(R.string.button_text_accept),
//            onDismissRequest = { showDeleteSuppliersConfirmation = false; viewModel.unselectAllSuppliers() },
//            onConfirmAction = {
//                viewModel.deleteSelectedSuppliers()
//                scope.launch {
//                    snackbarHostState.showSnackbar(
//                        message = "Los proveedores se han elminado",
//                        withDismissAction = true,
//                        duration = SnackbarDuration.Short
//                    )
//                }
//                showDeleteSuppliersConfirmation = false
//            }
//        )
//    }

    if (navigateSettings) {
        navigateToSettings()
        viewModel.toggleNavigateSettings(false)
    }

    val isSearching = isSearchingProducts || isSearchingSuppliers || isSearchingUsers

    fun toggleDrawer() {
        scope.launch {
            drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }

    fun launchBarcodeScanner() {
        val options = ScanOptions().apply {
            setCaptureActivity(PortraitCaptureActivity::class.java)
            setOrientationLocked(true)
            setPrompt("Escanea un código de barras")
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
        }
        barcodeLauncher.launch(options)
    }

// -------------------- UI --------------------
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppNavigationDrawer(
                navDrawerOptions,
                selectedHomeSection,
                drawerState,
                scope,
            )
        }
    ) {
        Scaffold(
            // Dependiendo de si estamos en búsqueda o no, cambiamos el alcance de la barra de navegación
            contentWindowInsets = if (isSearching) WindowInsets(0.dp) else WindowInsets.systemBars,


            // --- SCAFFOLD - TOP BAR ---
            topBar = {
                when (selectedHomeSection) {
                    HomeSections.PRODUCTS -> {
                        HomeTopAppBar(
                            section = selectedHomeSection,
                            isSearching = isSearchingProducts,
                            onNavClick = { toggleDrawer() },
                            actionButton = {
                                GeneralIconButton(
                                    onClick = { launchBarcodeScanner() },
                                    icon = R.drawable.ic_barcode_scanner,
                                )

                                FilterDropDownMenu(
                                    isMenuExpanded = isMenuExpanded,
                                    filterOptions = productSortMenuItems,
                                    onClickIconButton = { isMenuExpanded = !isMenuExpanded },
                                    onDismissRequest = { isMenuExpanded = false },
                                    itemSelected = productSortOption.name
                                )
                            },
                            modifier = Modifier.alpha(alpha)
                        )
                    }
                    HomeSections.SUPPLIERS -> {
                        HomeTopAppBar(
                            section = selectedHomeSection,
                            isSearching = isSearchingSuppliers,
                            onNavClick = { toggleDrawer() },
                            actionButton = {
                                FilterDropDownMenu(
                                    isMenuExpanded = isMenuExpanded,
                                    filterOptions = supplierSortMenuItems,
                                    onClickIconButton = {
                                        isMenuExpanded = !isMenuExpanded
                                    },
                                    onDismissRequest = { isMenuExpanded = false },
                                    itemSelected = supplierSortOption.name
                                )
                            },
                            modifier = Modifier.alpha(alpha)
                        )
                    }
                    HomeSections.USERS -> {
                        HomeTopAppBar(
                            section = selectedHomeSection,
                            isSearching = isSearchingUsers,
                            onNavClick = { toggleDrawer() },
                            actionButton = {
                                DobleDropDownMenu(
                                    sortOptions = userSortMenuItems,
                                    groupOptions = userGroupMenuItems,
                                    isMenuExpanded = isMenuExpanded,
                                    onClickIconButton = { isMenuExpanded = !isMenuExpanded },
                                    onDismissRequest = { isMenuExpanded = false }
                                )
                            },
                            modifier = Modifier.alpha(alpha)
                        )
                    }
                    else -> {
                        HomeTopAppBar(
                            section = selectedHomeSection,
                            isSearching = false,
                            onNavClick = { toggleDrawer() },
                            actionButton = {
                                FilterDropDownMenu(
                                    isMenuExpanded = isMenuExpanded,
                                    filterOptions = listOf(),
                                    onClickIconButton = { isMenuExpanded = !isMenuExpanded },
                                    onDismissRequest = { isMenuExpanded = false },
                                    itemSelected = ""
                                )
                            },
                            modifier = Modifier.alpha(alpha)
                        )
                    }
                }
            },

            // --- SCAFFOLD - BOTTOM BAR ---
            bottomBar = {
                when (selectedHomeSection) {
                    HomeSections.PRODUCTS -> {}
//                                HomeBottomBar(
//                                textCountSingular = R.string.selected_products_singular,
//                                textCountPlural = R.string.selected_product_plural,
//                                selectedCheckBoxCount = selectedProducts.size,
//                                onDeleteSelected = { showDeleteProductsConfirmation = true },
//                                modifier = Modifier.alpha(alpha)
//                            )

                    HomeSections.SUPPLIERS -> {}
//                                HomeBottomBar(
//                                textCountSingular = R.string.selected_suppliers_singular,
//                                textCountPlural = R.string.selected_supplier_plural,
//                                selectedCheckBoxCount = selectedSuppliers.size,
//                                onDeleteSelected = { showDeleteSuppliersConfirmation = true },
//                                modifier = Modifier.alpha(alpha)
//                            )

                    else -> {}
                }
            },


            // --- SCAFFOLD - FLOATING ACTION BUTTON ---
            floatingActionButton = {
                when (selectedHomeSection) {
                    HomeSections.PRODUCTS -> {
                        FABContainer(
                            isVisible = !isSearchingProducts,
                            isExpanded = isButtonsAddVisible,
                            onDismiss = { isButtonsAddVisible = false }
                        ) {
                            fabOptions.forEach { item ->
                                CustomFABChild(
                                    fab = item,
                                    isExpanded = isButtonsAddVisible,
                                    onStateChanged = { isButtonsAddVisible = !isButtonsAddVisible }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            CustomFAB(
                                action = { isButtonsAddVisible = !isButtonsAddVisible },
                                modifier = Modifier.rotate(rotation)
                            )
                        }
                    }
                    HomeSections.SUPPLIERS -> {
                        FABContainer(
                            isVisible = !isSearchingSuppliers,
                            isExpanded = isButtonsAddVisible,
                            onDismiss = { isButtonsAddVisible = false }
                        ) {
                            fabOptions.forEach { item ->
                                CustomFABChild(
                                    fab = item,
                                    isExpanded = isButtonsAddVisible,
                                    onStateChanged = { isButtonsAddVisible = !isButtonsAddVisible }
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            CustomFAB(
                                action = { isButtonsAddVisible = !isButtonsAddVisible },
                                modifier = Modifier.rotate(rotation)
                            )
                        }
                    }
                    HomeSections.USERS -> {
                        FABContainer(
                            isVisible = !isSearchingUsers,
                            isExpanded = isButtonsAddVisible,
                            onDismiss = { isButtonsAddVisible = false }
                        ) {
                            FloatingActionButton(
                                onClick = { navigateToUserCreation() },
                                containerColor = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                                elevation = FloatingActionButtonDefaults.elevation(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_user_add),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    else -> {}
                }
            },

//      HomeSections.DISCOUNTS -> {
//          FloatingActionButton(
//              onClick = { showNewFunction = true },
//              containerColor = MaterialTheme.colorScheme.primary,
//              shape = CircleShape,
//              elevation = FloatingActionButtonDefaults.elevation(8.dp)
//          ) {
//              Icon(
//                  painter = painterResource(R.drawable.ic_discount),
//                  contentDescription = null
//              )
//          }
//      }

            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier.fillMaxSize()

        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .alpha(if (isButtonsAddVisible) 0.3f else 1f)
            ) {
                when (selectedHomeSection) {
                    HomeSections.PRODUCTS -> {
                        //val productViewModel: ProductViewModel = hiltViewModel()
                        //val uiState by productViewModel.productsUiState.collectAsState()
                        ProductSectionScreen { navigateToProductDetails(it) }
                    }

                    HomeSections.SUPPLIERS -> {
                        //val supplierViewModel: SupplierViewModel = hiltViewModel()
                        //val uiState by supplierViewModel.suppliersUiState.collectAsState()
                        SupplierSectionScreen { navigateToSupplierDetails(it) }
                    }

                    HomeSections.USERS -> {
                        //val userViewModel: UserViewModel = hiltViewModel()
                        //val uiState by userViewModel.usersUiState.collectAsState()
                        UserSectionScreen {

                            navigateToUserDetails(it)
                        }
                    }

                    else -> Text("Sección en construcción")
                }

//                        GeneralSearchBar(
//                            query = searchQuery,
//                            onQueryChange = {
//                                viewModel.onSearchQueryChange(it)
//                                if (selectedHomeSection == HomeSection.PRODUCTS) viewModel.searchProductByName(it)
//                                else viewModel.searchSupplierByName(it)
//                            },
//                            active = isSearching,
//                            onActiveChange = { viewModel.onToggleSearch() },
//                            placeholderText = when (selectedHomeSection) {
//                                HomeSection.PRODUCTS -> R.string.search_product
//                                HomeSection.SUPPLIERS -> R.string.search_supplier
//                                else -> R.string.no_available
//                            },
//                            cleanQuery = { viewModel.onSearchQueryDelete() }
//                        ) {

//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(MaterialTheme.colorScheme.background)
//                        ) {
//                            // Maneja la búsqueda en función de la opción seleccionada
//                            when (selectedHomeSection) {
//                                HomeSection.PRODUCTS -> {}
//                                HomeSection.SUPPLIERS -> {}
//                                else -> {}
//                            }
//                        }
//                    }
//

//
//                    Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
//
//
//                    // Maneja la lista mostrada en función de la opción seleccionada
//                    when (selectedHomeSection) {
//                        HomeSection.PRODUCTS -> {}
//                        HomeSection.SUPPLIERS -> {}
//
//                        HomeSection.USERS -> {
////                                SwipeRefresh(
////                                    state = rememberSwipeRefreshState(isRefreshing = false),
////                                    onRefresh = { viewModel.refreshUsers() }
////                                ) {
////                                    UserSectionHomeBodyScreen(
////                                        users = users,
////                                        navigateToUserDetails = navigateToUserDetails
////                                    )
////                                }
////                                Log.i("HOME SCREEN - USERS", "Users: $users")
//                            }
////
//                           else -> {
//                               Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
//                                    Text(text = "Próximamente...")
//                                }
//                            }
//                        }
//                    }
//                }
//      } }
            }
        }
    }
}


@Composable
fun HomeLoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun HomeErrorScreen(messageError: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(text = messageError)
    }
}