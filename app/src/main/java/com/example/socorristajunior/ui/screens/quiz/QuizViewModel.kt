package com.example.socorristajunior.ui.screens.quiz

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class QuizUiState(
    val isLoading: Boolean = true
)

@HiltViewModel
class QuizViewModel @Inject constructor() : ViewModel() {
    // Lógica futura do quiz
}