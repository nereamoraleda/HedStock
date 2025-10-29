package es.cursos.android.ejercicios.stocksnma.ui.screen.supplier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.ConfirmationDialog
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralIconButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSegmentedButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTextFieldTitle
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.LoadingContent
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.ShowMessageErrorText
import es.cursos.android.ejercicios.stocksnma.ui.components.VerticalScrollableColumn
import es.cursos.android.ejercicios.stocksnma.ui.components.supportingErrorText
import es.cursos.android.ejercicios.stocksnma.ui.screen.store.StoreFields
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.StoreSections
import es.cursos.android.ejercicios.stocksnma.utils.enums.SupplierFields
import es.cursos.android.ejercicios.stocksnma.utils.validations.SupplierValidationState

@Composable
fun SupplierDetailsScreen(
    viewModel: SupplierDetailsViewModel,
    idSupplier: Long,
    navigateBack: () -> Unit
) {
    LaunchedEffect(idSupplier) {
        viewModel.getSupplierById(idSupplier)
    }


    val state by viewModel.uiState.collectAsState()
    val validationState by viewModel.validationsSupplierState.collectAsState()

    var showDeleteSupplierConfirmation by remember { mutableStateOf(false) }
    var showLostChangesDialog by remember { mutableStateOf(false) }


    when (val uiState = state) {
        is DetailsUiState.Loading -> {
            LoadingContent()
        }
        is DetailsUiState.NotFound -> {}
        is DetailsUiState.Error -> {}
        is DetailsUiState.Success -> {
            if (showDeleteSupplierConfirmation) {
                ConfirmationDialog(
                    title = stringResource(R.string.confirm_delete_selected_suppliers_title),
                    message = stringResource(R.string.confirm_delete_selected_suppliers),
                    confirmButtonText = stringResource(R.string.button_accept),
                    onDismissRequest = { showDeleteSupplierConfirmation = false },
                    onConfirmAction = {
                        viewModel.deleteSupplier()
                        showDeleteSupplierConfirmation = false
                        navigateBack()
                    }
                )
            }

            if (showLostChangesDialog) {
                Dialog(
                    onDismissRequest = {}
                ) {
                    GeneralCard {
                        Column(
                            modifier = Modifier.padding(dimensionResource(R.dimen.padding_16dp))
                        ) {
                            Text(text = "Perderás los cambios sin guardar, ¿deseas continuar?")
                            Row {
                                TextButton(
                                    onClick = { showLostChangesDialog = false }
                                ) {
                                    Text(text = "Cancelar")
                                }

                                TextButton(
                                    onClick = {
                                        //viewModel.toggleEdit()
                                        navigateBack()
                                        showLostChangesDialog = false
                                    }
                                ) { Text(text = "Aceptar") }
                            }
                        }
                    }
                }
            }

            //val isEditing = uiState.isEditing ?: true

            Scaffold(
                topBar = {
                    GeneralTopAppBar(
                        title = stringResource(R.string.supplier_details_title),
                        navigationButton = { NavigateBackButton({ showLostChangesDialog = true }) },
                        actionButton = {
//                            GeneralIconButton(
//                                icon = R.drawable.ic_delete,
//                                onClick = { showDeleteSupplierConfirmation = true }
//                            ) TODO - Manejar primero si tiene productos asociados
                        }
                    )
                },

                bottomBar = {
                    ButtonsBottomBar(
                        acceptButtonEnabled = uiState.isFormValid,
                        onAcceptAction = { viewModel.saveSupplier() },
                        onCancelAction = { viewModel.resetUi() }
                    )
                }

            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    SupplierDetailsBody(
                        editableSupplier = uiState.editableItem,
                        onValueChange = viewModel::onFieldChange,
                        onCheckedChange = viewModel::onFieldChange,
                        validationState = validationState,
                        //isEditing = uiState.isEditing ?: true,
                    )
                }
            }
        }
    }
}


@Composable
fun SupplierDetailsBody(
    editableSupplier: Supplier,
    onValueChange: (SupplierFields, String) -> Unit,
    onCheckedChange: (SupplierFields, Boolean) -> Unit,
    validationState: SupplierValidationState,
    isEditing: Boolean = true,
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


        // -------------------- CARD -------------------- //
        VerticalScrollableColumn {
            GeneralCard {

                // Encabezado
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    GeneralTextFieldTitle(
                        value = editableSupplier.name,
                        onValueChange = { onValueChange(SupplierFields.NAME, it) },
                        label = stringResource(R.string.supplier_name),
                        isError = validationState.nameErrorMessage != null,
                        enabled = isEditing,
                    )
                    ShowMessageErrorText(validationState.nameErrorMessage)
                }

                // Body
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    when (sectionSelected) {
                        StoreSections.CONTACT -> {
                            GeneralOutlinedTextField(
                                value = editableSupplier.contactName,
                                onValueChange = { onValueChange(SupplierFields.CONTACT_NAME, it) },
                                label = stringResource(R.string.supplier_contact_name),
                                enabled = isEditing
                            )

                            GeneralOutlinedTextField(
                                value = editableSupplier.email,
                                onValueChange = { onValueChange(SupplierFields.EMAIL, it) },
                                label = stringResource(R.string.supplier_email),
                                supportingText = supportingErrorText(validationState.emailErrorMessage),
                                isError = validationState.emailErrorMessage != null,
                                enabled = isEditing,
                                keyboardType = KeyboardType.Email
                            )

                            GeneralOutlinedTextField(
                                value = editableSupplier.phone,
                                onValueChange = { onValueChange(SupplierFields.PHONE, it) },
                                label = stringResource(R.string.supplier_phone),
                                supportingText = supportingErrorText(validationState.phoneErrorMessage),
                                isError = validationState.phoneErrorMessage != null,
                                enabled = isEditing,
                                keyboardType = KeyboardType.Phone
                            )
                        }
                        StoreSections.ADDRESS -> {
                            GeneralOutlinedTextField(
                                value = editableSupplier.address,
                                onValueChange = { onValueChange(SupplierFields.ADDRESS, it) },
                                label = stringResource(R.string.supplier_address),
                                enabled = isEditing,
                                singleLine = false,
                                maxLines = 2,
                                //modifier = Modifier.heightIn(min = 100.dp)
                            )

                            GeneralOutlinedTextField(
                                value = editableSupplier.city,
                                onValueChange = { onValueChange(SupplierFields.CITY, it) },
                                label = stringResource(R.string.supplier_create_city),
                                enabled = isEditing
                            )

                            GeneralOutlinedTextField(
                                value = editableSupplier.country,
                                onValueChange = { onValueChange(SupplierFields.COUNTRY, it) },
                                label = stringResource(R.string.supplier_create_country),
                            )

                            GeneralOutlinedTextField(
                                value = editableSupplier.zipCode,
                                onValueChange = { onValueChange(SupplierFields.ZIP_CODE, it) },
                                label = stringResource(R.string.supplier_create_zip_code),
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.user_active))
                        Switch(
                            checked = editableSupplier.isActive,
                            onCheckedChange = {
                                onCheckedChange(
                                    SupplierFields.IS_ACTIVE,
                                    !editableSupplier.isActive
                                )
                            },
                            //enabled = hasPermission
                        )
                    }
                }
            }
        }
    }
}
