package com.example.socorristajunior.ui.screens.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun QuizResultScreen(navController: NavController, score: Int, total: Int) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Quiz Concluído!", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("$score / $total acertos", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))

        Button(onClick = { navController.navigate("quizScreen") }) {
            Text("Voltar ao Início")
        }
    }
}
