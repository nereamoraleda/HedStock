package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import es.cursos.android.ejercicios.stocksnma.R

/**
 * COMPONENTE NothingCreateScreen - Se muestra cuando no se han creado productos o proveedores
 * @param nothingCreateText - String resource que dependerá de la pantalla en la que se encuentre (producto/proveedor)
 */
@Composable
fun NothingCreateScreen(nothingCreateText: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = Modifier.fillMaxSize()
    ) {
        GifImage(modifier = Modifier.padding(top = 16.dp))
        Text(
            text = nothingCreateText,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 50.dp)
        )
    }
}


/**
 * COMPONENTE GifImage - Animación GIF
 */
@Composable
fun GifImage(modifier: Modifier = Modifier) {
    Image(
        painter = rememberDrawablePainter(
            drawable = getDrawable(
                LocalContext.current,
                R.drawable.hedgehog_sleep
            )
        ),
        contentDescription = stringResource(R.string.image_animated_description),
        contentScale = ContentScale.FillWidth,
        modifier = modifier
    )
}
