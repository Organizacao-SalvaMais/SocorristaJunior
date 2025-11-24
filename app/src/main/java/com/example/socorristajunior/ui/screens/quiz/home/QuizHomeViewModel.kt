package com.example.socorristajunior.ui.screens.quiz.home

import android.util.Log
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI (O que a View vai desenhar)
data class QuizHomeUiState(
    val isLoading: Boolean = true,
    val categories: List<QuizHomeItem> = emptyList(),
    val stats: List<StatItem> = emptyList(), // Stats do rodapé
    val error: String? = null
)

// Modelo de UI (Traduzido do seu Model do DB)
data class QuizHomeItem(
    val categoryId: Int, // ID para navegação
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

// Modelo de UI para as estatísticas
data class StatItem(val label: String, val value: String, val color: Color)

@HiltViewModel
class QuizHomeViewModel @Inject constructor(
    private val quizRepo: QuizRepo // Injetando SEU Repositório
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizHomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Int>() // Emite o categoryId
    val navigationEvent = _navigationEvent.asSharedFlow()

    // Cores (Mover para o Theme se possível)
    private val successColor = Color(0xFF4CAF50)
    private val warningColor = Color(0xFFFFC107)
    private val primaryColor = Color(0xFF6200EE) // Exemplo, use seu Theme
    private val secondaryColor = Color(0xFF03DAC6) // Exemplo, use seu Theme

    init {
        loadScreenData()
        syncWithServer()
    }

    private fun syncWithServer() {
        viewModelScope.launch {
            try {
                quizRepo.atualizarQuizzesDoServidor()
            } catch (e: Exception) {
                Log.e("QuizHomeVM", "Erro ao sincronizar: ${e.message}")
            }
        }
    }

    private fun loadScreenData() {
        viewModelScope.launch {
            // 1. Busca as categorias do SEU repo
            quizRepo.getCategorias()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { categoriesFromDb ->
                    // 2. Mapeia os dados do DB para dados de UI
                    val uiItems = categoriesFromDb.map { mapCategoryToUiItem(it) }

                    // 3. Adiciona as estatísticas (estáticas, como no React)
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

    // Ação do Usuário: Clicou em um card
    fun onCategorySelected(categoryId: Int) {
        viewModelScope.launch {
            _navigationEvent.emit(categoryId)
        }
    }

    // Lógica de "Tradução" (Mapper)
    private fun mapCategoryToUiItem(category: QuizCategory): QuizHomeItem {
        // Usa os campos exatos do seu model: 'nome' e 'descricao'
        val title = category.nome
        val description = category.descricao

        // Mapeia o nome para o ícone e cor
        val (icon, color) = when (category.nome.uppercase()) {
            "FÁCIL" -> Icons.Filled.Favorite to successColor
            "MÉDIO" -> Icons.Filled.Warning to warningColor
            "DIFÍCIL" -> Icons.Filled.Psychology to primaryColor
            else -> Icons.Filled.Favorite to Color.Gray // Padrão
        }

        return QuizHomeItem(
            categoryId = category.id,
            title = title,
            description = description,
            icon = icon,
            color = color
        )
    }
}