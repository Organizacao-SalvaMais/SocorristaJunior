package com.example.socorristajunior.ui.emergencies

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.socorristajunior.Data.model.Emergencia // Importe seu modelo
import com.example.socorristajunior.ui.components.EmergencyCard
import com.example.socorristajunior.ui.components.EmergencyDetailContent


// Função para mapear o nome da imagem (do BD) para um ícone
fun mapEmergencyImageToIcon(imageName: String): ImageVector {
    return when (imageName) {
        "fire" -> Icons.Default.LocalFireDepartment
        "cut" -> Icons.Default.MedicalServices
        "choke" -> Icons.Default.ReportProblem
        else -> Icons.Default.HelpOutline
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EmergenciesScreen(
    navController: NavController,
    viewModel: EmergenciesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (uiState.selectedEmergencyId == null) "Emergências" else "Como Agir") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.selectedEmergencyId != null) {
                            viewModel.onBackToList()
                        } else {
                            navController.popBackStack() // Volta para a HomeScreen
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.selectedEmergencyId,
            modifier = Modifier.padding(innerPadding),
            transitionSpec = {
                fadeIn() + slideInHorizontally { it } with fadeOut() + slideOutHorizontally { -it }
            }, label = "emergency_content_switcher"
        ) { targetId ->
            if (targetId == null) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    EmergencyListContent(
                        searchText = uiState.searchText,
                        onSearchTextChanged = viewModel::onSearchTextChanged,
                        emergencies = uiState.emergenciesList,
                        onEmergencyClick = viewModel::onEmergencySelected
                    )
                }
            } else {
                EmergencyDetailContent(steps = uiState.stepsList)
            }
        }
    }
}

@Composable
private fun EmergencyListContent(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    emergencies: List<Emergencia>,
    onEmergencyClick: (Int) -> Unit
) {
    val filteredEmergencies = emergencies.filter { it.emernome.contains(searchText, ignoreCase = true) }

    Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize()) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            label = { Text("Ex: engasgo, queimadura...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filteredEmergencies) { emergency ->
                EmergencyCard(
                    name = emergency.emernome,
                    icon = mapEmergencyImageToIcon(emergency.emerimagem), // Ajuste para seus campos
                    onClick = { onEmergencyClick(emergency.emercodigo) } // Ajuste para seus campos
                )
            }
        }
    }
}