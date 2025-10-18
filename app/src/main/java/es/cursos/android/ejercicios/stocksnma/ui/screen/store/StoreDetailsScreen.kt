package es.cursos.android.ejercicios.stocksnma.ui.screen.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSegmentedButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.VerticalScrollableColumn
import es.cursos.android.ejercicios.stocksnma.ui.components.colorsSimpleTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.segmentedButtonColors
import es.cursos.android.ejercicios.stocksnma.utils.enums.StoreSections

@Composable
fun StoreDetailsScreen(
    storeId: Long,
    navigateBack: () -> Unit
) {
    val viewModel: StoreDetailsViewModel = hiltViewModel()
    val store by viewModel.store.collectAsState()

    LaunchedEffect(storeId) {
        viewModel.getDataStore(storeId)
    }

    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.store_details_title),
                navigationButton = { NavigateBackButton(onNavigateBack = { navigateBack() } ) }
            )
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            StoreDetailsBodyScreen(store)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailsBodyScreen(
    store: Store
) {
    var sectionSelected by remember { mutableStateOf(StoreSections.CONTACT) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // -------------------- SEGMENTED BUTTON -------------------- //
        GeneralSegmentedButton(
            selectedSection = sectionSelected,
            onSectionChange = { sectionSelected = it },
            sections = StoreSections.entries,
            label = {
                when (it) {
                    StoreSections.CONTACT -> stringResource(R.string.store_section_contact)
                    StoreSections.ADDRESS -> stringResource(R.string.store_section_address)
                }
            }
        )

        VerticalScrollableColumn {
            GeneralCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.secondary)
                ) {
                    TextField(
                        value = store.name,
                        onValueChange = { },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.store_name),
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        colors = colorsSimpleTextField(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_16dp))
                ) {
                    when (sectionSelected) {
                        StoreSections.ADDRESS -> {
                            GeneralOutlinedTextField(
                                value = store.address,
                                onValueChange = { },
                                label = stringResource(R.string.store_address),
                            )

                            GeneralOutlinedTextField(
                                value = store.city,
                                onValueChange = { },
                                label = stringResource(R.string.store_city),
                            )

                            GeneralOutlinedTextField(
                                value = store.country,
                                onValueChange = { },
                                label = stringResource(R.string.store_country),
                            )

                            GeneralOutlinedTextField(
                                value = store.postalCode,
                                onValueChange = { },
                                label = stringResource(R.string.store_postal_code),
                            )
                        }
                        StoreSections.CONTACT -> {
                            GeneralOutlinedTextField(
                                value = store.email,
                                onValueChange = { },
                                label = stringResource(R.string.store_email),
                            )

                            GeneralOutlinedTextField(
                                value = store.phone,
                                onValueChange = { },
                                label = stringResource(R.string.store_phone),
                            )
                        }
                    }
                }
            }
        }
    }
}
