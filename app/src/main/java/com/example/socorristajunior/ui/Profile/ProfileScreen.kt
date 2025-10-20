package com.example.socorristajunior.ui.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.BottomNavigationBar
import com.example.socorristajunior.ui.login.LOGIN_ROUTE
import com.example.socorristajunior.ui.login.MAIN_SCREEN_ROUTE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val user = state.user
    val isUserLoggedIn = user?.isLoggedIn == true // ⭐️ CHAVE: Status de Login

    // Lógica de Redirecionamento após Logout (Para evitar que o usuário fique no Profile deslogado)
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
                actions = {
                    if (isUserLoggedIn) {
                        IconButton(onClick = { navController.navigate("edit_profile") }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar Perfil")
                        }
                    }
                }
            )
        },
        // A BottomNavigationBar só aparece se estiver logado
        bottomBar = { if (isUserLoggedIn) BottomNavigationBar(navController) }
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

            // ----------------------------------------
            // ⭐️ CONTEÚDO CONDICIONAL ⭐️
            // ----------------------------------------
            if (isUserLoggedIn) {
                // 1. USUÁRIO LOGADO: MOSTRA DETALHES E BOTÃO SAIR

                // Header com dados reais
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFF1F2F45)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Foto de Perfil",
                            tint = Color.White,
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = user?.username ?: "Usuário Junior",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = user?.email ?: "Email não informado",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Detalhes do Usuário (usando ProfileDetailItem)
                ProfileDetailItem(label = "Gênero", value = user?.gender ?: "Não informado")
                ProfileDetailItem(label = "Nascimento", value = user?.dateOfBirth ?: "Não informado")
                ProfileDetailItem(label = "Telefone", value = user?.phone ?: "Não informado")

                Spacer(modifier = Modifier.height(32.dp))

                // Menu Itens e Botão de SAIR
                ProfileMenuItem(title = "Meu Desempenho", onClick = { /* TODO */ })
                ProfileMenuItem(title = "Alterar Senha", onClick = { /* TODO */ })
                ProfileMenuItem(title = "Suporte", onClick = { /* TODO */ })

                ProfileMenuItem(
                    title = "Sair",
                    textColor = Color(0xFFE51F2D),
                    onClick = { viewModel.signOut() } // Chama a função de Logout
                )

            } else {
                // 2. USUÁRIO DESLOGADO: MOSTRA BOTÃO DE LOGIN/CADASTRO (TRANSFERIDO)

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Acesso Necessário",
                    tint = Color.Gray,
                    modifier = Modifier.size(120.dp).padding(bottom = 16.dp)
                )

                Text(
                    text = "Acesse sua conta para ver seu perfil completo.",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // ⭐️ BOTÃO DE LOGIN/CADASTRO
                Button(
                    onClick = {
                        // Navega para a tela de Login/Cadastro
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
            color = Color.Gray,
            fontSize = 14.sp
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    textColor: Color = Color.Black,
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
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}