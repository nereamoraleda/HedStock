package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun GeneralHorizontalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        modifier = modifier
    )
}


@Composable
fun GeneralHorizontalDividerIfLast(
    item: Any,
    items: List<Any>,
) {
    if (items.indexOf(item) != items.lastIndex) GeneralHorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
