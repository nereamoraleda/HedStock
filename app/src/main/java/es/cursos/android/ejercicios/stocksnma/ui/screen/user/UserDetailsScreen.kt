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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.ui.components.ConfirmationDialog
import es.cursos.android.ejercicios.stocksnma.ui.components.CustomBottomAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralHorizontalDivider
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralIconButton
import es.cursos.android.ejercicios.stocksnma.ui.state.DetailsUiState
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles.Companion.getRoles
import es.cursos.android.ejercicios.stocksnma.utils.UserValidationState
import androidx.compose.material3.OutlinedTextField as OutlinedTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    viewModel: UserDetailsViewModel,
    userId: Long,
    navigateBack: () -> Unit
) {
    LaunchedEffect(userId) {
        viewModel.loadUserDetails(userId)
    }

    val state by viewModel.uiState.collectAsState()
    val user by viewModel.userEditable.collectAsState()
    val validationState by viewModel.validationState.collectAsState()
    val storeOptions by viewModel.storeOptions.collectAsState()

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showChangeCredentialsDialog by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            GeneralTopAppBar(
                title = stringResource(R.string.user_details_title),
                navigationButton = {
                    GeneralIconButton(
                        icon = R.drawable.ic_arrow_back,
                        onClick = navigateBack
                    )
                },
                actionButton = {
                    Box(contentAlignment = Alignment.Center) {
                        GeneralIconButton(
                            onClick = { isMenuExpanded = true },
                            icon = R.drawable.ic_menu_filter,
                        )

                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                        ) {
                            DropdownMenuItem(
                                onClick = { viewModel.resetPassword() },
                                text = {
                                    Text(
                                        text = "Cambiar credenciales",//stringResource(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
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

                            DropdownMenuItem(
                                onClick = { showConfirmationDialog = true },
                                text = {
                                    Text(
                                        text = "Eliminar usuario",//stringResource(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
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
            val uiState = state
            if (uiState is DetailsUiState.Success) {
                CustomBottomAppBar(
                    enabled = uiState.isEntryValid,
                    onAcceptAction = {
                        viewModel.saveUserUpdates { success ->
                            if (success) navigateBack()
                        }
                    },
                    onCancelAction = { viewModel.resetUiState() }
                )
            }
        }

    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (val uiState = state) {
                is DetailsUiState.Loading -> { CircularProgressIndicator(modifier = Modifier.size(50.dp)) }
                is DetailsUiState.NotFound -> { Text("Producto no encontrado") }
                is DetailsUiState.Error -> { Text("Error: ${uiState.messageError}") }
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

    if (showConfirmationDialog) {
        ConfirmationDialog(
            title = "Eliminar usuario",
            message = "¿Está seguro de eliminar este usuario?, la acción no se puede deshacer",
            onDismissRequest = { showConfirmationDialog = false },
            onConfirmAction = {
                viewModel.deleteUser()
                showConfirmationDialog = false
                navigateBack()
            },
            confirmButtonText = "Aceptar",
        )
    }

    if (showChangeCredentialsDialog) {
        Dialog(
            onDismissRequest = { showChangeCredentialsDialog = false }
        ) {
            Card() {
                Text(text = "Cambiar credenciales")
                OutlinedTextField(value = "", onValueChange = {})
                OutlinedTextField(value = "", onValueChange = {})
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsBody(
    user: User,
    onFieldChange: (String, Any) -> Unit,
    validationState: UserValidationState,
    storeOptions: List<Store>
) {

    // Variables - Menú desplegable
    var expandedRoleMenu by remember { mutableStateOf(false) }
    var expandedStoreMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            elevation = CardDefaults.cardElevation(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_user_no_photo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(4.dp)
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


            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = user.name,
                    onValueChange = { onFieldChange("name", it) },
                    label = { Text(text = stringResource(id = R.string.user_name)) },
                    supportingText = supportingErrorText(validationState.nameError),
                    isError = validationState.nameError != null,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = user.username,
                    onValueChange = {  },
                    label = { Text(text = stringResource(id = R.string.user_username)) },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = user.email,
                    onValueChange = { onFieldChange("email", it) },
                    label = { Text(text = stringResource(id = R.string.user_email)) },
                    supportingText = supportingErrorText(
                        validationState.emailOrPhoneError,
                        validationState.emailError
                    ),
                    isError = (validationState.emailOrPhoneError != null) || (validationState.emailError != null),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = user.phone,
                    onValueChange = { onFieldChange("phone", it) },
                    label = { Text(text = stringResource(id = R.string.user_phone)) },
                    supportingText = supportingErrorText(
                        validationState.emailOrPhoneError,
                        validationState.phoneError
                    ),
                    isError = (validationState.emailOrPhoneError != null) || (validationState.phoneError != null),
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = expandedRoleMenu,
                    onExpandedChange = { expandedRoleMenu = !expandedRoleMenu }
                ) {
                    OutlinedTextField(
                        value = user.role,
                        onValueChange = {},
                        label = { Text(text = stringResource(id = R.string.user_role)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRoleMenu) },
                        supportingText = supportingErrorText(validationState.roleError),
                        isError = validationState.roleError != null,
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedRoleMenu,
                        onDismissRequest = { expandedRoleMenu = false }
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
                }

                ExposedDropdownMenuBox(
                    expanded = expandedStoreMenu,
                    onExpandedChange = { expandedStoreMenu = !expandedStoreMenu }
                ) {
                    OutlinedTextField(
                        value = user.storeName ?: "",
                        onValueChange = {},
                        label = { Text(text = stringResource(id = R.string.user_store)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRoleMenu) },
                        //supportingText = {},
                        isError = false,
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedStoreMenu,
                        onDismissRequest = { expandedStoreMenu = false }
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
                }

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
