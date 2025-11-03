package com.example.socorristajunior.ui.screens.quiz

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {
    private val _currentQuestionIndex = mutableStateOf(0)
    private val _score = mutableStateOf(0)

    val questions = listOf(
        Question("Qual é o primeiro cuidado ao socorrer uma queimadura?", listOf(
            "Aplicar pasta de dente", "Resfriar com água corrente", "Estourar bolhas", "Cobrir com cobertor"
        ), correctAnswerIndex = 1),
        Question("O que fazer em caso de fratura exposta?", listOf(
            "Tentar recolocar o osso", "Imobilizar e acionar o socorro", "Aplicar gelo diretamente", "Fazer massagem"
        ), correctAnswerIndex = 1)
    )

    val currentQuestionIndex: State<Int> = _currentQuestionIndex
    val score: State<Int> = _score

    fun answerQuestion(selectedIndex: Int, onFinished: (Int, Int) -> Unit) {
        if (selectedIndex == questions[_currentQuestionIndex.value].correctAnswerIndex) {
            _score.value += 1
        }
        if (_currentQuestionIndex.value < questions.lastIndex) {
            _currentQuestionIndex.value += 1
        } else {
            onFinished(_score.value, questions.size)
        }
    }
}

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)