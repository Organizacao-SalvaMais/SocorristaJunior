package com.example.socorristajunior.ui.screens.emergencies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.DAO.UserInteractionDAO
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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

data class EmergenciaComFavorito(
    val emergencia: Emergencia,
    val isFavorite: Boolean
)

@HiltViewModel
class EmergenciesViewModel @Inject constructor(
    private val emergenciaRepo: EmergenciaRepo,
    private val userDao: UserDAO,
    private val interactionDAO: UserInteractionDAO
) : ViewModel() {

    // _uiState privado para gerenciar o estado interno
    private val _uiState = MutableStateFlow(EmergenciesUiState())
    // uiState público e imutável para a UI observar
    val uiState = _uiState.asStateFlow()

    private val currentUserId = userDao.getLoggedUser()
        .map { it?.supabaseUserId }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // Flow que lê DIRETAMENTE do banco de dados local (Room)
    private val emergenciesFromDb: StateFlow<List<Emergencia>> =
        emergenciaRepo.getAllEmergencias()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    // Flow que combina a busca de texto com a lista do banco
    val filteredEmergencies: StateFlow<List<EmergenciaComFavorito>> = combine(
        emergenciaRepo.getAllEmergencias(), // Lista base
        currentUserId.flatMapLatest { userId -> // Lista de favoritos do usuário
            if (userId != null) interactionDAO.getAllInteractionsFlow(userId)
            else flowOf(emptyList())
        },
        _uiState.map { it.searchText }.distinctUntilChanged() // Texto da busca
    ) { emergencias, interacoes, searchText ->

        // Transforma a lista de interações em um Map para acesso rápido
        // Chave: ID da emergência, Valor: se é favorito
        val favoritosMap = interacoes.associate { it.emergencyId to it.isFavorite }

        // Filtra e mapeia
        emergencias
            .filter {
                searchText.isBlank() ||
                        it.emernome.contains(searchText, ignoreCase = true) ||
                        it.emerdesc.contains(searchText, ignoreCase = true)
            }
            .map { emergencia ->
                EmergenciaComFavorito(
                    emergencia = emergencia,
                    isFavorite = favoritosMap[emergencia.emercodigo] ?: false
                )
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