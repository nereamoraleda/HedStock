package es.cursos.android.ejercicios.stocksnma.ui.screen.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.domain.model.Store
import es.cursos.android.ejercicios.stocksnma.domain.model.User
import es.cursos.android.ejercicios.stocksnma.ui.components.ButtonsBottomBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralTopAppBar
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralHorizontalDivider
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralIconButton
import es.cursos.android.ejercicios.stocksnma.utils.enums.UserRoles.Companion.getRoles
import es.cursos.android.ejercicios.stocksnma.utils.UserValidationState


@Composable
fun UserCreationScreen(
    viewModel: UserCreationViewModel,
    navigateBack: () -> Unit
) {
    val user = viewModel.userUiState.item

    val validationState by viewModel.validationState.collectAsState()
    val storesList by viewModel.storeOptions.collectAsState()

        Scaffold(
            topBar = {
                GeneralTopAppBar(
                    title = stringResource(R.string.user_create_title),
                    navigationButton = {
                        GeneralIconButton(
                            icon = R.drawable.ic_arrow_back,
                            onClick = {
                                navigateBack()
                                viewModel.cleanUserUiState()
                            }
                        )
                    },
                )
            },
            bottomBar = {
                ButtonsBottomBar(
                    enabled = viewModel.userUiState.isEntryValid,
                    onAcceptAction = {
                        viewModel.createUser()
                        //navigateBack()
                    },
                    onCancelAction = {
                        viewModel.cleanUserUiState()
                    }
                )
            },

        ) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
            ) {
                UserCreationBodyScreen(
                    user = user,
                    storeList = storesList,
                    onValueChange = viewModel::updateUiState,
                    validationState = validationState
                )
            }
        }
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCreationBodyScreen(
    user: User,
    storeList: List<Store> = emptyList(),
    onValueChange: (User) -> Unit,
    validationState: UserValidationState,
    modifier: Modifier = Modifier
) {
    //var roleSelected by remember { mutableStateOf("") }
    //var storeSelected by remember { mutableStateOf("") }

    var expandedRoleMenu by remember { mutableStateOf(false) }
    var expandedStoreMenu by remember { mutableStateOf(false) }

    //Column(modifier = modifier) {
        Card(
            elevation = CardDefaults.cardElevation(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
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
                    onValueChange = { onValueChange(user.copy(name = it)) },
                    label = { Text(text = stringResource(id = R.string.user_name)) },
                    supportingText = validationState.nameError?.let { { Text(text = it) } },
                    isError = validationState.nameError != null,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = user.username,
                    onValueChange = { onValueChange(user.copy(username = it)) },
                    label = { Text(text = stringResource(id = R.string.user_username)) },
                    supportingText = supportingErrorText(validationState.usernameError),
                    isError = validationState.usernameError != null,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = user.password,
                    onValueChange = { onValueChange(user.copy(password = it)) },
                    label = { Text(text = stringResource(id = R.string.user_password)) },
                    supportingText = supportingErrorText(validationState.passwordError),
                    isError = validationState.passwordError != null,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = user.email,
                    onValueChange = { onValueChange(user.copy(email = it)) },
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
                    onValueChange = { onValueChange(user.copy(phone = it)) },
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
                        supportingText = validationState.roleError?.let { { Text(text = it) } },
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
                                    onValueChange(user.copy(role = roleName.name))
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
                        value = storeList.find { it.id == user.storeId }?.name ?: "",
                        onValueChange = {},
                        label = { Text(text = stringResource(id = R.string.user_store)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStoreMenu) },
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
                            storeList.isEmpty() -> {
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

                                storeList.forEachIndexed { index, store ->
                                    DropdownMenuItem(
                                        text = { Text(text = store.name) },
                                        onClick = {
                                            onValueChange(user.copy(storeId = store.id))
                                            expandedStoreMenu = false
                                        }
                                        //onClick = { storeSelected = storeName.name }
                                    )
                                    if (index != storeList.lastIndex) GeneralHorizontalDivider()
                                }
                            }
                        }
                    }
                }
            }
    }
}


@Composable
fun supportingErrorText(vararg errors: String?): (@Composable (() -> Unit))? {
    val error = errors.firstOrNull { it != null }
    return error?.let { { Text(text = it) } }
}
