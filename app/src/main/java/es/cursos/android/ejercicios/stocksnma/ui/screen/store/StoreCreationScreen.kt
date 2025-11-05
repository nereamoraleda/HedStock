package es.cursos.android.ejercicios.stocksnma.ui.screen.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import es.cursos.android.ejercicios.stocksnma.utils.enums.fields.StoreFields
import es.cursos.android.ejercicios.stocksnma.utils.validations.StoreValidationForm

@Composable
fun StoreCreationScreen(
    navigateBack: () -> Unit
) {
    val viewModel: StoreCreationViewModel = hiltViewModel()

    val store by viewModel.newStore.collectAsState()
    val validations by viewModel.validations.collectAsState()


    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.store_create_title),
                navigationButton = { NavigateBackButton(onNavigateBack = { navigateBack() }) }
            )
        },
        bottomBar = {
            ButtonsBottomBar(
                onAcceptAction = { viewModel.createStore() },
                onCancelAction = { viewModel.resetUi() },
                acceptButtonEnabled = viewModel.uiState.isFormValid
            )
        }

    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            StoreCreationBodyScreen(
                store = store,
                onValueChange = viewModel::onFieldChange,
                validations = validations
            )
        }
    }
}


@Composable
fun StoreCreationBodyScreen(
    store: Store,
    onValueChange: (StoreFields, String) -> Unit,
    validations: StoreValidationForm
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
            label = { section ->
                when (section) {
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
                        onValueChange = { onValueChange(StoreFields.NAME, it) },
                        label = stringResource(R.string.store_name),
                    )
                    ShowMessageErrorText(validations.nameMessageError)
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
                            onValueChange = { onValueChange(StoreFields.EMAIL, it) },
                            label = stringResource(R.string.store_email),
                            keyboardType = KeyboardType.Email,
                            supportingText = supportingErrorText(validations.emailMessageError),
                            isError = validations.emailMessageError != null || validations.contactInformationErrorMessage != null
                        )

                        GeneralPhoneOutlinedTextField(
                            value = store.phone,
                            valueLength = StoreFields.PHONE.maxLength,
                            onValueChange = { onValueChange(StoreFields.PHONE, it) },
                            label = stringResource(R.string.store_phone),
                            supportingText = supportingErrorText(validations.phoneMessageError),
                            isError = validations.phoneMessageError != null || validations.contactInformationErrorMessage != null
                        )
                        ShowMessageErrorText(validations.contactInformationErrorMessage)
                    }
                        StoreSections.ADDRESS -> {
                            GeneralOutlinedTextField(
                                value = store.address,
                                valueLength = StoreFields.ADDRESS.maxLength,
                                onValueChange = { onValueChange(StoreFields.ADDRESS, it) },
                                label = stringResource(R.string.store_address),
                                singleLine = false,
                                minLines = 2
                            )

                            GeneralOutlinedTextField(
                                value = store.city,
                                valueLength = StoreFields.CITY.maxLength,
                                onValueChange = { onValueChange(StoreFields.CITY, it) },
                                label = stringResource(R.string.store_city),
                            )

                            GeneralOutlinedTextField(
                                value = store.country,
                                valueLength = StoreFields.COUNTRY.maxLength,
                                onValueChange = { onValueChange(StoreFields.COUNTRY, it) },
                                label = stringResource(R.string.store_country),
                            )

                            GeneralOutlinedTextField(
                                value = store.postalCode,
                                valueLength = StoreFields.ZIP_CODE.maxLength,
                                onValueChange = { onValueChange(StoreFields.ZIP_CODE, it) },
                                label = stringResource(R.string.store_postal_code)
                            )
                        }
                    }
                }
            }
        }
    }
}
