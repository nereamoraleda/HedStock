package es.cursos.android.ejercicios.stocksnma.ui.screen.supplier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSegmentedButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTextFieldTitle
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.VerticalScrollableColumn
import es.cursos.android.ejercicios.stocksnma.utils.enums.StoreSections
import kotlinx.coroutines.launch

@Composable
fun SupplierCreationScreen(
    viewModel: SupplierCreationViewModel,
    onSupplierCreated: () -> Unit,
    navigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.supplier_create_title),
                navigationButton = { NavigateBackButton(navigateBack) }
            )
        },

        bottomBar = {
            ButtonsBottomBar(
                acceptButtonEnabled = viewModel.supplierUiState.isEntryValid,
                onAcceptAction = {
                    coroutineScope.launch {
                        viewModel.saveSupplier()
                        onSupplierCreated()
                    }
                },
                onCancelAction = { /*TODO*/ }
            )
        },

        modifier = Modifier
            .fillMaxSize()
            .imePadding(),


    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SupplierNewBody(
                newSupplierViewModel = viewModel
            )
        }
    }
}


@Composable
fun SupplierNewBody(
    newSupplierViewModel: SupplierCreationViewModel
) {
    val newSupplierItem = newSupplierViewModel.supplierUiState.supplierItem
    var sectionSelected by remember { mutableStateOf(StoreSections.CONTACT) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
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
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    GeneralTextFieldTitle(
                        value = newSupplierItem.name,
                        onValueChange = { newSupplierViewModel.updateUiState(newSupplierItem.copy(name = it)) },
                        label = stringResource(R.string.supplier_name)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_16dp))
                ) {
                    when (sectionSelected) {
                        StoreSections.CONTACT -> {
                            SupplierNewCard(
                                supplierUiState = newSupplierViewModel.supplierUiState,
                                onSupplierValueChange = newSupplierViewModel::updateUiState,
                                modifier = Modifier
                                    .padding(dimensionResource(R.dimen.padding_16dp))
                                    .fillMaxWidth()
                            )
                        }
                        StoreSections.ADDRESS -> {
                            GeneralOutlinedTextField(
                                value = newSupplierItem.address,
                                onValueChange = {
                                    newSupplierViewModel.updateUiState(
                                        supplierItem = newSupplierItem.copy(address = it)
                                    )
                                },
                                label = stringResource(R.string.supplier_address),
                                supportingText = { Text("Ej: C/ HedStock, N123, 2ºA") }
                            )

                            GeneralOutlinedTextField(
                                value = newSupplierItem.country,
                                onValueChange = {
                                    newSupplierViewModel.updateUiState(
                                        supplierItem = newSupplierItem.copy(
                                            country = it
                                        )
                                    )
                                },
                                label = stringResource(R.string.supplier_create_country)
                            )

                            GeneralOutlinedTextField(
                                value = newSupplierItem.city,
                                onValueChange = {
                                    newSupplierViewModel.updateUiState(
                                        supplierItem = newSupplierItem.copy(
                                            city = it
                                        )
                                    )
                                },
                                label = stringResource(R.string.supplier_create_city)
                            )

                            GeneralOutlinedTextField(
                                value = newSupplierItem.zipCode,
                                onValueChange = {
                                    newSupplierViewModel.updateUiState(
                                        supplierItem = newSupplierItem.copy(
                                            zipCode = it
                                        )
                                    )
                                },
                                label = stringResource(R.string.supplier_create_zip_code)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SupplierNewCard(
    supplierUiState: SupplierCreationViewModel.SupplierUiState,
    onSupplierValueChange: (Supplier) -> Unit,
    modifier: Modifier = Modifier
) {
    val supplierItem = supplierUiState.supplierItem

    GeneralOutlinedTextField(
        value = supplierItem.contactName,
        onValueChange = { onSupplierValueChange(supplierItem.copy(contactName = it)) },
        label = stringResource(R.string.supplier_contact_name),
        //textAling = TextAlign.Start,
    )

    GeneralOutlinedTextField(
        value = supplierItem.phone,
        onValueChange = { onSupplierValueChange(supplierItem.copy(phone = it)) },
        label = stringResource(R.string.supplier_phone),
        //textAling = TextAlign.Start,
        keyboardType = KeyboardType.Phone
    )

    GeneralOutlinedTextField(
        value = supplierItem.email,
        onValueChange = { onSupplierValueChange(supplierItem.copy(email = it)) },
        label = stringResource(R.string.supplier_email),
        //textAling = TextAlign.Start,
        keyboardType = KeyboardType.Email
    )
}
