package com.example.socorristajunior.ui.quiz


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun QuizHomeScreen(navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Escolha seu Quiz",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(24.dp))

        QuizCard(
            title = "Traumas e Lesões Comuns",
            description = "Teste seus conhecimentos sobre primeiros socorros em situações de trauma.",
            difficulty = "Fácil"
        ) {
            navController.navigate("questionScreen")
        }

        Spacer(Modifier.height(16.dp))

        QuizCard(
            title = "Emergências Clínicas Comuns",
            description = "Aprenda a agir em situações como IAM, AVE e convulsões.",
            difficulty = "Médio"
        ) {
            navController.navigate("questionScreen")
        }
    }
}

@Composable
fun QuizCard(title: String, description: String, difficulty: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(description, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Text("Dificuldade: $difficulty", color = MaterialTheme.colorScheme.secondary)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onClick, modifier = Modifier.align(Alignment.End)) {
                Text("Iniciar Quiz")
            }
        }
    }
}
