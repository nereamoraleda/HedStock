package es.cursos.android.ejercicios.stocksnma.ui.screen.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.ui.components.CardColumn
import es.cursos.android.ejercicios.stocksnma.ui.components.ErrorContent
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralCard
import es.cursos.android.ejercicios.stocksnma.ui.components.GeneralOutlinedTextField
import es.cursos.android.ejercicios.stocksnma.ui.components.LoadingContent
import es.cursos.android.ejercicios.stocksnma.ui.components.supportingErrorText

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navigateToHome: () -> Unit
) {
    // -------------------- VARIABLES -------------------- //
    val uiState by viewModel.loginState.collectAsState()                                 // Estado de la UI (loading, success, error)
    val showChangePasswordDialog by viewModel.showChangePasswordDialog.collectAsState()  // Dialog de cambio de contraseña


    // Comprobar si el usuario ha iniciado sesión correctamente y navegar a la pantalla Home
    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success && !(uiState as LoginUiState.Success).auth.mustChangePassword) {
            navigateToHome()
            Log.d("LOGIN-SCREEN-SUCCESS ", "Login result: $uiState")
        }
    }

    // -------------------- UI -------------------- //
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LoginBodyScreen(viewModel = viewModel)
        }
    }


    // Dialog - Cambio de contraseña
    if (showChangePasswordDialog) {
        ChangePasswordDialog { currentPassword, newPassword ->
            viewModel.changePassword(
                token = (uiState as LoginUiState.Success).auth.token,
                username = (uiState as LoginUiState.Success).auth.username,
                currentPassword = currentPassword,
                newPassword = newPassword
            )
        }
    }
}


@Composable
fun LoginBodyScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    // -------------------- VARIABLES -------------------- //
    val uiState by viewModel.loginState.collectAsState()                    // Estado de la UI (loading, success, error)
    val validationState by viewModel.loginValidationState.collectAsState()  // Estado de validación de campos de texto
    val isEntryValid by viewModel.isEntryValid.collectAsState()             // Booleano que indica si los campos de texto son válidos

    val username by viewModel.username.collectAsState()  // Campo de texto para el nombre de usuario
    val password by viewModel.password.collectAsState()  // Campo de texto para la contraseña


    // -------------------- UI -------------------- //
    when (uiState) {
        is LoginUiState.Loading -> { LoadingContent() }
        is LoginUiState.Error -> { ErrorContent(messageError = (uiState as LoginUiState.Error).messageError) }
        else -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_16dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_logo_no_background),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp)
                )

                GeneralCard {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_8dp)),
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_16dp))
                            .padding(vertical = dimensionResource(R.dimen.padding_20dp)),
                    ) {
                        Text(
                            text = stringResource(id = R.string.login_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        GeneralOutlinedTextField(
                            value = username,
                            onValueChange = { viewModel.onCredentialsChanged(it, password) },
                            label = stringResource(id = R.string.login_username),
                            supportingText = supportingErrorText(validationState.usernameErrorMessage),
                            isError = (validationState.usernameErrorMessage != null) || (validationState.credentialsErrorMessage != null)
                        )

                        GeneralOutlinedTextField(
                            value = password,
                            onValueChange = { viewModel.onCredentialsChanged(username, it) },
                            label = stringResource(id = R.string.login_password),
                            supportingText = supportingErrorText(validationState.passwordErrorMessage),
                            isError = (validationState.passwordErrorMessage != null) || (validationState.credentialsErrorMessage != null)
                        )

                        // Mostrar mensaje de error si las credenciales son incorrectas
                        if (validationState.credentialsErrorMessage != null) {
                            Text(
                                text = validationState.credentialsErrorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        ElevatedButton(
                            onClick = { viewModel.login(username, "{noop}$password") },
                            enabled = isEntryValid,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.login_button))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ChangePasswordDialog(onChangePassword: (String, String) -> Unit) {
    var currentPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }

    Dialog(
        onDismissRequest = {}  // No se podrá cerrar el Dialog hasta que se modifique la contraseña
    ) {
        GeneralCard {
            CardColumn {
                Text(
                    text = stringResource(id = R.string.change_password_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                GeneralOutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = stringResource(id = R.string.change_password_current_password)
                )

                GeneralOutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = stringResource(id = R.string.change_password_new_password)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onChangePassword(currentPassword, newPassword) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.change_password_button))
                }
            }
        }
    }
}
