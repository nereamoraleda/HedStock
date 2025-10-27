package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.utils.enums.HomeSections


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
    section: HomeSections,
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
