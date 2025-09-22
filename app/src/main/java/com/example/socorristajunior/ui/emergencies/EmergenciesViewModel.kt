package com.example.socorristajunior.ui.emergencies

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo // 1. Importe o PassoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Modelo de dados para a UI (não muda)
data class EmergencyStep(val stepNumber: Int, val totalSteps: Int, val title: String, val description: String, val icon: ImageVector)

// Estado da UI (não muda)
data class EmergenciesUiState(
    val emergenciesList: List<Emergencia> = emptyList(),
    val stepsList: List<EmergencyStep> = emptyList(),
    val searchText: String = "",
    val selectedEmergencyId: Int? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class EmergenciesViewModel @Inject constructor(
    private val emergenciaRepo: EmergenciaRepo,
    private val passoRepo: PassoRepo // 2. Injete o PassoRepo aqui
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmergenciesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadEmergencies()
    }

    private fun loadEmergencies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            emergenciaRepo.getAllEmergencias().collect { emergenciesFromDb ->
                _uiState.update {
                    it.copy(emergenciesList = emergenciesFromDb, isLoading = false)
                }
            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        _uiState.update { it.copy(searchText = newText) }
    }

    // 3. A função onEmergencySelected agora busca os dados do repositório
    fun onEmergencySelected(emergencyId: Int) {
        viewModelScope.launch {
            // .first() pega a primeira lista emitida pelo Flow e encerra a coleta
            val passosDoBanco = passoRepo.getPassos(emergencyId).first()
            val totalPassos = passosDoBanco.size

            _uiState.update {
                it.copy(
                    selectedEmergencyId = emergencyId,
                    // Mapeamos a lista de "Passo" (do BD) para a lista de "EmergencyStep" (da UI)
                    stepsList = passosDoBanco.map { passo ->
                        passo.toEmergencyStep(totalPassos) // Usando a função de mapeamento abaixo
                    }
                )
            }
        }
    }

    fun onBackToList() {
        _uiState.update { it.copy(selectedEmergencyId = null, stepsList = emptyList()) }
    }

    // 4. (BOA PRÁTICA) Função de extensão para converter o modelo de dados em modelo de UI
    private fun Passo.toEmergencyStep(totalSteps: Int): EmergencyStep {
        return EmergencyStep(
            stepNumber = this.pasordem,
            totalSteps = totalSteps,
            title = this.pasnome,
            description = this.pasdescricao,
            icon = mapPasoImageToIcon(this.pasimagem) // Função para mapear o ícone
        )
    }

    // Mapeia o nome da imagem do passo (do BD) para um ícone
    private fun mapPasoImageToIcon(imageName: String?): ImageVector {
        return when (imageName) {
            "call" -> Icons.Default.Call
            "breathe" -> Icons.Default.Hearing
            "wait" -> Icons.Default.Timer
            else -> Icons.Default.HelpOutline
        }
    }
}