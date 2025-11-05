package es.cursos.android.ejercicios.stocksnma.ui.screen.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.hilt.navigation.compose.hiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.store.Store
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.ErrorContent
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralPhoneOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSegmentedButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTextFieldTitle
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.LoadingContent
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.ShowMessageErrorText
import es.cursos.android.ejercicios.stocksnma.ui.components.VerticalScrollableColumn
import es.cursos.android.ejercicios.stocksnma.ui.components.supportingErrorText
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.constants.FieldMaxLengths
import es.cursos.android.ejercicios.stocksnma.utils.enums.StoreSections
import es.cursos.android.ejercicios.stocksnma.utils.enums.fields.StoreFields
import es.cursos.android.ejercicios.stocksnma.utils.validations.StoreValidationForm

@Composable
fun StoreDetailsScreen(
    storeId: Long,
    navigateBack: () -> Unit
) {
    val viewModel: StoreDetailsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val store by viewModel.editableStore.collectAsState()
    val validationState by viewModel.validationState.collectAsState()
    val hasPermission by viewModel.hasPermission.collectAsState()

    //var showDialogConfirmation by remember { mutableStateOf(false) }


    LaunchedEffect(storeId) {
        viewModel.getStoreDetails(storeId)
    }


    // -------------------- DIALOGS -------------------- //
/*    if (showDialogConfirmation) {
        ConfirmationDialog(
            title = stringResource(R.string.store_delete_title),
            message = stringResource(R.string.store_delete_message, store.name),
            confirmButtonText = stringResource(R.string.button_delete),
            onConfirmAction = {
                viewModel.deleteStore()
                navigateBack()
            },
            onDismissRequest = { showDialogConfirmation = false }
        )
       }
 */



    // -------------------- UI -------------------- //
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.store_details_title),
                navigationButton = { NavigateBackButton(onNavigateBack = { navigateBack() } ) },
                /*
                actionButton = {
                    if (hasPermission) {
                        GeneralIconButton(
                            onClick = { showDialogConfirmation = true },
                            icon = R.drawable.ic_delete_forever
                        )
                    }
                }
                 */

            )
        },
        bottomBar = {
            if (uiState is DetailsUiState.Success && hasPermission) {
                ButtonsBottomBar(
                    acceptButtonEnabled = (uiState as DetailsUiState.Success).isFormValid,
                    onAcceptAction = { viewModel.saveChanges { success ->
                        if (success) navigateBack()
                    } },
                    onCancelAction = { viewModel.resetUi() }
                )
            }
        }

    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState) {
                is DetailsUiState.Loading -> { LoadingContent() }
                is DetailsUiState.Error, DetailsUiState.NotFound -> { ErrorContent((uiState as DetailsUiState.Error).messageError) }
                is DetailsUiState.Success -> {
                    StoreDetailsBodyScreen(
                        store,
                        onFieldChange = viewModel::onFieldChange,
                        onCheckedChange = viewModel::onFieldChange,
                        validacionState = validationState,
                        hasPermission = hasPermission
                    )
                }
            }
        }
    }
}


@Composable
fun StoreDetailsBodyScreen(
    store: Store,
    onFieldChange: (StoreFields, String) -> Unit,
    onCheckedChange: (StoreFields, Boolean) -> Unit,
    validacionState: StoreValidationForm,
    hasPermission: Boolean
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
                    GeneralTextFieldTitle(
                        value = store.name,
                        valueLength = StoreFields.NAME.maxLength,
                        onValueChange = { onFieldChange(StoreFields.NAME, it) },
                        label = stringResource(R.string.store_name),
                        readOnly = !hasPermission,
                        isError = validacionState.nameMessageError != null
                    )
                    ShowMessageErrorText(validacionState.nameMessageError)
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
                            value = store.email,
                            valueLength = StoreFields.EMAIL.maxLength,
                            onValueChange = { onFieldChange(StoreFields.EMAIL, it) },
                            readOnly = !hasPermission,
                            label = stringResource(R.string.store_email),
                            supportingText = supportingErrorText(validacionState.emailMessageError),
                            isError = validacionState.emailMessageError != null || validacionState.contactInformationErrorMessage != null
                        )

                        GeneralPhoneOutlinedTextField(
                            value = store.phone,
                            valueLength = StoreFields.PHONE.maxLength,
                            onValueChange = { onFieldChange(StoreFields.PHONE, it) },
                            readOnly = !hasPermission,
                            label = stringResource(R.string.store_phone),
                            supportingText = supportingErrorText(validacionState.phoneMessageError),
                            isError = validacionState.phoneMessageError != null || validacionState.contactInformationErrorMessage != null
                        )
                        ShowMessageErrorText(validacionState.contactInformationErrorMessage)
                    }
                        StoreSections.ADDRESS -> {
                            GeneralOutlinedTextField(
                                value = store.address,
                                valueLength = StoreFields.ADDRESS.maxLength,
                                onValueChange = { onFieldChange(StoreFields.ADDRESS, it) },
                                label = stringResource(R.string.store_address),
                                readOnly = !hasPermission,
                                singleLine = false,
                                minLines = 2
                            )

                            GeneralOutlinedTextField(
                                value = store.city,
                                valueLength = StoreFields.CITY.maxLength,
                                onValueChange = { onFieldChange(StoreFields.CITY, it) },
                                readOnly = !hasPermission,
                                label = stringResource(R.string.store_city),
                            )

                            GeneralOutlinedTextField(
                                value = store.country,
                                valueLength = StoreFields.COUNTRY.maxLength,
                                onValueChange = { onFieldChange(StoreFields.COUNTRY, it) },
                                readOnly = !hasPermission,
                                label = stringResource(R.string.store_country),
                            )

                            GeneralOutlinedTextField(
                                value = store.postalCode,
                                valueLength = StoreFields.ZIP_CODE.maxLength,
                                onValueChange = { onFieldChange(StoreFields.ZIP_CODE, it) },
                                readOnly = !hasPermission,
                                label = stringResource(R.string.store_postal_code),
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
                            checked = store.isActive,
                            onCheckedChange = {
                                onCheckedChange(
                                    StoreFields.IS_ACTIVE,
                                    !store.isActive
                                )
                            },
                            enabled = hasPermission
                        )
                    }
                }
            }
        }
    }
}
