package com.example.socorristajunior.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.BottomNavigationBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.socorristajunior.ui.screens.login.LOGIN_ROUTE
import com.example.socorristajunior.ui.screens.login.MAIN_SCREEN_ROUTE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.user
    val isUserLoggedIn = user?.isLoggedIn == true

    // Coleta o estado de exibição do diálogo
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()

    // Lógica de Navegação após Logout
    LaunchedEffect(isUserLoggedIn) {
        if (!isUserLoggedIn && !state.isLoading) {
            // Após o logout, navegamos para a tela principal (Home).
            // O Home deve saber lidar com usuários deslogados.
            navController.navigate(MAIN_SCREEN_ROUTE) {
                popUpTo(MAIN_SCREEN_ROUTE) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isUserLoggedIn) "Meu Perfil" else "Acesso") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar para Home")
                    }
                },
                actions = {
                    if (isUserLoggedIn) {
                        IconButton(onClick = { navController.navigate("edit_profile") }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Perfil")
                        }
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // ⭐️ Centraliza verticalmente o conteúdo se o usuário não estiver logado
            verticalArrangement = if (isUserLoggedIn) Arrangement.Top else Arrangement.Center
        ) {

            if (state.isLoading) {
                CircularProgressIndicator()
                return@Column
            }

            if (isUserLoggedIn) {
                // 1. USUÁRIO LOGADO: MOSTRA DETALHES E BOTÃO SAIR

                // Header com dados reais
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de Perfil",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = user?.username ?: "Usuário",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = user?.email ?: "Email não informado",
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }


                Spacer(modifier = Modifier.height(32.dp))

                ProfileMenuItem(
                    title = "Excluir Conta",
                    textColor = MaterialTheme.colorScheme.error,
                    onClick = { viewModel.openDeleteDialog() }
                )

                // Adicione o Diálogo de Confirmação (Pode ser fora do Column principal)
                if (showDeleteDialog) {
                    DeleteAccountConfirmationDialog(
                        onDismiss = viewModel::closeDeleteDialog,
                        onConfirm = viewModel::reauthenticateAndDelete
                    )
                }

                ProfileMenuItem(title = "Meu Desempenho", onClick = { /* TODO */ })
                ProfileMenuItem(title = "Alterar Senha", onClick = {
                    navController.navigate("forgot_password")
                })
                ProfileMenuItem(title = "Suporte", onClick = { /* TODO */ })

                ProfileMenuItem(
                    title = "Sair",
                    textColor = MaterialTheme.colorScheme.error,
                    onClick = { viewModel.signOut() } // Chama a função de Logout
                )

            } else {
                // 2. USUÁRIO DESLOGADO: MOSTRA BOTÃO DE LOGIN/CADASTRO
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Acesso Necessário",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(120.dp).padding(bottom = 16.dp)
                )

                Text(
                    text = "Acesse sua conta para ver seu perfil completo.",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // ⭐️ BOTÃO DE LOGIN/CADASTRO
                Button(
                    onClick = {
                        navController.navigate(LOGIN_ROUTE)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("FAZER LOGIN / CADASTRE-SE")
                }
            }
        }
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(100.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DeleteAccountConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: (password: String) -> Unit
) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Exclusão de Conta") },
        text = {
            Column {
                Text("Para sua segurança, confirme sua senha para deletar permanentemente sua conta. Esta ação não pode ser desfeita.")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Confirme sua Senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(password) },
                enabled = password.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Deletar Permanentemente")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}