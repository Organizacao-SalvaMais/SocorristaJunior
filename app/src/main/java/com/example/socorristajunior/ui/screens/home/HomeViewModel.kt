package com.example.socorristajunior.ui.screens.home

// por enquanto deixaremos esse ViewModel vazio, pois a tela inicial esta estatica.

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
/*
data class HomeUiState(
    val searchText: String = "",
    val isLoading: Boolean = true
)*/
/*
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val emergenciaRepo: EmergenciaRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
/*
    // 🔎 Busca em tempo real isso não deveria estar aqui e sim na parte de emergencias
    val resultadosBusca: StateFlow<List<Emergencia>> =
        _uiState.map { it.searchText }
            .distinctUntilChanged()
            .flatMapLatest { termoBusca ->
                if (termoBusca.isBlank()) {
                    emergenciaRepo.getAllEmergencias()
                } else {
                    emergenciaRepo.searchEmergencias(termoBusca)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
*/
    init {
        carregarDadosIniciais()
    }

    private fun carregarDadosIniciais() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val totalEmergencias = emergenciaRepo.getTotalEmergencias()
            if (totalEmergencias == 0) {
                carregarDadosPadrao()
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
    // pq essa merda está aqui?
    private suspend fun carregarDadosPadrao() {
        val emergenciasPadrao = listOf(
            Emergencia(
                emernome = "Queimaduras",
                emerdesc = "Procedimento para queimaduras de 1º, 2º e 3º grau",
                emergravidade = "Alta",
                emerimagem = "fire",
                categoria = "Lesões",
                duracaoEstimada = 10
            ),
            Emergencia(
                emernome = "Engasgo Adulto",
                emerdesc = "Manobra de Heimlich para desobstrução das vias aéreas",
                emergravidade = "Altíssima",
                emerimagem = "choke",
                categoria = "Respiratório",
                duracaoEstimada = 2
            )
        )
        emergenciaRepo.insertAllEmergencias(emergenciasPadrao)
    }
/*
    fun onSearchTextChanged(novoTexto: String) {
        _uiState.update { it.copy(searchText = novoTexto) }
    }

    fun filtrarPorCategoria(categoria: String) {
        viewModelScope.launch {
            emergenciaRepo.getEmergenciasPorCategoria(categoria).collect { lista ->
                _uiState.update { it.copy(emergencias = lista) }
            }
        }
    }*/
}
*/