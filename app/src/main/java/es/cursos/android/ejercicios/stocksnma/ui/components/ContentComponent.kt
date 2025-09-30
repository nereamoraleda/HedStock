package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CenteredContent(content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
        content = { content() }
    )
}

@Composable
fun VerticalScrollableColumn(content: @Composable () -> Unit) {
    Column(
        content = { content() },
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
fun ErrorContent(message: String) {
    CenteredContent {
        Text(text = message)
    }
}
