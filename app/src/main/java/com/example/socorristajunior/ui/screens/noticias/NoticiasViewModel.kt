package com.example.socorristajunior.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Noticia
import com.example.socorristajunior.Data.model.TipoConteudo
import com.example.socorristajunior.Domain.Repositorio.NoticiasRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado da UI agora contém a lista ORIGINAL e a lista FILTRADA
data class NoticiasState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val noticiasOriginais: List<Noticia> = emptyList(),
    val noticiasExibidas: List<Noticia> = emptyList(),
    val filtroSelecionado: String? = null, // Null = Todos
    val filtrosDisponiveis: List<String> = emptyList() // Lista de tags (Temas + Tipos)
)

@HiltViewModel
class NoticiasViewModel @Inject constructor(
    private val repository: NoticiasRepositorio
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoticiasState())
    val uiState: StateFlow<NoticiasState> = _uiState.asStateFlow()

    init {
        carregarNoticias()
    }

    fun carregarNoticias() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val lista = repository.buscarNoticias()

                // Extrai temas únicos e adiciona os Tipos fixos (PDF, Video)
                val temas = lista.mapNotNull { it.temaPrincipal?.uppercase() }.distinct().sorted()
                val tipos = TipoConteudo.values().map { it.label }
                val filtros = tipos + temas // Combina tudo na barra de filtros

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        noticiasOriginais = lista,
                        noticiasExibidas = lista, // Começa exibindo tudo
                        filtrosDisponiveis = filtros
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }

    fun aplicarFiltro(filtro: String) {
        _uiState.update { state ->
            // Se clicar no mesmo filtro que já está ativo, remove o filtro (volta para Todos)
            val novoFiltro = if (state.filtroSelecionado == filtro) null else filtro

            val listaFiltrada = if (novoFiltro == null) {
                state.noticiasOriginais
            } else {
                state.noticiasOriginais.filter { noticia ->
                    // Verifica se é um TEMA ou se é um TIPO (PDF/Video)
                    val isTema = noticia.temaPrincipal?.uppercase() == novoFiltro
                    val isTipo = noticia.getTipoConteudo().label == novoFiltro
                    isTema || isTipo
                }
            }

            state.copy(
                filtroSelecionado = novoFiltro,
                noticiasExibidas = listaFiltrada
            )
        }
    }
}