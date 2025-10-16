package es.cursos.android.ejercicios.stocksnma.ui.screen.home.stores

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralHorizontalDividerIfLast
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSearchBar

@Composable
fun StoreSectionHomeScreen(navigateToStoreDetails: (Long) -> Unit) {
    val viewModel: StoreSectionHomeViewModel = hiltViewModel()
    val storesList by viewModel.storeList.collectAsState()

    GeneralSearchBar(
        query = "",
        onQueryChange = { },
        active = false,
        onActiveChange = { },
        placeholderText = "Buscar tienda",
        cleanQuery = { }
    ) {}


    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(storesList) { store ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { store.id?.let { navigateToStoreDetails(it) } }
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_supplier),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Column {
                    Text(text = store.name, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(text = store.city, style = MaterialTheme.typography.bodyMedium)
                    Text(text = store.email, style = MaterialTheme.typography.bodySmall)
                }
            }
            GeneralHorizontalDividerIfLast(store, storesList)
        }
    }
}
