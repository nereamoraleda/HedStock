package es.cursos.android.ejercicios.stocksnma.ui.screen.supplier

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.data.local.entity.SupplierEntity
import es.cursos.android.ejercicios.stocksnma.domain.model.Supplier
import es.cursos.android.ejercicios.stocksnma.ui.components.ConfirmationDialog
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomBottomAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralIconButton
import es.cursos.android.ejercicios.stocksnma.ui.components.IconButtonGoBack
import es.cursos.android.ejercicios.stocksnma.ui.components.ShowMessageErrorText
import es.cursos.android.ejercicios.stocksnma.ui.components.campoColores
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.SupplierValidationState


@Composable
fun SupplierDetailsScreen(
    viewModel: SupplierDetailsViewModel,
    idSupplier: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(idSupplier) {
        viewModel.getSupplierById(idSupplier)
    }

    var isEditing by remember { mutableStateOf(false) }
    val state by viewModel.uiState.collectAsState()
    val tempSupplier by viewModel.tempSupplier.collectAsState()
    val validationState by viewModel.validationsSupplierState.collectAsState()

    var showDeleteSupplierConfirmation by remember { mutableStateOf(false) }


    when (val uiState = state) {
        is DetailsUiState.Loading -> {}
        is DetailsUiState.Success -> {
            if (showDeleteSupplierConfirmation) {
                ConfirmationDialog(
                    title = stringResource(R.string.confirm_delete_selected_suppliers_title),
                    message = stringResource(R.string.confirm_delete_selected_suppliers),
                    confirmButtonText = stringResource(R.string.button_accept),
                    onDismissRequest = { showDeleteSupplierConfirmation = false },
                    onConfirmAction = {
                        viewModel.deleteSupplier(uiState.item)
                        showDeleteSupplierConfirmation = false
                        navigateBack()
                    }
                )
            }

            Scaffold(
                topBar = {
                    GeneralTopAppBar(
                        title = stringResource(R.string.supplier_details_title),
                        navigationButton = { IconButtonGoBack(navigateBack) },
                        actionButton = {
                            GeneralIconButton(
                                icon = R.drawable.ic_edit,
                                onClick = { isEditing = !isEditing }
                            )

                            if (isEditing) {
                                GeneralIconButton(
                                    icon = R.drawable.ic_delete,
                                    onClick = { showDeleteSupplierConfirmation = true }
                                )
                            }
                        }
                    )
                },

                bottomBar = {
                    if (isEditing) {
                        CustomBottomAppBar(
                            enabled = true,
                            onAcceptAction = {
                                Log.d("UpdateDebug", "Supplier: $tempSupplier")

                                val updatedSupplier = uiState.item.copy(
                                    name = tempSupplier.name,
                                    contactName = tempSupplier.contactName,
                                    phone = tempSupplier.phone,
                                    email = tempSupplier.email,
                                    address = tempSupplier.address
                                )

                                updatedSupplier.let {
                                    viewModel.updateSupplier(updatedSupplier)
                                    isEditing = false
                                }
                            },
                            onCancelAction = {
                                viewModel.setInitialSupplier(uiState.item)
                                isEditing = false
                            }
                        )
                    }
                }

            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    SupplierDetailsBody(
                        tempSupplier = tempSupplier,
                        onValueChange = viewModel::updateSupplierFields,
                        validationState = validationState,
                        isEditing = isEditing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(dimensionResource(R.dimen.padding_medium))
                    )
                }
            }
        }
        is DetailsUiState.NotFound -> {}
        is DetailsUiState.Error -> {}
    }
}



@Composable
fun SupplierDetailsBody(
    tempSupplier: Supplier,
    onValueChange: (String, String) -> Unit,
    validationState: SupplierValidationState,
    isEditing: Boolean = false,
    modifier: Modifier = Modifier
) {
    var hasEdited by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Encabezado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = if (tempSupplier.name.isEmpty() && !hasEdited) "Proveedor" else tempSupplier.name,
                    onValueChange = { onValueChange("name", it); hasEdited = true },
                    textStyle = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                    //color = MaterialTheme.colorScheme.primary
                )
                ShowMessageErrorText(validationState.nameError)
            }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = tempSupplier.contactName,
                        onValueChange = { onValueChange("contactName", it) },
                        label = { Text(stringResource(R.string.supplier_contact_name)) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditing,
                        colors = campoColores(),
                        //supportingText = { Text("Especifica $label") }
                    )

                    OutlinedTextField(
                        value = tempSupplier.phone,
                        onValueChange = { onValueChange("phone", it) },
                        label = { Text(stringResource(R.string.supplier_phone)) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditing,
                        colors = campoColores(),
                        //supportingText = { Text("Especifica $label") }
                    )
                    ShowMessageErrorText(validationState.phoneError)

                    OutlinedTextField(
                        value = tempSupplier.email,
                        onValueChange = { onValueChange("email", it) },
                        label = { Text(stringResource(R.string.supplier_email)) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditing,
                        colors = campoColores(),
                        //supportingText = { Text("Especifica $label") }
                    )

                    OutlinedTextField(
                        value = tempSupplier.address,
                        onValueChange = { onValueChange("address", it) },
                        label = { Text(stringResource(R.string.supplier_address)) },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditing,
                        colors = campoColores(),
                        //supportingText = { Text("Especifica $label") }
                    )
                    ShowMessageErrorText(validationState.emailError)
                }

        }
    }
}


@Composable
fun SupplierDetailsCard(
    supplier: SupplierEntity,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        BasicTextField(
            value = supplier.name,
            onValueChange = {},
            readOnly = true,
            textStyle = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
//        OutlinedTextField(
//            value = supplier.name,
//            onValueChange = {},
//            label = { Text(stringResource(R.string.supplier_name)) },
//            modifier = Modifier.fillMaxWidth(),
//            readOnly = true
//        )
        Spacer(modifier = Modifier.padding(dimensionResource(R.dimen.padding_very_small)))
        TextField(
            value = supplier.contactName ?: "No especificado",
            onValueChange = {},
            placeholder = { Text(stringResource(R.string.supplier_contact_name)) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledIndicatorColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                errorIndicatorColor = MaterialTheme.colorScheme.surface
            )
        )
        OutlinedTextField(
            value = supplier.phone ?: "No especificado",
            onValueChange = {},
            label = { Text(stringResource(R.string.supplier_phone)) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        OutlinedTextField(
            value = supplier.email ?: "No especificado",
            onValueChange = {},
            label = { Text(stringResource(R.string.supplier_email)) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )
        OutlinedTextField(
            value = supplier.address ?: "No especificado",
            onValueChange = {},
            label = { Text(stringResource(R.string.supplier_address)) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

//        CustomTextFieldProduct(
//            label = stringResource(R.string.supplier_name),
//            value = supplier.name,
//            onValueChange = {},
//        )
//
//
//        CustomTextFieldProduct(
//            label = stringResource(R.string.supplier_contact_name),
//            value = supplier.contactName ?: "No especificado",
//            onValueChange = {},
//        )
//
//        CustomTextFieldProduct(
//            label = stringResource(R.string.supplier_phone),
//            value = supplier.phone ?: "No especificado",
//            onValueChange = {},
//        )
//
//        CustomTextFieldProduct(
//            label = stringResource(R.string.supplier_email),
//            value = supplier.email ?: "No especificado",
//            onValueChange = {},
//        )
//
//        CustomTextFieldProduct(
//            label = stringResource(R.string.supplier_address),
//            value = supplier.address ?: "No especificado",
//            onValueChange = {},
//            singleLine = false,
//        )
    }
}