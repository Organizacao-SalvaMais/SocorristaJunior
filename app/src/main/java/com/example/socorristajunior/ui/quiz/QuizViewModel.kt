package com.example.socorristajunior.ui.quiz

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class QuizUiState(
    val isLoading: Boolean = true
)

@HiltViewModel
class QuizViewModel @Inject constructor() : ViewModel() {
    // Lógica futura do quiz
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()
}