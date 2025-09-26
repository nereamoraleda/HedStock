package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.material3.Checkbox
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState


/**
 * COMPOSABLE ParentChechBox - Componente de checkbox padre
 *
 * @param state Estado del checkbox padre
 * @param onClick Acción a realizar cuando se pulsa el checkbox padre
 */
@Composable
fun ParentCheckBox(
    state: ToggleableState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TriStateCheckbox(
        state = state,
        onClick = onClick,
        modifier = modifier
    )
}


/**
 * COMPOSABLE ChildCheckBox - Componente de checkbox hijo
 *
 * @param checked Estado del checkbox hijo
 * @param onCheckedChange Acción a realizar cuando se pulsa el checkbox hijo
 */
@Composable
fun ChildCheckBox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier
    )
}
