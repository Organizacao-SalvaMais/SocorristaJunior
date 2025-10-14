package com.example.socorristajunior.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.EmergencyDetailContent
import com.example.socorristajunior.ui.screens.emergencies.EmergenciesViewModel
import com.example.socorristajunior.ui.theme.sRed
import com.example.socorristajunior.ui.theme.sWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyDetailScreen(
    emergencyId: Int,
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = emergencyId) {
        viewModel.loadSteps(emergencyId)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                // --- OTIMIZAÇÃO 4: Usando o Componente Estilizado ---
                // Agora que a lógica está correta, podemos usar seu EmergencyDetailContent.
                // Ele é mais eficiente que a LazyColumn para esta finalidade, pois
                // usa uma Column rolável simples, ideal para um número fixo de passos.
                EmergencyDetailContent(steps = uiState.stepsList)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Anotação necessária para TopAppBar
@Composable
fun SmallTopAppBar(navController: NavController) { // Assinatura de exemplo, ajuste se necessário
    // Implementação real da TopAppBar
    TopAppBar(
        // Título que aparecerá na barra
        title = { Text("Detalhes da Emergência") },
        // Ícone de navegação para voltar
        navigationIcon = {
            // Botão com ícone clicável
            IconButton(onClick = { navController.popBackStack() }) {
                // Ícone de seta para a esquerda
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        }
    )
}