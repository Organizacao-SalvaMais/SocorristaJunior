package com.example.socorristajunior.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.DAO.UserDao
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.ui.login.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val loggedUser: UserEntity? = null,
    val searchText: String = "",
    val emergencias: List<Emergencia> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val emergenciaRepo: EmergenciaRepo,
    private val userDao: UserDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    // 🔎 Busca em tempo real
    val uiState: StateFlow<HomeUiState> =
        userDao.getLoggedUser() // Flow do Room para o estado de login
            .combine(_uiState) { user, currentUiState ->
                // Combina o UserEntity do Room com o estado atual do ViewModel
                currentUiState.copy(loggedUser = user)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HomeUiState(isLoading = true)
            )

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

    fun onSearchTextChanged(novoTexto: String) {
        _uiState.update { it.copy(searchText = novoTexto) }
    }

    fun filtrarPorCategoria(categoria: String) {
        viewModelScope.launch {
            emergenciaRepo.getEmergenciasPorCategoria(categoria).collect { lista ->
                _uiState.update { it.copy(emergencias = lista) }
            }
        }
    }
}
