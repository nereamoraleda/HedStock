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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.store.Store
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralSegmentedButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.ShowMessageErrorText
import es.cursos.android.ejercicios.stocksnma.ui.components.VerticalScrollableColumn
import es.cursos.android.ejercicios.stocksnma.ui.components.colorsSimpleTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.supportingErrorText
import es.cursos.android.ejercicios.stocksnma.utils.enums.StoreSections
import es.cursos.android.ejercicios.stocksnma.utils.validations.StoreValidationForm

@Composable
fun StoreDetailsScreen(
    storeId: Long,
    navigateBack: () -> Unit
) {
    val viewModel: StoreDetailsViewModel = hiltViewModel()
    val store by viewModel.editableStore.collectAsState()
    val validationState by viewModel.validationState.collectAsState()
    val isFormValid by viewModel.isFormValid.collectAsState()
    val canEdit by viewModel.hasPermission.collectAsState()

    LaunchedEffect(storeId) {
        viewModel.getStoreDetails(storeId)
    }

    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.store_details_title),
                navigationButton = { NavigateBackButton(onNavigateBack = { navigateBack() } ) }
            )
        },
        bottomBar = {
            if (canEdit) {
                ButtonsBottomBar(
                    acceptButtonEnabled = isFormValid,
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
            StoreDetailsBodyScreen(
                store,
                onFieldChange = viewModel::onFieldChange,
                onCheckedChange = viewModel::onFieldChange,
                validacionState = validationState,
                canEdit = canEdit
            )
        }
    }
}


@Composable
fun StoreDetailsBodyScreen(
    store: Store,
    onFieldChange: (StoreFields, String) -> Unit,
    onCheckedChange: (StoreFields, Boolean) -> Unit,
    validacionState: StoreValidationForm,
    canEdit: Boolean
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
                        onValueChange = { onFieldChange(StoreFields.NAME, it) },
                        readOnly = !canEdit,
                        placeholder = {
                            Text(
                                stringResource(R.string.store_name),
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        //supportingText = supportingErrorText(validacionState.nameMessageError),
                        isError = validacionState.nameMessageError != null,
                        textStyle = MaterialTheme.typography.titleLarge,
                        colors = colorsSimpleTextField(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    ShowMessageErrorText(
                        validacionState.nameMessageError,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_16dp))
                            .padding(bottom = dimensionResource(R.dimen.padding_16dp))
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
                                onValueChange = { onFieldChange(StoreFields.ADDRESS, it) },
                                readOnly = !canEdit,
                                label = stringResource(R.string.store_address),
                                //supportingText = supportingErrorText(validacionState.addressErrorMessage),
                                //isError = validacionState.nameMessageError != null
                            )

                            GeneralOutlinedTextField(
                                value = store.city,
                                onValueChange = { onFieldChange(StoreFields.CITY, it) },
                                readOnly = !canEdit,
                                label = stringResource(R.string.store_city),
                                //supportingText = supportingErrorText(validacionState.cityErrorMessage),
                                //isError = validacionState.cityErrorMessage != null
                            )

                            GeneralOutlinedTextField(
                                value = store.country,
                                onValueChange = { onFieldChange(StoreFields.COUNTRY, it) },
                                readOnly = !canEdit,
                                label = stringResource(R.string.store_country),
                                //supportingText = supportingErrorText(validacionState.countryErrorMessage),
                                //isError = validationState.countryErrorMessage != null
                            )

                            GeneralOutlinedTextField(
                                value = store.postalCode,
                                onValueChange = { onFieldChange(StoreFields.POSTAL_CODE, it) },
                                readOnly = !canEdit,
                                label = stringResource(R.string.store_postal_code),
                                //supportingText = supportingErrorText(validacionState.postalCodeErrorMessage),
                                //isError = validacionState.postalCodeErrorMessage != null
                            )
                        }
                        StoreSections.CONTACT -> {
                            GeneralOutlinedTextField(
                                value = store.email,
                                onValueChange = { onFieldChange(StoreFields.EMAIL, it) },
                                readOnly = !canEdit,
                                label = stringResource(R.string.store_email),
                                supportingText = supportingErrorText(validacionState.emailMessageError),
                                isError = validacionState.emailMessageError != null || validacionState.contactInformationErrorMessage != null
                            )

                            GeneralOutlinedTextField(
                                value = store.phone,
                                onValueChange = { onFieldChange(StoreFields.PHONE, it) },
                                readOnly = !canEdit,
                                label = stringResource(R.string.store_phone),
                                supportingText = supportingErrorText(validacionState.phoneMessageError),
                                isError = validacionState.phoneMessageError != null || validacionState.contactInformationErrorMessage != null
                            )

                            ShowMessageErrorText(validacionState.contactInformationErrorMessage)
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
                            enabled = canEdit
                        )
                    }
                }
            }
        }
    }
}
