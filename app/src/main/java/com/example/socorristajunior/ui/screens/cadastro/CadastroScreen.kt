package com.example.socorristajunior.ui.screens.cadastro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.w3c.dom.CDATASection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(
    navController: NavController,
    // Injeta a ViewModel usando Hilt
    viewModel: CadastroViewModel = hiltViewModel()
) {
    // 1. Estados da UI para os campos de texto
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 2. Coleta o estado de cadastro da ViewModel
    val signUpState by viewModel.signUpState.collectAsState()

    // 3. Efeito colateral para navegar ou mostrar feedback
    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.Success -> {
                println("Cadastro de usuário concluído com sucesso!")

                navController.navigate("login") {
                    // Limpa a tela de cadastro da pilha para que o usuário não volte
                    popUpTo("cadastro") { inclusive = true }
                }
            }
            is SignUpState.Error -> {
                // Lógica para mostrar Snackbar com a mensagem de erro
                val errorMessage = (signUpState as SignUpState.Error).message
                println("Erro de Cadastro: $errorMessage")
                // Você pode usar um Scaffold/SnackbarHost para exibir a mensagem
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Novo Cadastro") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Campo Nome
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome Completo") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Campo Senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha (Mínimo 6 caracteres)") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Botão de Cadastro
            Button(
                onClick = {
                    // Chama a função da ViewModel, iniciando o fluxo Firebase -> Supabase
                    viewModel.signUp(email, name, password)
                },
                enabled = signUpState !is SignUpState.Loading, // Desabilita durante o Loading
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                if (signUpState is SignUpState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("CADASTRAR")
                }
            }

            // Exibe mensagem de erro persistente (se houver)
            if (signUpState is SignUpState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = (signUpState as SignUpState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}