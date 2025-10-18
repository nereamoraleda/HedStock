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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.CardColumn
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSegmentedButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.VerticalScrollableColumn
import es.cursos.android.ejercicios.stocksnma.ui.components.coloresTextFields
import es.cursos.android.ejercicios.stocksnma.ui.components.colorsSimpleTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.segmentedButtonColors
import es.cursos.android.ejercicios.stocksnma.utils.enums.StoreSections

@Composable
fun StoreCreationScreen(
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.store_create_title),
                navigationButton = { NavigateBackButton(onNavigateBack = { navigateBack() }) }
            )
        },
        bottomBar = {
            ButtonsBottomBar(
                onAcceptAction = {},
                onCancelAction = {},
                acceptButtonEnabled = false
            )
        }

    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            StoreCreationBodyScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreCreationBodyScreen() {
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
                        value = "Nombre de la tienda",
                        onValueChange = { },
                        placeholder = {
                            Text(
                                text = "Nombre de la tienda",/* TODO - stringResource(R.string.product_form_name),*/
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
                        StoreSections.CONTACT -> {
                        GeneralOutlinedTextField(
                            value = "Email",
                            onValueChange = { },
                            label = "Email",
                        )

                        GeneralOutlinedTextField(
                            value = "Teléfono",
                            onValueChange = { },
                            label = "Teléfono",
                        )
                    }
                        StoreSections.ADDRESS -> {
                            GeneralOutlinedTextField(
                                value = "Dirección",
                                onValueChange = { },
                                label = "Dirección",
                            )

                            GeneralOutlinedTextField(
                                value = "Ciudad",
                                onValueChange = { },
                                label = "Ciudad",
                            )

                            GeneralOutlinedTextField(
                                value = "País",
                                onValueChange = { },
                                label = "País",
                            )

                            GeneralOutlinedTextField(
                                value = "C.P",
                                onValueChange = { },
                                label = "C.P",
                            )
                        }
                    }
                }
            }
        }
    }
}
