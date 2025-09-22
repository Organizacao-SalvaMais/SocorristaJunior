package com.example.socorristajunior.ui.functionalities

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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

@Composable
fun FunctionalitiesScreen(
    navController: NavController,
    viewModel: FunctionalitiesViewModel = hiltViewModel()
) {
    Surface(
        color = Color(0xFFFAF7F2),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SALVAR+",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF424242)
            )
            Spacer(modifier = Modifier.height(32.dp))
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