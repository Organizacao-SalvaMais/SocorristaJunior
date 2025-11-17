package com.example.socorristajunior.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite // Importante
import androidx.compose.material.icons.outlined.FavoriteBorder // Importante
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.EmergencyDetailContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyDetailScreen(
    emergencyId: Int,
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    // O uiState agora contém o campo 'isFavorite' que vem do banco
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Carrega os passos e REGISTRA A VISUALIZAÇÃO (Isso acontece dentro do loadSteps no ViewModel)
    LaunchedEffect(key1 = emergencyId) {
        viewModel.loadSteps(emergencyId)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                navController = navController,
                title = "Detalhes da Emergência", // Passando título dinâmico se quiser
                isFavorite = uiState.isFavorite,  // Estado atual (vem do ViewModel)
                onFavoriteClick = { viewModel.toggleFavorito() } // Ação de clique
            )
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
                EmergencyDetailContent(steps = uiState.stepsList)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallTopAppBar(
    navController: NavController,
    title: String,
    isFavorite: Boolean,        // Recebe se é favorito
    onFavoriteClick: () -> Unit // Recebe a função de clicar
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        },
        // AQUI ESTÁ O BOTÃO QUE FALTAVA:
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    // Muda o ícone: Cheio se favorito, Borda se não
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favoritar",
                    // Muda a cor: Vermelho se favorito, Cinza se não
                    tint = if (isFavorite) Color.Red else Color.Gray
                )
            }
        }
    )
}