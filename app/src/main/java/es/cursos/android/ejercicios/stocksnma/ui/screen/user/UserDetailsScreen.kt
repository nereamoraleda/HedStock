package es.cursos.android.ejercicios.stocksnma.ui.screen.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.ui.components.ConfirmationDialog
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.ErrorContent
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralExposedDropDownBox
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralHorizontalDivider
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralIconButton
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.LoadingContent
import es.cursos.android.ejercicios.stocksnma.ui.components.NavigateBackButton
import es.cursos.android.ejercicios.stocksnma.ui.components.NotFoundContent
import es.cursos.android.ejercicios.stocksnma.ui.components.VerticalScrollableColumn
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles.Companion.getRoles
import es.cursos.android.ejercicios.stocksnma.utils.UserValidationState
import kotlinx.coroutines.launch

@Composable
fun UserDetailsScreen(
    viewModel: UserDetailsViewModel,
    userId: Long,
    onNavigateBack: () -> Unit
) {
    // -------------------- VARIABLES -------------------- //
    val state by viewModel.uiState.collectAsState()                    // Estado de la UI (Loading, Success...)
    val user by viewModel.userEditable.collectAsState()                // Datos del usuario
    val validationState by viewModel.validationState.collectAsState()  // Estado de validación de campos
    val storeOptions by viewModel.storeOptions.collectAsState()        // Listado de tiendas disponibles
    val resetResult by viewModel.resetResult.collectAsState()          // Resultado del reseteo de contraseña (True/False)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var showConfirmationDialog by remember { mutableStateOf(false) }  // Confirmación de eliminación de usuario
    var isMenuExpanded by remember { mutableStateOf(false) }          // Menu desplegable de opciones del usuario


    // Cargar datos del usuario
    LaunchedEffect(userId) {
        viewModel.loadUserDetails(userId)
    }


    // -------------------- UI -------------------- //
    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.user_details_title),
                navigationButton = { NavigateBackButton(onNavigateBack = onNavigateBack) },
                actionButton = {
                    Box(contentAlignment = Alignment.Center) {

                        // Abrir menú desplegable
                        GeneralIconButton(
                            onClick = { isMenuExpanded = true },
                            icon = R.drawable.ic_menu_filter,
                        )

                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                        ) {

                            // Opción de reseteo de contraseña
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.resetPassword()
                                    scope.launch {
                                        if (resetResult) snackbarHostState.showSnackbar("Contraseña del usuario \"${user.username}\" reseteada")
                                        else snackbarHostState.showSnackbar("Error al resetear la contraseña")
                                    }
                                },
                                text = {
                                    Text(
                                        text = stringResource(id = R.string.reset_password),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_8dp))
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_key),
                                        contentDescription = null
                                    )
                                }
                            )

                            GeneralHorizontalDivider()

                            // Opción de eliminación de usuario
                            DropdownMenuItem(
                                onClick = { showConfirmationDialog = true },
                                text = {
                                    Text(
                                        text = stringResource(id = R.string.delete_user),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_8dp))
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete_forever),
                                        contentDescription = null
                                    )
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.error,
                                    leadingIconColor = MaterialTheme.colorScheme.error,
                                    trailingIconColor = MaterialTheme.colorScheme.error
                                )
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (state is DetailsUiState.Success) {
                ButtonsBottomBar(
                    onAcceptAction = {
                        viewModel.saveUserUpdates { success ->
                            if (success) onNavigateBack()
                        }
                    },
                    onCancelAction = { viewModel.resetUiState() },
                    enabled = (state as DetailsUiState.Success).isEntryValid,
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (state) {
                is DetailsUiState.Loading -> { LoadingContent() }
                is DetailsUiState.NotFound -> { NotFoundContent(stringResource(R.string.user_not_found)) }
                is DetailsUiState.Error -> { ErrorContent(stringResource(R.string.message_error, (state as DetailsUiState.Error).messageError)) }
                is DetailsUiState.Success -> {
                    UserDetailsBody(
                        user = user,
                        onFieldChange = viewModel::updateUserEditable,
                        validationState = validationState,
                        storeOptions = storeOptions
                    )
                }
            }
        }
    }

    // Dialog - Confirmación de eliminación de usuario
    /*TODO - Cambiar apariencia del diálogo*/
    if (showConfirmationDialog) {
        ConfirmationDialog(
            title = stringResource(id = R.string.user_delete_title),
            message = stringResource(id = R.string.user_delete_message, user.username),
            onDismissRequest = { showConfirmationDialog = false },
            onConfirmAction = {
                viewModel.deleteUser()
                showConfirmationDialog = false
                onNavigateBack()
            },
            confirmButtonText = stringResource(id = R.string.button_delete),
        )
    }
}


@Composable
fun UserDetailsBody(
    user: User,
    onFieldChange: (String, Any) -> Unit,
    validationState: UserValidationState,
    storeOptions: List<Store>
) {
    // -------------------- VARIABLES -------------------- //
    var expandedRoleMenu by remember { mutableStateOf(false) }   // Menu desplegable de roles
    var expandedStoreMenu by remember { mutableStateOf(false) }  // Menu desplegable de tiendas

    VerticalScrollableColumn {
        GeneralCard {
            // Cabecera de la tarjeta de detalles de usuario
            HeaderUserDetailsCard()

            // Cuerpo de la tarjeta de detalles de usuario
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_16dp))
            ) {
                // Campo de texto para el nombre (Nombre y apellidos)
                GeneralOutlinedTextField(
                    value = user.name,
                    onValueChange = { value -> onFieldChange("name", value) },
                    label = stringResource(id = R.string.user_name),
                    supportingText = supportingErrorText(validationState.nameError),
                    isError = validationState.nameError != null,
                )

                // Campo de texto para el nombre de usuario (no editable)
                GeneralOutlinedTextField(
                    value = user.username,
                    onValueChange = {  },
                    label = stringResource(id = R.string.user_username),
                    enabled = false
                )

                // Campo de texto para el email
                GeneralOutlinedTextField(
                    value = user.email,
                    onValueChange = { onFieldChange("email", it) },
                    label = stringResource(id = R.string.user_email),
                    supportingText = supportingErrorText(validationState.emailOrPhoneError, validationState.emailError),
                    isError = (validationState.emailOrPhoneError != null) || (validationState.emailError != null),
                    keyboardType = KeyboardType.Email
                )

                // Campo de texto para el teléfono
                GeneralOutlinedTextField(
                    value = user.phone,
                    onValueChange = { onFieldChange("phone", it) },
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
                    label = stringResource(id = R.string.user_role)
                ) {
                    getRoles().forEachIndexed { index, roleName ->
                        DropdownMenuItem(
                            text = { Text(text = roleName.name) },
                            onClick = {
                                onFieldChange("role", roleName.name)
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
                    valueSelected = user.storeName ?: "",
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
                                    onFieldChange("storeId", "")
                                    expandedStoreMenu = false
                                }
                            )

                            GeneralHorizontalDivider()

                            storeOptions.forEachIndexed { index, store ->
                                DropdownMenuItem(
                                    text = { Text(text = store.name) },
                                    onClick = {
                                        onFieldChange("storeId", store.id)
                                        onFieldChange("storeName", store.name)
                                        expandedStoreMenu = false
                                    }
                                )
                                if (index != storeOptions.lastIndex) GeneralHorizontalDivider()
                            }
                        }
                    }
                }

                // Switch para activar/desactivar el usuario
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.user_active))
                    Switch(
                        checked = user.isActive,
                        onCheckedChange = { active ->
                            onFieldChange("isActive", active)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun HeaderUserDetailsCard() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_12dp)),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_16dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_user_no_photo),
            contentDescription = stringResource(id = R.string.profile_image_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .padding(dimensionResource(id = R.dimen.padding_4dp))
                .clip(CircleShape)
                .clickable { /*TODO*/ }
        )

        ElevatedButton(
            onClick = { /*TODO*/ },
            enabled = false
        ) {
            Text(text = stringResource(id = R.string.user_add_photo))
        }
    }
}
