package com.example.socorristajunior.ui.screens.quiz.question

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Usa os nomes exatos dos seus models:
import com.example.socorristajunior.Data.model.QuizCategoryWithQuestions
import com.example.socorristajunior.Data.model.QuestionWithOptions
import com.example.socorristajunior.Domain.Repositorio.QuizRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI
data class QuizQuestionUiState(
    val isLoading: Boolean = true,
    val categoryTitle: String = "",
    val currentQuestion: QuestionWithOptions? = null,
    val questionNumber: Int = 0,
    val totalQuestions: Int = 0,
    val progress: Float = 0f,
    val selectedAnswerIndex: Int? = null, // Índice da opção clicada
    val showFeedback: Boolean = false,
    val isCorrect: Boolean = false
    // O campo 'explanation' foi removido pois não existe no seu 'Question.kt'
)

// Argumentos para navegar para a tela de Resultados
data class QuizResultArgs(
    val score: Int,
    val totalQuestions: Int
)

@HiltViewModel
class QuizQuestionViewModel @Inject constructor(
    private val quizRepo: QuizRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // 1. Pega o ID da navegação
    private val categoryId: Int = checkNotNull(savedStateHandle["categoryId"])

    // 2. Dados internos para gerenciar o quiz
    private var allQuestions: List<QuestionWithOptions> = emptyList()
    private var score = 0

    private val _uiState = MutableStateFlow(QuizQuestionUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<QuizResultArgs>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadQuiz()
    }

    private fun loadQuiz() {
        viewModelScope.launch {
            // 2. CHAMA SEU REPO
            quizRepo.getQuizCompleto(categoryId)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false) } // Tratar erro
                }
                .collect { quizCompleto: QuizCategoryWithQuestions ->

                    // 3. GUARDA OS DADOS
                    // Usa 'questoesComOpcoes' do seu model 'QuizCategoryWithQuestions'
                    allQuestions = quizCompleto.questoesComOpcoes.shuffled() // Embaralha

                    if (allQuestions.isEmpty()) {
                        _uiState.update { it.copy(isLoading = false) } // TODO: Adicionar estado de erro
                        return@collect
                    }

                    _uiState.update { it.copy(
                        // Usa 'categoria.nome' do seu model 'QuizCategory'
                        categoryTitle = quizCompleto.categoria.nome,
                        totalQuestions = allQuestions.size
                    )}

                    // 4. MOSTRA A PRIMEIRA PERGUNTA
                    showQuestion(0)
                }
        }
    }

    private fun showQuestion(index: Int) {
        val question = allQuestions[index]
        _uiState.update {
            it.copy(
                isLoading = false,
                currentQuestion = question,
                questionNumber = index + 1,
                progress = ((index + 1) / allQuestions.size.toFloat()) * 100,
                selectedAnswerIndex = null,
                showFeedback = false,
                isCorrect = false
            )
        }
    }

    // --- AÇÕES DO USUÁRIO ---

    fun onAnswerSelected(optionIndex: Int) {
        if (!_uiState.value.showFeedback) {
            _uiState.update { it.copy(selectedAnswerIndex = optionIndex) }
        }
    }

    fun onConfirmAnswer() {
        val currentState = _uiState.value
        val question = currentState.currentQuestion ?: return
        val selectedIndex = currentState.selectedAnswerIndex ?: return

        // **LÓGICA CORRIGIDA**
        // Usa 'opcoes[index].correta' (Boolean) do seu 'Option.kt'
        val isCorrect = question.opcoes[selectedIndex].correta

        if (isCorrect) {
            score++
        }

        _uiState.update {
            it.copy(
                showFeedback = true,
                isCorrect = isCorrect
            )
        }
    }

    fun onNextQuestion() {
        val currentState = _uiState.value
        val nextQuestionIndex = currentState.questionNumber // (questionNumber é 1-based)

        if (nextQuestionIndex < allQuestions.size) {
            // Próxima pergunta
            showQuestion(nextQuestionIndex)
        } else {
            // Fim do Quiz - Navegar para Resultados
            viewModelScope.launch {
                _navigationEvent.emit(
                    QuizResultArgs(
                        score = score,
                        totalQuestions = allQuestions.size
                    )
                )
            }
        }
    }
}