package es.cursos.android.ejercicios.stocksnma.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.HomeScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.product.ProductCreationScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.product.ProductoDetailsScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.supplier.SupplierDetailsScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.supplier.SupplierCreationScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.home.HomeViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.login.LoginScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.login.LoginViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.product.ProductCreationViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.product.ProductDetailsViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.settings.SettingsScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.supplier.SupplierDetailsViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.supplier.SupplierCreationViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.user.UserCreationScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.user.UserDetailsScreen
import es.cursos.android.ejercicios.stocksnma.ui.screen.user.UserDetailsViewModel
import es.cursos.android.ejercicios.stocksnma.ui.screen.user.UserCreationViewModel

@Composable
fun NavigationWrapper() {
    // -- VARIABLES --
    val navController = rememberNavController()

    // Variables - ViewModels
    val loginViewModel: LoginViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

    val productDetailsViewModel: ProductDetailsViewModel = hiltViewModel()
    val supplierDetailsViewModel: SupplierDetailsViewModel = hiltViewModel()
    val userDetailsViewModel: UserDetailsViewModel = hiltViewModel()

    val productCreationViewModel: ProductCreationViewModel = hiltViewModel()
    val supplierCreationViewModel: SupplierCreationViewModel = hiltViewModel()
    val userCreationViewModel: UserCreationViewModel = hiltViewModel()


//    @Composable
//    fun HedStockNavGraph(navController: NavHostController, splashViewModel: SplashViewModel) {
//        val startDestination by splashViewModel.startDestination.collectAsState()
//
//        if (startDestination != null) {
//            NavHost(
//                navController = navController,
//                startDestination = startDestination!!
//            ) {
//                composable(Routes.LOGIN) { LoginScreen(navController) }
//                composable(Routes.HOME) { HomeScreen(navController) }
//                // otras pantallas...
//            }
//        } else {
//            // Pantalla vacía mientras se decide la ruta inicial
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//    }


    /**
     * NAV HOST - Navegación entre pantallas
     * StartDestination - Pantalla Home
     */
    NavHost(navController = navController, startDestination = Login) {

        /**
         * Pantalla Login
         */
        composable<Login> {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { navController.navigate(Home) { popUpTo(Login) { inclusive = true } } }
            )
        }

        /**
         * Pantalla Inicial - Envía el id del item seleccionado de la lista actualmente visible a la pantalla de detalles
         * Muestra una tabla con la información de todos los productos o proveedores
         * Puede navegar a la pantalla de crear un nuevo producto o proveedor
         * Puede navegar a la pantalla de detalles de un producto o proveedor
         */
        composable<Home> {
            HomeScreen(
                viewModel = homeViewModel,
                navigateToSettings = { navController.navigate(Settings) },
                navigateToProductCreation = { navController.navigate(ProductCreation(barcodeScanner = it)) },
                navigateToSupplierCreation = { navController.navigate(SupplierCreation) },
                navigateToProductDetails = { navController.navigate(ProductDetails(idProduct = it)) },
                navigateToSupplierDetails = { navController.navigate(SupplierDetails(idSupplier = it)) },
                navigateToUserCreation = { navController.navigate(UserCreation) },
                navigateToUserDetails = { navController.navigate(UserDetails(idUser = it)) }
            )
        }


        /**
         * Pantalla Creación Producto
         * Muestra un formulario para crear un nuevo producto
         */
        composable<ProductCreation> { backStackEntry ->
            val barcodeScanner: ProductCreation = backStackEntry.toRoute()

            ProductCreationScreen(
                viewModel = productCreationViewModel,
                barcodeScanner = barcodeScanner.barcodeScanner,
                onProductCreated = { navController.navigate(Home) { popUpTo(Home) { inclusive = true } } },
                navigateBack = { navController.popBackStack() },
            )
        }


        /**
         * Pantalla Creación Proveedor
         * Muestra un formulario para crear un nuevo proveedor
         */
        composable<SupplierCreation> {
            SupplierCreationScreen(
                viewModel = supplierCreationViewModel,
                onSupplierCreated = { navController.navigate(Home) { popUpTo(Home) { inclusive = true } } },
                navigateBack = { navController.popBackStack() }
            )
        }


        /**
         * Pantalla Detalles Productos - Obtiene el id pasado del producto seleccionado
         * Muestra toda la información del producto seleccionado
         */
        composable<ProductDetails> { backStackEntry ->
            val idProduct: ProductDetails = backStackEntry.toRoute()

            ProductoDetailsScreen(
                viewModel = productDetailsViewModel,
                idProduct = idProduct.idProduct,
                navigateBack = { navController.popBackStack() }
            )
        }


        /**
         * Pantalla Detalles Proveedores - Obtiene el id pasado del proveedor seleccionado
         * Muestra toda la información del proveedor seleccionado
         */
        composable<SupplierDetails> { backStackEntry ->
            val idSupplier: SupplierDetails = backStackEntry.toRoute()

            SupplierDetailsScreen(
                viewModel = supplierDetailsViewModel,
                idSupplier = idSupplier.idSupplier,
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<UserCreation> {
            UserCreationScreen(
                viewModel = userCreationViewModel,
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<UserDetails> { backStackEntry ->
            val idUser: UserDetails = backStackEntry.toRoute()

            UserDetailsScreen(
                viewModel = userDetailsViewModel,
                userId = idUser.idUser,
                onNavigateBack = { navController.popBackStack() }
            )
        }


        /**
         * Pantalla Configuración
         */
        composable<Settings> {
            SettingsScreen(navigateBack = { navController.popBackStack() } )
        }
    }
}
