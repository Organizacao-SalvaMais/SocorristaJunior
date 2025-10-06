package com.example.socorristajunior.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.ui.Profile.ProfileViewModel
import com.example.socorristajunior.ui.components.BottomNavigationBar
import com.example.socorristajunior.ui.components.FeatureCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
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
        }
    }
}

@Composable
private fun EmergencyItemCard(emergency: Emergencia, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            navController.navigate("emergency_detail/${emergency.emercodigo}")
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        when (emergency.emergravidade) { // ✅ corrigido
                            "Alta" -> Color(0xFFD32F2F)
                            "Moderada" -> Color(0xFFFFA000)
                            else -> Color(0xFF4CAF50)
                        }
                    )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = emergency.emernome,
                    fontSize = 16.sp,
                    color = Color(0xFF333333)
                )
                Text(
                    text = emergency.emerdesc,
                    fontSize = 12.sp,
                    color = Color(0xFF666666),
                    maxLines = 1
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Ver detalhes",
                tint = Color(0xFF1A6FA9),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}


