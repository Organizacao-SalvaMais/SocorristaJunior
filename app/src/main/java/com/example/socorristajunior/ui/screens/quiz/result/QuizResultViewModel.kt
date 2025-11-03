package com.example.socorristajunior.ui.screens.quiz.result

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI
data class QuizResultUiState(
    val scoreText: String = "",
    val percentageText: String = "",
    val feedbackTitle: String = "",
    val feedbackMessage: String = "",
    val feedbackColor: Color = Color.Transparent
)

// Eventos de Navegação (Apenas Reiniciar)
enum class ResultNavigation {
    OnRestart
}

@HiltViewModel
class QuizResultsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Cores
    private val successColor = Color(0xFF4CAF50)
    private val warningColor = Color(0xFFFFC107)
    private val primaryColor = Color(0xFF6200EE) // Exemplo, use seu Theme

    // 1. Argumentos recebidos da tela anterior
    private val score: Int = checkNotNull(savedStateHandle["score"])
    private val totalQuestions: Int = checkNotNull(savedStateHandle["totalQuestions"])

    private val _uiState = MutableStateFlow(QuizResultUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<ResultNavigation>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        processResults()
    }

    private fun processResults() {
        // Evita divisão por zero se o quiz estiver vazio
        val percentage = if (totalQuestions > 0) {
            (score.toFloat() / totalQuestions.toFloat() * 100).toInt()
        } else {
            0
        }

        val (title, message, color) = getFeedback(percentage)

        _uiState.update {
            it.copy(
                scoreText = "$score / $totalQuestions",
                percentageText = "$percentage%",
                feedbackTitle = title,
                feedbackMessage = message,
                feedbackColor = color
            )
        }
    }

    // Lógica de negócio movida do seu código React
    private fun getFeedback(percentage: Int): Triple<String, String, Color> {
        return when {
            percentage >= 90 -> Triple("Excelente!", "Você está muito bem preparado! 🌟", successColor)
            percentage >= 70 -> Triple("Bom trabalho!", "Você tem um bom conhecimento! 👏", warningColor)
            else -> Triple("Continue praticando!", "Revise o conteúdo. 💪", primaryColor)
        }
    }

    // --- AÇÕES DO USUÁRIO ---
    fun onRestart() {
        viewModelScope.launch {
            _navigationEvent.emit(ResultNavigation.OnRestart)
        }
    }
}