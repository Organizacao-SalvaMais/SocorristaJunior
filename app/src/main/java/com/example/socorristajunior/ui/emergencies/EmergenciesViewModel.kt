package com.example.socorristajunior.ui.emergencies

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Emergencia // Importe seu modelo
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo // Importe seu repositório
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Movi o modelo de dados dos passos para cá
data class EmergencyStep(val stepNumber: Int, val totalSteps: Int, val title: String, val description: String, val icon: ImageVector)

data class EmergenciesUiState(
    val emergenciesList: List<Emergencia> = emptyList(),
    val stepsList: List<EmergencyStep> = emptyList(),
    val searchText: String = "",
    val selectedEmergencyId: Int? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class EmergenciesViewModel @Inject constructor(
    private val emergenciaRepo: EmergenciaRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmergenciesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadEmergencies()
    }

    private fun loadEmergencies() {
        viewModelScope.launch {
            Log.d("EmergenciesViewModel", "Carregando emergências...")
            _uiState.update { it.copy(isLoading = true) }
            emergenciaRepo.getAllEmergencias().collect { emergenciesFromDb ->
                Log.d("EmergenciesViewModel", "Emergências recebidas do banco de dados. Quantidade: ${emergenciesFromDb.size}")
                _uiState.update {
                    it.copy(emergenciesList = emergenciesFromDb, isLoading = false)
                }
            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        _uiState.update { it.copy(searchText = newText) }
    }

    fun onEmergencySelected(emergencyId: Int) {
        // A lógica para buscar os passos agora está no ViewModel
        val steps = getStepsForEmergency(emergencyId)
        _uiState.update { it.copy(selectedEmergencyId = emergencyId, stepsList = steps) }
    }

    fun onBackToList() {
        _uiState.update { it.copy(selectedEmergencyId = null) }
    }

    // Função privada para obter os passos (aqui com dados de exemplo)
    private fun getStepsForEmergency(emergencyId: Int): List<EmergencyStep> {
        // No futuro, você pode buscar isso de um repositório também!
        return when (emergencyId) {
            1 -> listOf(
                EmergencyStep(1, 3, "Ligue para a Emergência", "Disque 192 (SAMU) ou 193 (Bombeiros). Mantenha a calma e informe a situação.", Icons.Default.Call),
                EmergencyStep(2, 3, "Verifique a Respiração", "Veja se a pessoa está respirando. Se não, inicie a massagem cardíaca.", Icons.Default.Hearing),
                EmergencyStep(3, 3, "Aguarde Ajuda", "Não mova a vítima a menos que seja absolutamente necessário. Aguarde a chegada do socorro.", Icons.Default.Timer)
            )
            else -> emptyList() // Para outras emergências
        }
    }
}