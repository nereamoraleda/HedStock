package es.cursos.android.ejercicios.stocksnma.ui.screen.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralExposedDropDownBox
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralHorizontalDivider
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.VerticalScrollableColumn
import es.cursos.android.ejercicios.stocksnma.ui.components.supportingErrorText
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles.Companion.getRoles
import es.cursos.android.ejercicios.stocksnma.utils.UserValidationState

@Composable
fun UserCreationScreen(
    viewModel: UserCreationViewModel,
    onNavigateBack: () -> Unit
) {
    // -------------------- VARIABLES -------------------- //
    val user = viewModel.userUiState.item  // Datos del usuario a crear

    val validationState by viewModel.validationState.collectAsState()  // Estado de validación de campos
    val storeOptions by viewModel.storeOptions.collectAsState()        // Listado de tiendas disponibles


    // -------------------- UI -------------------- //
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.user_create_title),
                navigationButton = {
                    NavigateBackButton(onNavigateBack = {
                        onNavigateBack()
                        viewModel.resetUiState()
                    })
                },
            )
        },
        bottomBar = {
            ButtonsBottomBar(
                onAcceptAction = { viewModel.saveNewUser() },
                onCancelAction = { viewModel.resetUiState() },
                enabled = viewModel.userUiState.isEntryValid
            )
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            UserCreationBodyScreen(
                user = user,
                onValueChange = viewModel::updateUiState,
                validationState = validationState,
                storeOptions = storeOptions,
            )
        }
    }
}


@Composable
fun UserCreationBodyScreen(
    user: User,
    onValueChange: (User) -> Unit,
    validationState: UserValidationState,
    storeOptions: List<Store>,
    modifier: Modifier = Modifier
) {
    // -------------------- VARIABLES -------------------- //
    var expandedRoleMenu by remember { mutableStateOf(false) }   // Menu desplegable de roles
    var expandedStoreMenu by remember { mutableStateOf(false) }  // Menu desplegable de tiendas


    // -------------------- UI -------------------- //
    VerticalScrollableColumn {
        GeneralCard {
            // Cabecera de la tarjeta de creación de usuario
            UserFormHeader()

            // Cuerpo de la tarjeta de creación de usuario
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_16dp))
            ) {
                // Campo de texto para el nombre (Nombre y apellidos)
                GeneralOutlinedTextField(
                    value = user.name,
                    onValueChange = { onValueChange(user.copy(name = it)) },
                    label = stringResource(id = R.string.user_name),
                    supportingText = supportingErrorText(validationState.nameError),
                    isError = validationState.nameError != null,
                )

                // Campo de texto para el nombre de usuario (único)
                GeneralOutlinedTextField(
                    value = user.username,
                    onValueChange = { onValueChange(user.copy(username = it)) },
                    label = stringResource(id = R.string.user_username),
                    supportingText = supportingErrorText(validationState.usernameError),
                    isError = validationState.usernameError != null
                )

                // Campo de texto para el email (único)
                GeneralOutlinedTextField(
                    value = user.email,
                    onValueChange = { onValueChange(user.copy(email = it)) },
                    label = stringResource(id = R.string.user_email),
                    supportingText = supportingErrorText(validationState.emailOrPhoneError, validationState.emailError),
                    isError = (validationState.emailOrPhoneError != null) || (validationState.emailError != null),
                    keyboardType = KeyboardType.Email
                )

                // Campo de texto para el teléfono (único)
                GeneralOutlinedTextField(
                    value = user.phone,
                    onValueChange = { onValueChange(user.copy(phone = it)) },
                    label = stringResource(id = R.string.user_phone),
                    supportingText = supportingErrorText(validationState.emailOrPhoneError, validationState.phoneError),
                    isError = (validationState.emailOrPhoneError != null) || (validationState.phoneError != null),
                    keyboardType = KeyboardType.Phone
                )

                // Campo de texto para el rol (seleccionable)
                GeneralExposedDropDownBox(
                    expandedMenu = expandedRoleMenu,
                    onExpandedChange = { expandedRoleMenu = it },
                    valueSelected = user.role,
                    label = stringResource(id = R.string.user_role),
                    supportingText = supportingErrorText(validationState.roleError),
                    isError = validationState.roleError != null
                ) {
                    getRoles().forEachIndexed { index, roleName ->
                        DropdownMenuItem(
                            text = { Text(text = roleName.name) },
                            onClick = {
                                onValueChange(user.copy(role = roleName.name))
                                expandedRoleMenu = false
                            }
                        )
                        if (index != getRoles().lastIndex) GeneralHorizontalDivider()
                    }
                }

                // Campo de texto para la tienda (seleccionable)
                GeneralExposedDropDownBox(
                    expandedMenu = expandedStoreMenu,
                    onExpandedChange = { expandedStoreMenu = it },
                    valueSelected = storeOptions.find { it.id == user.storeId }?.name ?: "",
                    label = stringResource(id = R.string.user_store)
                ) {
                    when {
                        storeOptions.isEmpty() -> {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.no_stores_available)) },
                                onClick = { expandedStoreMenu = false }
                            )
                        }
                        else -> {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(R.string.no_store)) },
                                onClick = {
                                    onValueChange(user.copy(storeId = null))
                                    expandedStoreMenu = false
                                }
                            )

                            GeneralHorizontalDivider()

                            storeOptions.forEachIndexed { index, store ->
                                DropdownMenuItem(
                                    text = { Text(text = store.name) },
                                    onClick = {
                                        onValueChange(user.copy(storeId = store.id))
                                        expandedStoreMenu = false
                                    }
                                )
                                if (index != storeOptions.lastIndex) GeneralHorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}
