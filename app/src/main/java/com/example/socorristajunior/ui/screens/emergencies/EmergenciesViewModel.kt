package com.example.socorristajunior.ui.screens.emergencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EmergencyStep(val stepNumber: Int, val totalSteps: Int, val title: String, val description: String)
/*
data class EmergenciesUiState(
    val emergenciesList: List<Emergencia> = emptyList(),
    val stepsList: List<EmergencyStep> = emptyList(),
    val searchText: String = "",
    val selectedEmergencyId: Int? = null,
    val isLoading: Boolean = true
)
*/

data class EmergenciesUiState(
    val allEmergencies: List<Emergencia> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class EmergenciesViewModel @Inject constructor(
    private val emergenciaRepo: EmergenciaRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmergenciesUiState())
    private val searchText = _uiState.map { it.searchText }.distinctUntilChanged()
    private val allEmergencies = _uiState.map { it.allEmergencies }

    val filteredEmergencies: StateFlow<List<Emergencia>> =
        combine(allEmergencies, searchText) { emergencies, text ->
            if (text.isBlank()) {
                emergencies
            } else {
                emergencies.filter {
                    it.emernome.contains(text, ignoreCase = true) ||
                            it.emerdesc.contains(text, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState = _uiState.asStateFlow()

    init {
        loadEmergencies()
    }

    // Carregas a lista das emergencia que vem do banco e só modifica quando o banco é modificado.
    private fun loadEmergencies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // o ".first" que diz que a lista é carregada apenas quando a pagina é carregada, caso queiram que ela atualize quando o banco atualizar então utilize ".collect".
                val emergenciesFromDb = emergenciaRepo.getAllEmergencias().first()
                _uiState.update {
                    it.copy(allEmergencies = emergenciesFromDb, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        _uiState.update { it.copy(searchText = newText) }
    }

}