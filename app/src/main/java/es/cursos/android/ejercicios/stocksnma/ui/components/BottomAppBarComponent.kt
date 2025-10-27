package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import es.cursos.android.ejercicios.stocksnma.R


// Número de productos seleccionados para:
const val SHOW_BOTTOM_BAR = 0
const val SHOW_SINGULAR_STRING = 1



/**
 * COMPOSABLE - BOTTOM APP BAR (GENERAL DE LA APP, BOTONES)
 *
 * @param onAcceptAction - Acción al pulsar el botón Aceptar
 * @param onCancelAction - Acción al pulsar el botón Cancelar
 * @param enabled - Botón Aceptar habilitado o no
 */
@Composable
fun ButtonsBottomBar(
    onAcceptAction: () -> Unit,
    onCancelAction: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        containerColor = Color.Transparent,
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_16dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
        ) {
            ButtonCancel(
                action = { onCancelAction() },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.weight(0.5f))

            ButtonAccept(
                action = { onAcceptAction() },
                enabled = enabled,
                modifier = Modifier.weight(1f)
            )
        }
    }
}



/**
 * COMPOSABLE - BOTTOM APP BAR (SCREEN INICIO, CONTADOR DE "PEDIDOS" SELECCIONADOS)
 *
 * @param selectedCheckBoxCount - Número de productos seleccionados
 * @param onDeleteSelected - Acción al pulsar el botón Borrar
 *
 */
@Composable
fun HomeBottomBar(
    textCountSingular: Int,
    textCountPlural: Int,
    selectedCheckBoxCount: Int,
    onDeleteSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selectedCheckBoxCount > SHOW_BOTTOM_BAR) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_16dp)),
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                Text(
                    modifier = Modifier.weight(4f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                    text =
                    if (selectedCheckBoxCount == SHOW_SINGULAR_STRING) stringResource(
                        textCountSingular,
                        selectedCheckBoxCount
                    )
                    else stringResource(textCountPlural, selectedCheckBoxCount),
                )

                GeneralIconButton(
                    onClick = onDeleteSelected,
                    icon = R.drawable.ic_delete,
                    modifier = Modifier.weight(0.5f)
                )
            }
        }
    }
}


@Composable
fun HomeBottomBar2(
    textCountSingular: Int,
    textCountPlural: Int,
    itemCount: Int,
    modifier: Modifier = Modifier
) {

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_16dp)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.weight(4f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                text =
                if (itemCount == SHOW_SINGULAR_STRING) stringResource(textCountSingular, itemCount)
                else stringResource(textCountPlural, itemCount)
            )
        }
    }
}