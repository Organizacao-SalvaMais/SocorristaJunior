package com.example.socorristajunior.ui.screens.quiz.question

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


// --- 1. A ROTA "INTELIGENTE" ---
@Composable
fun QuizQuestionRoute(
    viewModel: QuizQuestionViewModel = hiltViewModel(),
    onNavigateToResults: (QuizResultArgs) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { args ->
            onNavigateToResults(args)
        }
    }

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.background
        ),

    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.currentQuestion != null) {
            QuizQuestionScreen(
                uiState = uiState,
                onAnswerSelected = viewModel::onAnswerSelected,
                onConfirm = viewModel::onConfirmAnswer,
                onNext = viewModel::onNextQuestion
            )
        } else {
            // TODO: Mostrar estado de erro ou quiz vazio
            Text("Não foi possível carregar o quiz.")
        }
    }
}

// --- 2. A TELA "BURRA" ---
@Composable
private fun QuizQuestionScreen(
    uiState: QuizQuestionUiState,
    onAnswerSelected: (Int) -> Unit,
    onConfirm: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Garantimos que a questão não é nula pela lógica da Rota
    val question = uiState.currentQuestion!!

    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 700.dp) // max-w-3xl
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- Barra de Progresso ---
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Pergunta ${uiState.questionNumber} de ${uiState.totalQuestions}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${uiState.progress.toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { uiState.progress / 100f },
                modifier = Modifier.fillMaxWidth().clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Card da Pergunta ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Cabeçalho da Pergunta (Número e Texto)
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${uiState.questionNumber}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        // Usa 'enunciado' do seu 'Question.kt'
                        text = question.questao.enunciado,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                }

                // (Tag de Categoria removida - não existe no model Question.kt)

                Spacer(modifier = Modifier.height(24.dp))

                // --- Opções ---
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Usa 'opcoes' do seu 'QuestionWithOptions'
                    question.opcoes.forEachIndexed { index, option ->
                        val isSelected = uiState.selectedAnswerIndex == index
                        // Usa 'correta' (Boolean) do seu 'Option.kt'
                        val isCorrectAnswer = option.correta
                        val showCorrect = uiState.showFeedback && isCorrectAnswer
                        val showIncorrect = uiState.showFeedback && isSelected && !isCorrectAnswer

                        OptionButton(
                            // Usa 'resposta' do seu 'Option.kt'
                            text = option.resposta,
                            onClick = { onAnswerSelected(index) },
                            enabled = !uiState.showFeedback,
                            isSelected = isSelected,
                            showCorrect = showCorrect,
                            showIncorrect = showIncorrect
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Card de Feedback ---
                if (uiState.showFeedback) {
                    // Passa a explicação vazia, já que ela não existe no model
                    FeedbackCard(
                        isCorrect = uiState.isCorrect,
                        explanation = "" // Seu model Question não tem 'explanation'
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // --- Botão de Ação ---
                val buttonText = if (uiState.showFeedback) "Próxima Pergunta" else "Confirmar Resposta"
                val buttonIcon = if (uiState.showFeedback) Icons.Default.ChevronRight else null

                Button(
                    onClick = if (uiState.showFeedback) onNext else onConfirm,
                    enabled = if (uiState.showFeedback) true else uiState.selectedAnswerIndex != null,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text(buttonText, style = MaterialTheme.typography.titleMedium)
                    if (buttonIcon != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = buttonIcon, contentDescription = null)
                    }
                }
            }
        }
    }
}


// --- 3. COMPOSABLES AUXILIARES "BURROS" ---

@Composable
fun OptionButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    isSelected: Boolean,
    showCorrect: Boolean,
    showIncorrect: Boolean
) {
    val successColor = Color(0xFF4CAF50)
    val errorColor = MaterialTheme.colorScheme.error

    val borderColor = when {
        showCorrect -> successColor
        showIncorrect -> errorColor
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    val backgroundColor = when {
        showCorrect -> successColor.copy(alpha = 0.1f)
        showIncorrect -> errorColor.copy(alpha = 0.1f)
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        else -> Color.Transparent
    }

    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)

            if (showCorrect) {
                Icon(Icons.Default.Check, contentDescription = "Correto", tint = successColor)
            }
            if (showIncorrect) {
                Icon(Icons.Default.Close, contentDescription = "Incorreto", tint = errorColor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackCard(isCorrect: Boolean, explanation: String) {
    val successColor = Color(0xFF4CAF50)
    val errorColor = MaterialTheme.colorScheme.error

    val color = if (isCorrect) successColor else errorColor
    val icon = if (isCorrect) Icons.Default.Check else Icons.Default.Close
    val title = if (isCorrect) "Correto!" else "Incorreto"

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(2.dp, color),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, color = color)
                // Só mostra a explicação se ela não for vazia
                if (explanation.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(explanation, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
