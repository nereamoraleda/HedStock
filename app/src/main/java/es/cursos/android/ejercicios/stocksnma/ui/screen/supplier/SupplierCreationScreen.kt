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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralPhoneOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSegmentedButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTextFieldTitle
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.ShowMessageErrorText
import es.cursos.android.ejercicios.stocksnma.ui.components.VerticalScrollableColumn
import es.cursos.android.ejercicios.stocksnma.ui.components.supportingErrorText
import es.cursos.android.ejercicios.stocksnma.utils.constants.FieldMaxLengths
import es.cursos.android.ejercicios.stocksnma.utils.enums.StoreSections
import es.cursos.android.ejercicios.stocksnma.utils.enums.fields.SupplierFields
import es.cursos.android.ejercicios.stocksnma.utils.validations.SupplierValidationForm

@Composable
fun SupplierCreationScreen(
    viewModel: SupplierCreationViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    // -------------------- VARIABLES -------------------- //
    val validationForm by viewModel.validationForm.collectAsState()        // Validación del formulario
    var showLossChangesDialog by remember { mutableStateOf(false) }  // Mostrar un Dialog para navegar hacia atrás o quedarse en la pantalla (solo cuando hay algún cambio en el formulario)



    // -------------------- DIALOGS -------------------- //
    if (showLossChangesDialog) {
        LossChangesDialog(
            onDismissRequest = { showLossChangesDialog = false },
            onConfirmAction = {
                showLossChangesDialog = false
                viewModel.resetUi()
                navigateBack()
            }
        )
    }



    // -------------------- UI -------------------- //
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.supplier_create_title),
                navigationButton = {
                    NavigateBackButton( {
                        if (viewModel.uiState.newItem == Supplier()) navigateBack()
                        else showLossChangesDialog = true
                    })
                }
            )
        },

        bottomBar = {
            ButtonsBottomBar(
                acceptButtonEnabled = viewModel.uiState.isFormValid,
                onAcceptAction = { viewModel.saveSupplier() },
                onCancelAction = { viewModel.resetUi() }
            )
        },

        modifier = Modifier
            .fillMaxSize()
            .imePadding()

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SupplierCreationBodyScreen(
                newSupplier = viewModel.uiState.newItem,
                onValueChange = viewModel::onFieldChange,
                validationForm = validationForm
            )
        }
    }
}


@Composable
fun SupplierCreationBodyScreen(
    newSupplier: Supplier,
    onValueChange: (SupplierFields, String) -> Unit,
    validationForm: SupplierValidationForm
) {
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
                        value = newSupplier.name,
                        valueLength = SupplierFields.NAME.maxLength,
                        onValueChange = { onValueChange(SupplierFields.NAME, it) },
                        label = stringResource(R.string.supplier_name),
                        isError = validationForm.nameErrorMessage != null
                    )
                    ShowMessageErrorText(validationForm.nameErrorMessage)
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_16dp))
                ) {
                    when (sectionSelected) {
                        StoreSections.CONTACT -> {
                            GeneralOutlinedTextField(
                                value = newSupplier.contactName,
                                valueLength = SupplierFields.CONTACT_NAME.maxLength,
                                onValueChange = { onValueChange(SupplierFields.CONTACT_NAME, it) },
                                label = stringResource(R.string.supplier_contact_name),
                                supportingText = supportingErrorText()
                            )

                            GeneralOutlinedTextField(
                                value = newSupplier.email,
                                valueLength = SupplierFields.EMAIL.maxLength,
                                onValueChange = { onValueChange(SupplierFields.EMAIL, it) },
                                label = stringResource(R.string.supplier_email),
                                supportingText = supportingErrorText(validationForm.emailErrorMessage),
                                isError = validationForm.emailErrorMessage != null || validationForm.contactInformationErrorMessage != null,
                                keyboardType = KeyboardType.Email
                            )

                            GeneralPhoneOutlinedTextField(
                                value = newSupplier.phone,
                                valueLength = SupplierFields.PHONE.maxLength,
                                onValueChange = { onValueChange(SupplierFields.PHONE, it) },
                                label = stringResource(R.string.supplier_phone),
                                supportingText = supportingErrorText(validationForm.phoneErrorMessage),
                                isError = validationForm.phoneErrorMessage != null || validationForm.contactInformationErrorMessage != null
                            )
                            ShowMessageErrorText(validationForm.contactInformationErrorMessage)
                        }
                        StoreSections.ADDRESS -> {
                            GeneralOutlinedTextField(
                                value = newSupplier.address,
                                valueLength = SupplierFields.ADDRESS.maxLength,
                                onValueChange = { onValueChange(SupplierFields.ADDRESS, it) },
                                label = stringResource(R.string.supplier_address),
                                supportingText = { Text(stringResource(R.string.supplier_address_example)) },
                                singleLine = false,
                                minLines = 2
                            )

                            GeneralOutlinedTextField(
                                value = newSupplier.city,
                                valueLength = SupplierFields.CITY.maxLength,
                                onValueChange = { onValueChange(SupplierFields.CITY, it) },
                                label = stringResource(R.string.supplier_create_city),
                                supportingText = supportingErrorText(validationForm.cityErrorMessage),
                                isError = validationForm.cityErrorMessage != null
                            )

                            GeneralOutlinedTextField(
                                value = newSupplier.country,
                                valueLength = SupplierFields.COUNTRY.maxLength,
                                onValueChange = { onValueChange(SupplierFields.COUNTRY, it) },
                                label = stringResource(R.string.supplier_create_country),
                                supportingText = supportingErrorText(validationForm.countryErrorMessage),
                                isError = validationForm.countryErrorMessage != null
                            )

                            GeneralOutlinedTextField(
                                value = newSupplier.zipCode,
                                valueLength = SupplierFields.ZIP_CODE.maxLength,
                                onValueChange = { onValueChange(SupplierFields.ZIP_CODE, it) },
                                label = stringResource(R.string.supplier_create_zip_code)
                            )
                        }
                    }
                }
            }
        }
    }
}
