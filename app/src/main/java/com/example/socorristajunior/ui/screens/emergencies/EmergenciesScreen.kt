package com.example.socorristajunior.ui.screens.emergencies

import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.BottomNavigationBar
import com.example.socorristajunior.ui.components.getCorPorNome


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergenciesScreen(
    navController: NavController,
    viewModel: EmergenciesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredList by viewModel.filteredEmergencies.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergências") },
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
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.searchText,
                    onValueChange = viewModel::onSearchTextChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    placeholder = { Text("Buscar por emergência...") },
                    singleLine = true,
                    trailingIcon = {
                        if (uiState.searchText.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchTextChanged("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpar busca")
                            }
                        } else {
                            Icon(Icons.Default.Search, contentDescription = "Ícone de busca")
                        }
                    }
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredList) { item ->
                        val emergencia = item.emergencia
                        val isFav = item.isFavorite
                        val isViewed = item.isViewed
                        val cor = getCorPorNome(emergencia.gravidadeCor)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("emergency_detail/${emergencia.emercodigo}")
                                },
                            colors = CardDefaults.cardColors(containerColor = cor, contentColor = MaterialTheme.colorScheme.onSurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = emergencia.emernome,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isViewed) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                    else MaterialTheme.colorScheme.secondary
                                        )

                                        if (isViewed) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(
                                                imageVector = Icons.Filled.CheckCircle,
                                                contentDescription = "Lido",
                                                tint = Color(0xFF4CAF50), // Verde
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    // Exibe a gravidade que veio do Supabase
                                    Text(
                                        text = "Gravidade: ${emergencia.gravidadeNome}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "Cor: ${emergencia.gravidadeCor}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = emergencia.emerdesc,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                if (isFav) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "Favorito",
                                        tint = MaterialTheme.colorScheme.error, // Vermelho
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .size(24.dp)
                                    )
                                }
                                if(!emergencia.emerimagem.isNullOrEmpty()){
                                    AsyncImage(
                                        model = emergencia.emerimagem,
                                        contentDescription = "Imagem de ${emergencia.emernome}",
                                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                        modifier = Modifier
                                            .width(100.dp)
                                            .height(100.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}