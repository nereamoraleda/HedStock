package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.cursos.android.ejercicios.stocksnma.R


@Composable
fun RowTextButtons(
    confirmButtonText: String,
    cancelButtonText: String = stringResource(R.string.button_cancel),
    onAcceptAction: () -> Unit,
    onCancelAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(onClick = onCancelAction) {
            Text(
                text = cancelButtonText,
                style = MaterialTheme.typography.labelLarge
            )
        }
        TextButton(onClick = {
                onCancelAction()
                onAcceptAction()
        }) {
            Text(
                text = confirmButtonText,
                style = MaterialTheme.typography.labelLarge,
                color = if (confirmButtonText == stringResource(R.string.button_delete)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun RowElevatedButtons() {}