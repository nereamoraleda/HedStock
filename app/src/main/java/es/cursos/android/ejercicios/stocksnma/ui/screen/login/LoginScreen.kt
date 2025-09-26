package es.cursos.android.ejercicios.stocksnma.ui.screen.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import es.cursos.android.ejercicios.stocksnma.R
import es.cursos.android.ejercicios.stocksnma.ui.components.coloresOutlinedTextField


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    val loginResult by viewModel.loginState.collectAsState()

    if (loginResult != null) {
        Log.d("LoginScreen", "Login result: $loginResult")
    }

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LoginBodyScreen(
                viewModel = viewModel,
                onLoginSuccess = if (loginResult != null) { onLoginSuccess } else { {} }
            )
        }
    }
}


@Composable
fun LoginBodyScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.loginState.collectAsState()
    val showChangePasswordDialog by viewModel.showChangePasswordDialog.collectAsState()

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var oldPassword by rememberSaveable { mutableStateOf("") }
    var newPassword by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(uiState) {
        val state = uiState
        if (state is LoginUiState.Success && !state.auth.mustChangePassword) {
            onLoginSuccess()
            Log.d("LOGIN-SCREEN-SUCCESS ", "Login result: $uiState")
            //viewModel.resetState()
            //Log.d("LOGIN-SCREEN-SUCCESS ", "Login result: $uiState")
        }
    }

    if (showChangePasswordDialog) {
        Dialog(onDismissRequest = {}) {
            OutlinedCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Cambiar contraseña")

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it },
                        label = { Text("Contraseña actual") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nueva contraseña") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {

                            viewModel.changePassword(
                                username = (uiState as LoginUiState.Success).auth.username, // ⚡ viene del backend
                                token = (uiState as LoginUiState.Success).auth.token,
                                oldPassword = oldPassword,
                                newPassword = newPassword
                            )
                                //viewModel.onShowChangePasswordDialogChanged(false)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Cambiar contraseña")
                    }
                }
            }
        }
    }

    when (uiState) {
        is LoginUiState.Loading -> { CircularProgressIndicator() }
        is LoginUiState.Error -> { Text(text = (uiState as LoginUiState.Error).messageError) }
        else -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hedstock_without_background),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
                Card(
                    elevation = CardDefaults.cardElevation(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(vertical = 20.dp),
                    ) {
                        Text(
                            text = "Login",
                            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text(text = "Email") },
                            colors = coloresOutlinedTextField(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(text = "Password") },
                            colors = coloresOutlinedTextField(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        ElevatedButton(
                            onClick = { viewModel.login(username, "{noop}$password") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Login")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ChangePasswordScreen(
    token: String,
    onPasswordChanged: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Debes cambiar tu contraseña antes de continuar")

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Nueva contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmar contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            onClick = {
                if (newPassword == confirmPassword) {
                    //viewModel.changePassword(token, newPassword)
                    onPasswordChanged()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cambiar contraseña")
        }
    }
}
