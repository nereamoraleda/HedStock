package es.cursos.android.ejercicios.stocksnma.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    active: Boolean,
    onActiveChange: () -> Unit,
    placeholderText: String,
    cleanQuery: () -> Unit,
    content: @Composable () -> Unit,
    //modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = { onQueryChange(it) },
        onSearch = { onQueryChange(it) },
        active = active,
        onActiveChange = { onActiveChange() },
        placeholder = {
            Text(
                text = placeholderText,
                //color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            if (active) {
                GeneralIconButton(
                    onClick = onActiveChange,
                    icon = R.drawable.ic_arrow_back,
                    description = stringResource(R.string.go_back_search_bar_icon_description)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            if (active) {
                GeneralIconButton(
                    onClick = cleanQuery,
                    icon = R.drawable.ic_close,
                )
            }
        },
        colors = SearchBarDefaults.colors(dividerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        windowInsets = if (active) WindowInsets.systemBars else WindowInsets(top = 0),
        content = { content() },
        modifier =
        if (active) Modifier.fillMaxSize()
        else Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    )
}


@Composable
fun CustomSearchBarHistory(
    searchHistory: List<String>,
    onClickSearch: (String) -> Unit,
    onClearHistory: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        searchHistory.forEach { recentSearch ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .clickable { onClickSearch(recentSearch) }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_history_search),
                    contentDescription = null
                )
                Text(text = recentSearch)
            }
        }

        HorizontalDivider()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
                .clickable { onClearHistory() }
        ) {
            Text(text = stringResource(R.string.clean_history))
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = null
            )
        }
    }
}


@Composable
fun CustomSearchNotFound(notFoundText: Int = R.string.search_no_results) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = stringResource(notFoundText))
        //Text(text = "No se ha encontrado ningún producto")
    }
}