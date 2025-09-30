package es.cursos.android.ejercicios.stocksnma.ui.screen.supplier

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomCard
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomTextFieldProduct
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
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
                enabled = viewModel.supplierUiState.isEntryValid,
                onAcceptAction = {
                    coroutineScope.launch {
                        viewModel.saveSupplier()
                        onSupplierCreated()
                    }
                },
                onCancelAction = { /*TODO*/ }
            )
        },

        modifier = Modifier.fillMaxSize().imePadding(),


    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
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

    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_16dp))
            .fillMaxWidth()
    ) {
        CustomCard(modifier = Modifier.fillMaxWidth()) {
            SupplierNewCard(
                supplierUiState = newSupplierViewModel.supplierUiState,
                onSupplierValueChange = newSupplierViewModel::updateUiState,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_16dp))
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.supplier_address),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_4dp)))
        CustomCard(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = newSupplierItem.address,
                onValueChange = { newSupplierViewModel.updateUiState(supplierItem = newSupplierItem.copy(address = it)) },
                placeholder = { Text(text = "Ejemplo: Calle HedStock, 123, 2ºA"/*stringResource(R.string.supplier_create_address)*/) },
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_16dp))
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
            )

            TextField(
                value = newSupplierItem.country,
                onValueChange = { newSupplierViewModel.updateUiState(supplierItem = newSupplierItem.copy(country = it)) },
                placeholder = { Text(text = stringResource(R.string.supplier_create_country)) },
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_16dp))
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
            )

            TextField(
                value = newSupplierItem.city,
                onValueChange = { newSupplierViewModel.updateUiState(supplierItem = newSupplierItem.copy(city = it)) },
                placeholder = { Text(text = stringResource(R.string.supplier_create_city)) },
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_16dp))
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
            )

            TextField(
                value = newSupplierItem.zipCode,
                onValueChange = { newSupplierViewModel.updateUiState(supplierItem = newSupplierItem.copy(zipCode = it)) },
                placeholder = { Text(text = stringResource(R.string.supplier_create_zip_code)) },
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_16dp))
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
            )
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


    Column(modifier) {
        CustomTextFieldProduct(
            value = supplierItem.name,
            onValueChange = { onSupplierValueChange(supplierItem.copy(name = it)) },
            label = stringResource(R.string.supplier_form_name),
            textAling = TextAlign.Start,
        )

        CustomTextFieldProduct(
            value = supplierItem.contactName,
            onValueChange = { onSupplierValueChange(supplierItem.copy(contactName = it)) },
            label = stringResource(R.string.supplier_contact_name),
            textAling = TextAlign.Start,
        )

        CustomTextFieldProduct(
            value = supplierItem.phone,
            onValueChange = { onSupplierValueChange(supplierItem.copy(phone = it)) },
            label = stringResource(R.string.supplier_phone),
            textAling = TextAlign.Start,
            keyboardType = KeyboardType.Phone
        )

        CustomTextFieldProduct(
            value = supplierItem.email,
            onValueChange = { onSupplierValueChange(supplierItem.copy(email = it)) },
            label = stringResource(R.string.supplier_email),
            textAling = TextAlign.Start,
            keyboardType = KeyboardType.Email
        )

//        CustomTextFieldProduct(
//            value = supplierItem.address,
//            onValueChange = { onSupplierValueChange(supplierItem.copy(address = it)) },
//            label = stringResource(R.string.supplier_create_address),
//            textAling = TextAlign.Start,
//        )
//
//        CustomTextFieldProduct(
//            value = supplierItem.country,
//            onValueChange = { onSupplierValueChange(supplierItem.copy(country = it)) },
//            label = stringResource(R.string.supplier_create_country),
//            textAling = TextAlign.Start,
//        )
//
//        CustomTextFieldProduct(
//            value = supplierItem.city,
//            onValueChange = { onSupplierValueChange(supplierItem.copy(city = it)) },
//            label = stringResource(R.string.supplier_create_city),
//            textAling = TextAlign.Start,
//        )
//
//        CustomInputProduct(
//            text = stringResource(R.string.supplier_create_zip_code),
//            value = supplierItem.zipCode,
//            onValueChange = { onSupplierValueChange(supplierItem.copy(zipCode = it)) },
//            typeBoard = KeyboardType.Number
//        )
    }
}