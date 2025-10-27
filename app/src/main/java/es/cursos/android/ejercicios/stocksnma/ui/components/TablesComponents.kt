package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import es.cursos.android.ejercicios.stocksnma.R


/**
 * COMPOSABLE - ENCABEZADO DE LA TABLA
 *
 * @param text Texto del encabezado
 */
@Composable
fun TableHeader(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_4dp))
    )
}


/**
 * COMPOSABLE - CELDA DE LA TABLA
 *
 * @param text Texto de la celda
 */
@Composable
fun TableCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_4dp))
    )
}