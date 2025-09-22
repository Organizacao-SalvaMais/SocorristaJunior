package com.example.socorristajunior.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Emergencia // Importe sua classe de modelo
import com.example.socorristajunior.Domain.Repositorio.EmergenciaRepo // Importe seu repositório
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Definimos um estado mais completo para a tela Home
data class HomeUiState(
    val searchText: String = "",
    val emergencias: List<Emergencia> = emptyList(),
    val isLoading: Boolean = true // Começa como true para mostrar o carregamento
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    // 2. Hilt vai injetar seu repositório aqui
    private val emergenciaRepo: EmergenciaRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    // 3. O bloco init é executado assim que o ViewModel é criado
    init {
        carregarEmergencias()
    }

    // 4. A lógica para buscar os dados do repositório
    private fun carregarEmergencias() {
        viewModelScope.launch {
            // Coleta o Flow de emergências do repositório
            emergenciaRepo.getAllEmergencias().collect { listaDeEmergencias ->
                // Atualiza o estado da UI com os novos dados
                _uiState.update { currentState ->
                    currentState.copy(
                        emergencias = listaDeEmergencias,
                        isLoading = false // Carregamento concluído
                    )
                }
            }
        }
    }

    // 5. As funções de evento que a UI vai chamar
    fun onSearchTextChanged(novoTexto: String) {
        _uiState.update { it.copy(searchText = novoTexto) }
        // TODO: Lógica de filtro da lista
    }

    fun onSearchSubmit() {
        // TODO: Lógica de busca
        println("Pesquisando por: ${_uiState.value.searchText}")
    }
}