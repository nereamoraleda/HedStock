package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R


/**
 * COMPOSABLE - CARD (GENERAL DE LA APP)
 *
 * @param content - Contenido de la tarjeta
 *
 */
@Composable
fun CustomCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        content = { content() }
    )
}


@Composable
fun GeneralCard(content: @Composable () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(16.dp),
        modifier = Modifier.padding(16.dp),
        content = { content() }
    )
}

@Composable
fun DetailsCard(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}


@Composable
fun CreateCategoryCard(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_16dp))
            .fillMaxWidth()
    ) {
        content()
    }
}


@Composable
fun CustomDialogCard(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            content()
        }
    }
}
