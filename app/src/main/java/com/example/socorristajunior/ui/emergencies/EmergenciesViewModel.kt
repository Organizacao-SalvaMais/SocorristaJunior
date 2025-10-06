package com.example.socorristajunior.ui.emergencies

import androidx.compose.ui.graphics.vector.ImageVector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmergencyStep(val stepNumber: Int, val totalSteps: Int, val title: String, val description: String)

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
    private val passoRepo: PassoRepo
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

    fun onEmergencySelected(emergencyId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(stepsList = emptyList(), selectedEmergencyId = emergencyId) }

            passoRepo.getPassos(emergencyId).collect { passosDoBanco ->
                val totalPassos = passosDoBanco.size
                _uiState.update { currentState ->
                    currentState.copy(
                        stepsList = passosDoBanco.map { passo ->
                            passo.toEmergencyStep(totalPassos)
                        }
                    )
                }
            }
        }
    }

    fun onBackToList() {
        _uiState.update { it.copy(selectedEmergencyId = null, stepsList = emptyList()) }
    }

    private fun Passo.toEmergencyStep(totalSteps: Int): EmergencyStep {
        return EmergencyStep(
            stepNumber = this.pasordem,
            totalSteps = totalSteps,
            title = this.pasnome,
            description = this.pasdescricao

        )
    }

}