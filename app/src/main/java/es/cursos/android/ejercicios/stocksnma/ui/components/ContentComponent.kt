package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R

@Composable
fun CardColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        content = { content() },
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_24dp))
    )
}

@Composable
fun VerticalScrollableColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        content = { content() },
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    )
}


@Composable
fun CenteredContent(content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
        content = { content() }
    )
}


@Composable
fun LoadingContent() {
    CenteredContent {
        CircularProgressIndicator(modifier = Modifier.size(50.dp))
    }
}


@Composable
fun NotFoundContent(itemNotFound: String) {
    CenteredContent {
        Text(text = itemNotFound)
    }
}


@Composable
fun ErrorContent(messageError: String) {
    CenteredContent {
        Text(text = messageError)
    }
}
