package com.example.socorristajunior.ui.screens.emergencies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo
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

data class EmergenciesUiState(
    val emergenciesList: List<Emergencia> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class EmergenciesViewModel @Inject constructor(
    private val emergenciaRepo: EmergenciaRepo
) : ViewModel() {

    // _uiState privado para gerenciar o estado interno
    private val _uiState = MutableStateFlow(EmergenciesUiState())
    // uiState público e imutável para a UI observar
    val uiState = _uiState.asStateFlow()

    // Flow que lê DIRETAMENTE do banco de dados local (Room)
    private val emergenciesFromDb: StateFlow<List<Emergencia>> =
        emergenciaRepo.getAllEmergencias()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Flow que combina a busca de texto com a lista do banco
    val filteredEmergencies: StateFlow<List<Emergencia>> =
        combine(emergenciesFromDb, _uiState) { emergencies, state ->
            // Atualiza a lista no _uiState (necessário para a busca)
            _uiState.update { it.copy(emergenciesList = emergencies) }

            if (state.searchText.isBlank()) {
                emergencies
            } else {
                emergencies.filter {
                    it.emernome.contains(state.searchText, ignoreCase = true) ||
                            it.emerdesc.contains(state.searchText, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Quando o ViewModel for criado, inicie a sincronização
        syncEmergencias()
    }

    /*
      Tenta buscar os dados mais recentes do Supabase e salvar no banco local.
      A UI irá atualizar automaticamente pois está observando o banco local.
     */
    private fun syncEmergencias() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Chama a nova função de sincronização do repositório
                Log.d("SupabaseSync", "Iniciando sincronização com Supabase...")
                emergenciaRepo.syncEmergenciasFromSupabase()
                Log.d("SupabaseSync", "Sincronização concluída com sucesso!")
                _uiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                // Vamos imprimir o erro no Logcat
                Log.e("SupabaseSync", "Falha ao sincronizar com Supabase: ${e.message}", e)

                // TODO: Lidar com erro de rede (ex: mostrar Snackbar)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        _uiState.update { it.copy(searchText = newText) }
    }

}