package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import es.cursos.android.ejercicios.stocksnma.R

/**
 * COMPOSABLE - TOP APP BAR (GENERAL DE LA APP)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralTopAppBar(
    title: String,
    navigationButton: @Composable () -> Unit,
    actionButton: @Composable () -> Unit = {},
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = { navigationButton() },
        actions = { actionButton() },
        modifier = modifier
    )
}


@Composable
fun HomeTopAppBar(
    //section: HomeSections,
    isSearching: Boolean,
    onNavClick: () -> Unit,
    actionButton: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isSearching) {
        GeneralTopAppBar(
            title = stringResource(R.string.app_name),
            navigationButton = {
                GeneralIconButton(
                    onClick = onNavClick,
                    icon = R.drawable.ic_menu
                )
            },
            actionButton = { actionButton() },
            modifier = modifier
        )
    }
}
