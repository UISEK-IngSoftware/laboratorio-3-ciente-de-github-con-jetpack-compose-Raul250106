package ec.edu.uisek.githubclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ec.edu.uisek.githubclient.services.AuthService
import ec.edu.uisek.githubclient.ui.theme.GithubClientTheme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ec.edu.uisek.githubclient.viewmodels.LoginViewModel

@Composable
fun LoginForm(
    onLoginSuccess: () -> Unit = {},
    viewModel: LoginViewModel = viewModel()
){
    val context = LocalContext.current
    val authService = remember { AuthService(context) }
    var username by remember { mutableStateOf(value = "") }
    var token by remember { mutableStateOf(value = "") }

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Ingreso a cliente Github",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            if (errorMsg != null) {
                Text(
                    text = errorMsg!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            OutlinedTextField(
                value = username,
                onValueChange = {username = it},
                label = {Text(text="Usuario")},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            OutlinedTextField(
                value = token,
                onValueChange = {token = it},
                label = {Text(text="Token de github")},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(height = 16.dp))
            Button(
                onClick = {
                    viewModel.login(authService, username, token)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = username.isNotBlank() && token.isNotBlank() && !isLoading
            ) {
                Text(text = if (isLoading) "Verificando..." else "Ingresar")
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    GithubClientTheme {
        LoginForm()
    }
}
