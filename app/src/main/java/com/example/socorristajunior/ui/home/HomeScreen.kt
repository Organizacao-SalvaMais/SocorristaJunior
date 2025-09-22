package com.example.socorristajunior.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.ui.components.BarraDePesquisa
import com.example.socorristajunior.ui.components.FeatureCard // Verifique se este import está aqui
import androidx.lifecycle.viewmodel.compose.viewModel // Importe viewModel se houver aviso de depreciação

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*Spacer(modifier = Modifier.height(16.dp))
            BarraDePesquisa(
                searchText = uiState.searchText,
                onSearchTextChanged = viewModel::onSearchTextChanged,
                onSearchSubmit = viewModel::onSearchSubmit
            )
            Spacer(modifier = Modifier.height(16.dp))
            */
            // --- CÓDIGO NOVO ADICIONADO AQUI ---

            // Card do Quiz
            FeatureCard(
                icon = Icons.Filled.Psychology,
                iconColor = Color(0xFFE57373),
                title = "Quiz Interativo",
                description = "Teste seus conhecimentos sobre primeiros socorros com nosso quiz educativo.",
                buttonText = "Iniciar Quiz",
                buttonColor = Color(0xFFF2D16B),
                onClick = {
                    navController.navigate("quiz")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card de Emergências
            FeatureCard(
                icon = Icons.Default.Warning,
                iconColor = Color(0xFFD92B2B),
                title = "Emergências",
                description = "Passo a passo para situações de emergência.",
                buttonText = "Ver Procedimentos",
                buttonColor = Color(0xFFD92B2B),
                onClick = {
                    navController.navigate("emergencies")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- FIM DO CÓDIGO NOVO ---


            // O restante da tela continua igual
            /*if (uiState.isLoading) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                ListaDeEmergencias(
                    emergencias = uiState.emergencias,
                    modifier = Modifier.weight(1f)
                )
            }*/
        }
    }
}

// As funções ListaDeEmergencias e CardEmergencia continuam as mesmas
@Composable
fun ListaDeEmergencias(
    emergencias: List<Emergencia>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(emergencias) { emergencia ->
            CardEmergencia(emergencia = emergencia)
        }
    }
}

@Composable
fun CardEmergencia(emergencia: Emergencia) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = emergencia.emernome, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = emergencia.emerdesc)
        }
    }
}