package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.cursos.android.ejercicios.stocksnma.R


@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    confirmButtonText: String,
    cancelButtonText: String = stringResource(R.string.button_cancel),
    onDismissRequest: () -> Unit,
    onConfirmAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        CustomDialogCard(
            content = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(
                            text = cancelButtonText,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    TextButton(onClick = {
                        onDismissRequest()
                        onConfirmAction()
                    }) {
                        Text(
                            text = confirmButtonText,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (confirmButtonText == stringResource(R.string.button_delete)) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        )
    }
}


@Composable
fun CategoryDialog(
    title: String,
    text: String = "",
    onDismissRequest: () -> Unit,
    onAcceptAction: (String) -> Unit,
    acceptButtonText: String = stringResource(R.string.button_create),
    modifier: Modifier = Modifier
) {
    var categoryName by remember { mutableStateOf("") }
    categoryName = text

    Dialog(onDismissRequest = { /*Para que no se cierre al pulsar fuera de la pantalla*/ }) {
        CreateCategoryCard(
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_medium)))

                    TextField(
                        value = categoryName,
                        onValueChange = { categoryName = it },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.category_create_name),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier.fillMaxWidth()
                    ) {
                        ButtonCancel(
                            action = onDismissRequest,
                            modifier = modifier.weight(1f)
                        )

                        Spacer(modifier = modifier.weight(0.25f))

                        ButtonAccept(
                            textButton = acceptButtonText,
                            action = {
                                onAcceptAction(categoryName)
                                onDismissRequest()
                            },
                            enabled = categoryName.isNotBlank(),
                            modifier = modifier.weight(1f)
                        )
                    }
                }
            }
        )
    }
}


@Composable
fun AboutAppDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .fillMaxWidth()
            ) {
                // Título
                Text(
                    text = stringResource(R.string.about_dialog_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                // Descripción
                Text(
                    text = stringResource(R.string.about_dialog_description),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Versión
                Text(
                    text = stringResource(R.string.about_dialog_version),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_very_small)))

                // Desarrollador
                Text(
                    text = stringResource(R.string.about_dialog_developer),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismissRequest()}) {
                        Text(
                            text = stringResource(R.string.about_dialog_close),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}