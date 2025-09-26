package es.cursos.android.ejercicios.stocksnma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import es.cursos.android.ejercicios.stocksnma.ui.navigation.NavigationWrapper
import es.cursos.android.ejercicios.stocksnma.ui.theme.StocksNMATheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen() // Instalar SplashScreen
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        screenSplash.setKeepOnScreenCondition { false }
        // Si estamos cargando los datos, mantener la pantalla de splash
//        val homeViewModel: HomeViewModel by viewModels()
//        screenSplash.setKeepOnScreenCondition {
//            homeViewModel.homeUiState.value is HomeUiState.Loading
//        }

        setContent {
            StocksNMATheme {
                NavigationWrapper()
            }
        }
    }
}
