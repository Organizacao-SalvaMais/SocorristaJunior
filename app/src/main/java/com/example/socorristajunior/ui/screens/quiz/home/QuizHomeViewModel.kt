package com.example.socorristajunior.ui.screens.quiz.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.QuizCategory
import com.example.socorristajunior.Domain.Repositorio.QuizRepo
import com.example.socorristajunior.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class QuizHomeUiState(
    val isLoading: Boolean = true,
    val categories: List<QuizHomeItem> = emptyList(),
    val stats: List<StatItem> = emptyList(),
    val error: String? = null
)


data class QuizHomeItem(
    val categoryId: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val imageRes: Int
)


data class StatItem(val label: String, val value: String, val color: Color)

@HiltViewModel
class QuizHomeViewModel @Inject constructor(
    private val quizRepo: QuizRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizHomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Int>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    // Cores
    private val successColor = Color(0xFF4CAF50)
    private val warningColor = Color(0xFFFFC107)
    private val primaryColor = Color(0xFF6200EE)
    private val secondaryColor = Color(0xFF03DAC6)

    init {
        loadScreenData()
    }

    private fun loadScreenData() {
        viewModelScope.launch {
            quizRepo.getCategorias()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { categoriesFromDb ->
                    val uiItems = categoriesFromDb.map { mapCategoryToUiItem(it) }

                    val stats = listOf(
                        StatItem("Perguntas", "15+", primaryColor),
                        StatItem("Opções", "4", secondaryColor),
                        StatItem("Educativo", "100%", successColor)
                    )

                    _uiState.update {
                        it.copy(isLoading = false, categories = uiItems, stats = stats)
                    }
                }
        }
    }

    fun onCategorySelected(categoryId: Int) {
        viewModelScope.launch {
            _navigationEvent.emit(categoryId)
        }
    }

    private fun mapCategoryToUiItem(category: QuizCategory): QuizHomeItem {
        val title = category.nome
        val description = category.descricao

        // MAPEIA USANDO O ID DA CATEGORIA
        val (icon, color, imageRes) = when (category.id) {
            1 -> Triple(
                Icons.Filled.Favorite,
                successColor,
                R.drawable.trauma_image
            )
            2 -> Triple(
                Icons.Filled.Warning,
                warningColor,
                R.drawable.emergency_image
            )
            3 -> Triple(
                Icons.Filled.Psychology,
                primaryColor,
                R.drawable.trauma_image // ou outra imagem para a terceira categoria
            )
            else -> Triple(
                Icons.Filled.Favorite,
                Color.Gray,
                R.drawable.trauma_image
            )
        }

        return QuizHomeItem(
            categoryId = category.id,
            title = title,
            description = description,
            icon = icon,
            color = color,
            imageRes = imageRes
        )
    }
}