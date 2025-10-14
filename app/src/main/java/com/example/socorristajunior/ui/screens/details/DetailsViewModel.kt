package com.example.socorristajunior.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import com.example.socorristajunior.ui.screens.emergencies.EmergencyStep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailsUiState(
    val stepsList: List<EmergencyStep> = emptyList(),
    val isLoading: Boolean = true
)

data class EmergencyStep(
    val stepNumber: Int,
    val totalSteps: Int,
    val title: String,
    val description: String
)


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val passoRepo: PassoRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun loadSteps(emergencyId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, stepsList = emptyList()) }

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


}

