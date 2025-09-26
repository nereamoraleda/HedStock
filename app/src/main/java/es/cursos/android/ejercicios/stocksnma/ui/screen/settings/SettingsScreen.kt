package es.cursos.android.ejercicios.stocksnma.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.IconButtonGoBack


@Composable
fun SettingsScreen(navigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.settings_title),
                navigationButton = { IconButtonGoBack(navigateBack) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "Configuración",
                style = MaterialTheme.typography.bodyLarge
            )
            // Aquí puedes agregar más opciones de configuración, como un Switch, botones, etc.
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = { /* acción de cerrar sesión u otra acción */ }) {
                Text("Cerrar sesión")
            }
        }
    }
}
