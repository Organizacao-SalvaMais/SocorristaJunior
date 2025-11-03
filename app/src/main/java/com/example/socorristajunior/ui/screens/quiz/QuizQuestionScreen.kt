package com.example.socorristajunior.ui.screens.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuizQuestionScreen(navController: NavController) {
    // Use um ViewModel real mais tarde; aqui só para demo:
    val viewModel = remember { QuizViewModel() }
    val question = viewModel.questions[viewModel.currentQuestionIndex.value]
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var feedback by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Questão ${viewModel.currentQuestionIndex.value + 1} de ${viewModel.questions.size}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        LinearProgressIndicator(
            progress = (viewModel.currentQuestionIndex.value + 1).toFloat() / viewModel.questions.size,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        AnimatedContent(targetState = question, transitionSpec = {
            slideInHorizontally() with slideOutHorizontally()
        }) { q ->
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(q.text, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    q.options.forEachIndexed { index, option ->
                        Button(
                            onClick = {
                                selectedOption = index
                                val correct = index == q.correctAnswerIndex
                                feedback = if (correct) "✅ Correto!" else "❌ Errado!"
                                // avança ou finaliza
                                viewModel.answerQuestion(index) { score, total ->
                                    navController.navigate("quiz_result/$score/$total")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedOption == index)
                                    MaterialTheme.colorScheme.primaryContainer else Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Text(option)
                        }
                    }

                    feedback?.let {
                        Text(
                            text = it,
                            color = if (it.contains("Correto")) Color(0xFF388E3C) else Color(0xFFD32F2F),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
