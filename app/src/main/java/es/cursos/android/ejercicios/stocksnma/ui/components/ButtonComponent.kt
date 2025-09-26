package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import es.cursos.android.ejercicios.stocksnma.R

/**
 * COMPOSABLE - BOTÓN ACEPTAR/GUARDAR
 *
 * @param action Función a ejecutar al hacer clic en el botón (aceptar/guardar)
 * @param enabled Estado del botón (activado o desactivado)
 */
@Composable
fun ButtonAccept(
    textButton: String = stringResource(R.string.button_accept),
    action: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { action() },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = modifier
    ) {
        Text(text = textButton, fontWeight = FontWeight.Bold)
    }
}


/**
 * COMPOSABLE - BOTÓN CANCELAR
 *
 * @param action Función a ejecutar al hacer clic en el botón (cancelar)
 */
@Composable
fun ButtonCancel(
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        onClick = { action() },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.button_cancel), fontWeight = FontWeight.Bold)
    }
}

