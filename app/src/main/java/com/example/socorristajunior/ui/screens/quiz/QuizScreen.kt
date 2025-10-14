package com.example.socorristajunior.ui.screens.quiz

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.BottomNavigationBar



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    // viewModel: QuizViewModel = hiltViewModel()
) {
    // val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    // Tela de Quiz
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar para Home")
                    }
                }
            )
        },
        bottomBar = {
            // Função da NavBar | | Label
            BottomNavigationBar(navController)
        }
    )
    { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize() // Ocupa todos o espaço disponível
                .padding(innerPadding) // Aplica o padding do Scaffold
                .padding(horizontal = 24.dp), // Adiciona um padding nas laterais
            contentAlignment = Alignment.Center // Alinha o conteúdo no centro
        ) {
            // Coluna para organizar os itens verticalmente
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Texto de instrução para o usuário
                Text(
                    text = "Teste seus conhecimentos em nosso site!",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 22.sp, // Tamanho da fonte
                    textAlign = TextAlign.Center // Alinha o texto no centro
                )
                // Espaçador vertical
                Spacer(modifier = Modifier.height(24.dp))
                // Botão para abrir o site
                Button(onClick = {
                    // Cria uma URI (endereço web) a partir da string do seu site.
                    val uri = Uri.parse("https://salvarmais.cloud")
                    // Cria uma Intent com a ação de VISUALIZAR (ACTION_VIEW) a URI.
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    // Usa o contexto para iniciar a atividade (abrir o navegador com o site).
                    context.startActivity(intent)
                }) {
                    // Texto dentro do botão
                    Text("Acessar Quizzes")
                }
            }
        }
    }
}