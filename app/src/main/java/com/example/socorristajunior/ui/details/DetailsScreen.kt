package com.example.socorristajunior.ui.details

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
import com.example.socorristajunior.ui.emergencies.EmergenciesViewModel
import com.example.socorristajunior.ui.theme.sRed
import com.example.socorristajunior.ui.theme.sWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyDetailScreen(
    emergencyId: Int,
    navController: NavController,
    viewModel: EmergenciesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = emergencyId) {
        viewModel.onEmergencySelected(emergencyId)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(navController = navController)
        }
    ) { innerPadding ->
        /*EmergencyDetailContent(
            steps = uiState.stepsList,
        )*/
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
             items(uiState.stepsList) { step ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = sWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // A Row agora contém APENAS o texto do passo
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Lógica do ícone foi completamente removida daqui.
                            Text(
                                text = "Passo ${step.stepNumber}/${step.totalSteps}",
                                fontWeight = FontWeight.Bold,
                                color = sRed
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = step.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = step.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
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