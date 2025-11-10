package com.example.socorristajunior.ui.screens.forgotPassorword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.socorristajunior.ui.screens.cadastro.CadastroViewModel// Use seu ViewModel de autenticação
import com.example.socorristajunior.ui.screens.cadastro.ResetState
import kotlinx.coroutines.launch

const val FORGOT_PASSWORD_ROUTE = "forgot_password"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: CadastroViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }

    // Coleta o estado de envio do link (Success/Error)
    val resetState by viewModel.resetPasswordState.collectAsState()


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Efeito colateral para mostrar o resultado final ao usuário
    LaunchedEffect(resetState) {
        if (resetState is ResetState.Success) {
            scope.launch {
                snackbarHostState.showSnackbar((resetState as ResetState.Success).message)
            }
        } else if (resetState is ResetState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar((resetState as ResetState.Error).message)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Esqueci a Senha") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Insira seu email para receber o link de redefinição.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Botão de Envio
            Button(
                onClick = {
                    viewModel.sendPasswordResetEmail(email)
                },
                enabled = resetState !is ResetState.Loading && email.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (resetState is ResetState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("ENVIAR LINK")
                }
            }
        }
    }
}