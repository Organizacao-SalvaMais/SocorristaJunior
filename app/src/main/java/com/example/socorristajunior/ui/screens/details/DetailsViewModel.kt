package com.example.socorristajunior.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Data.model.UserInteraction
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailsUiState(
    val stepsList: List<EmergencyStep> = emptyList(),
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false
)

data class EmergencyStep(
    val stepNumber: Int,
    val totalSteps: Int,
    val title: String,
    val description: String
)


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val passoRepo: PassoRepo,
    private val emergenciaRepo: EmergenciaRepo,
    private val userDao: UserDAO
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    private var currentEmergencyId: Int = 0

    private val currentUser = userDao.getLoggedUser()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _userInteraction = MutableStateFlow<UserInteraction?>(null)

    fun loadSteps(emergencyId: Int) {
        currentEmergencyId = emergencyId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, stepsList = emptyList()) }

            observeInteraction(emergencyId)

            registrarVisualizacao(emergencyId)

            try {
                // Usamos .first() para carregar os passos apenas uma vez.
                val passosDoBanco = passoRepo.getPassos(emergencyId).first()
                val totalPassos = passosDoBanco.size

                val emergencySteps = passosDoBanco.map { passo ->
                    passo.toEmergencyStep(totalPassos)
                }

                _uiState.update {
                    it.copy(stepsList = emergencySteps, isLoading = false)
                }
            } catch (e: Exception) {
                // Tratar o erro, se necessário
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun Passo.toEmergencyStep(totalSteps: Int): EmergencyStep {
        return EmergencyStep(
            stepNumber = this.pasordem,
            totalSteps = totalSteps,
            title = this.pasnome,
            description = this.pasdescricao
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeInteraction(emerId: Int) {
        viewModelScope.launch {
            // Combina o usuário atual com a busca no repositório
            currentUser.flatMapLatest { user ->
                if (user?.supabaseUserId != null) {
                    emergenciaRepo.getInteraction(user.supabaseUserId, emerId)
                } else {
                    flowOf(null)
                }
            }.collect { interaction ->
                // Atualiza o estado da UI com o status de favorito
                _userInteraction.value = interaction
                _uiState.update {
                    it.copy(isFavorite = interaction?.isFavorite ?: false)
                }
            }
        }
    }

    // Chamado automaticamente dentro de loadSteps
    private fun registrarVisualizacao(emerId: Int) {
        viewModelScope.launch {
            val user = currentUser.value
            val isFavAtual = _userInteraction.value?.isFavorite ?: false
            // Só marca se tivermos um usuário com ID do Supabase válido
            if (user != null && user.supabaseUserId != null) {
                emergenciaRepo.markAsViewed(user.supabaseUserId, emerId, isFavAtual)
            }
        }
    }

    // Chamado pelo botão de coração na UI
    fun toggleFavorito() {
        viewModelScope.launch {
            val user = currentUser.value
            val currentStatus = _userInteraction.value?.isFavorite ?: false

            if (user != null && user.supabaseUserId != null && currentEmergencyId != 0) {
                emergenciaRepo.toggleFavorite(user.supabaseUserId, currentEmergencyId, currentStatus)
            }
        }
    }


}

