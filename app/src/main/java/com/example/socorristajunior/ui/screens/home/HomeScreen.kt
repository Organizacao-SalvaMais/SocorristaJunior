package com.example.socorristajunior.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.BottomNavigationBar
import com.example.socorristajunior.ui.components.EmergencyContactsGrid
import com.example.socorristajunior.ui.components.FeatureCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    //viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar( // ✅ corrigido
                title = { Text("Salvar +") }
            )
        },
        bottomBar = {
            // Função da NavBar | | Label
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Card de Treinamento/Quiz
            FeatureCard(
                icon = Icons.Filled.Psychology,
                iconColor = Color(0xFF3F51B5),
                title = "Treinamento",
                description = "Aprenda primeiros socorros com perguntas rápidas e diretas.",
                buttonText = "Começar",
                buttonTextColor = Color(0xFF3F51B5),
                buttonColor = Color(0xFF3F51B5),
                onClick = { navController.navigate("quiz") }
            )

            Spacer(modifier = Modifier.height(24.dp))
            // Card de Emergencia
            FeatureCard(
                icon = Icons.Default.Warning,
                iconColor = Color(0xFFFAEB01),
                title = "EMERGÊNCIA",
                description = "Guias para Emergências",
                buttonText = "Abrir",
                buttonTextColor = Color.White,
                buttonColor = Color(0xFFE51F2D),
                onClick = { navController.navigate("emergencies") }
            )

            //Spacer(modifier = Modifier.height(24.dp))
/*
            EmergencyContactsGrid()
*/

            /*
            // Card de Botões de Contato
            FeatureCard(
                icon = Icons.Filled.AddIcCall,
                iconColor = Color(0xFF3F51B5),
                title = "CONTATOS",
                description = "Entre em contato com profissionais de saúde.",
                buttonText = "190",
                buttonTextColor = Color.White,
                buttonColor = Color(0xFFE51F2D),
                onClick = {navController.navigate("profile")}
            )*/
        }
    }
}

