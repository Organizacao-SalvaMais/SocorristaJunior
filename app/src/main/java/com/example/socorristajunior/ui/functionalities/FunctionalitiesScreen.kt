package com.example.socorristajunior.ui.functionalities

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.FeatureCard

// Adicionamos a anotação OptIn para usar a TopAppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionalitiesScreen(
    navController: NavController,
    viewModel: FunctionalitiesViewModel = hiltViewModel()
) {
    // 1. Usamos o Scaffold como container principal da tela
    Scaffold(
        // 2. No slot "topBar", definimos nossa TopAppBar
        topBar = {
            TopAppBar(
                // 3. Aqui você define o título que desejar!
                title = { Text("Página Inicial") }
            )
        }
    ) { innerPadding ->
        // O conteúdo da sua tela agora fica dentro do Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()
                // 4. Aplicamos o innerPadding para que o conteúdo não fique embaixo da TopAppBar
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // O conteúdo com os cards continua o mesmo
            FeatureCard(
                icon = Icons.Filled.Psychology,
                iconColor = Color(0xFFE57373),
                title = "Quiz Interativo",
                description = "Teste seus conhecimentos sobre primeiros socorros com nosso quiz educativo.",
                buttonText = "Iniciar Quiz",
                buttonColor = Color(0xFFF2D16B),
                onClick = { navController.navigate("quiz") }
            )
            Spacer(modifier = Modifier.height(24.dp))
            FeatureCard(
                icon = Icons.Default.Warning,
                iconColor = Color(0xFFD92B2B),
                title = "Emergências",
                description = "Passo a passo para situações de emergência.",
                buttonText = "Ver Procedimentos",
                buttonColor = Color(0xFFD92B2B),
                onClick = { navController.navigate("emergencies") }
            )
        }
    }
}