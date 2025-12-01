package com.example.socorristajunior.ui.screens.noticias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Noticia
import com.example.socorristajunior.Domain.Repositorio.NoticiasRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estados da Tela
sealed interface NoticiasUiState {
    object Loading : NoticiasUiState
    data class Success(val noticias: List<Noticia>) : NoticiasUiState
    data class Error(val message: String) : NoticiasUiState
}

@HiltViewModel
class NoticiasViewModel @Inject constructor(
    private val repository: NoticiasRepositorio
) : ViewModel() {

    private val _uiState = MutableStateFlow<NoticiasUiState>(NoticiasUiState.Loading)
    val uiState: StateFlow<NoticiasUiState> = _uiState.asStateFlow()

    init {
        carregarNoticias()
    }

    fun carregarNoticias() {
        viewModelScope.launch {
            _uiState.value = NoticiasUiState.Loading
            try {
                val lista = repository.buscarNoticias()
                _uiState.value = NoticiasUiState.Success(lista)
            } catch (e: Exception) {
                _uiState.value = NoticiasUiState.Error("Falha na conexão: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }
}