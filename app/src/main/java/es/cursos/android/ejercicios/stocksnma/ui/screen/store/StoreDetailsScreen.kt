package es.cursos.android.ejercicios.stocksnma.ui.screen.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton

@Composable
fun StoreDetailsScreen(
    storeId: Long,
    navigateBack: () -> Unit
) {
    val viewModel: StoreDetailsViewModel = hiltViewModel()
    val store by viewModel.store.collectAsState()

    LaunchedEffect(storeId) {
        viewModel.getDataStore(storeId)
    }

    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = "Detalles de la tienda",
                navigationButton = { NavigateBackButton(onNavigateBack = { navigateBack() } ) }
            )
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            StoreDetailsBodyScreen(store)
        }
    }
}


@Composable
fun StoreDetailsBodyScreen(
    store: Store
) {
    Column(
        //modifier = Modifier.fillMaxSize()
    ) {
        Text(
            store.name
        )
    }
}