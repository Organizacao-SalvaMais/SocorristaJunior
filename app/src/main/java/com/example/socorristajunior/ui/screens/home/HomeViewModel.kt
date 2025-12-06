package com.example.socorristajunior.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {

    private val _newsState = MutableStateFlow(NewsState())
    val newsState: StateFlow<NewsState> = _newsState.asStateFlow()

    init {
        loadNews()
        startNewsRefreshTimer()
    }

    private fun loadNews() {
        viewModelScope.launch {
            _newsState.update { it.copy(isLoading = true, error = null) }

            try {
                delay(1000)

                val news = generateSampleNews()
                _newsState.update {
                    it.copy(
                        isLoading = false,
                        news = news,
                        lastUpdated = System.currentTimeMillis()
                    )
                }
            } catch (e: Exception) {
                _newsState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Erro desconhecido"
                    )
                }
            }
        }
    }

    private fun startNewsRefreshTimer() {
        viewModelScope.launch {
            while (true) {
                delay(10 * 60 * 60 * 1000L)
                loadNews()
            }
        }
    }

    fun openNewsUrl(url: String, context: android.content.Context) {
        viewModelScope.launch {
            try {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
                _newsState.update {
                    it.copy(error = "Não foi possível abrir a notícia")
                }
            }
        }
    }

    private fun generateSampleNews(): List<NewsItem> {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return listOf(
            NewsItem(
                title = "Manaus registra 1.298 novos casos de HIV em 2025",
                imageUrl = "https://www.bing.com/images/search?view=detailV2&ccid=ElkV3kof&id=A18977D808F975B5A7F8704667F1FA45CEA5591E&thid=OIP.ElkV3kofxEk8IAD_Rt5TEwHaE5&mediaurl=https%3a%2f%2fwww.lalpathlabs.com%2fblog%2fwp-content%2fuploads%2f2016%2f03%2fhiv-test.jpg&cdnurl=https%3a%2f%2fth.bing.com%2fth%2fid%2fR.125915de4a1fc4493c2000ff46de5313%3frik%3dHlmlzkX68WdGcA%26pid%3dImgRaw%26r%3d0&exph=3264&expw=4928&q=teste+hiv&FORM=IRPRST&ck=376E0E71DACDD2E5479F9D5B59D7BD8E&selectedIndex=0&itb=0",
                source = "G1",
                publishedAt = dateFormat.format(Date()),
                url = "https://g1.globo.com/am/amazonas/noticia/2025/11/30/manaus-registra-1298-novos-casos-de-hiv-em-2025-e-lanca-campanha-dezembro-vermelho.ghtml"
            ),
            NewsItem(
                title = "Novo protocolo de primeiros socorros é lançado no AM",
                imageUrl = "https://images.unsplash.com/photo-1559757148-5c350d0d3c56?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80",
                source = "Jornal do Amazonas",
                publishedAt = dateFormat.format(Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)),
                url = "https://www.amazonas.am.gov.br/"
            ),
            NewsItem(
                title = "Campanha de prevenção a acidentes domésticos",
                imageUrl = "https://images.unsplash.com/photo-1582719471384-894fbb16e074?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80",
                source = "Defesa Civil",
                publishedAt = dateFormat.format(Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000)),
                url = "https://www.defesacivil.am.gov.br/"
            ),
            NewsItem(
                title = "Prefeitura mobiliza 19 unidades de saúde para o ‘Dia D’ de vacinação contra a influenza",
                imageUrl = "https://images.unsplash.com/photo-1584467735871-8db9ac8d0eaa?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80",
                source = "Prefeitura de Manaus",
                publishedAt = dateFormat.format(Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000)),
                url = "https://www.manaus.am.gov.br/noticia/imunizacao/prefeitura-semsa-diad-vacinacaoinfluenza/"
            ),
            NewsItem(
                title = "Curso gratuito de primeiros socorros atrai centenas em Manaus",
                imageUrl = "https://images.unsplash.com/photo-1579684385127-1ef15d508118?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1000&q=80",
                source = "Secretaria de Educação",
                publishedAt = dateFormat.format(Date(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000)),
                url = "https://www.educacao.am.gov.br/"
            )
        )
    }
}

data class NewsState(
    val news: List<NewsItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val lastUpdated: Long = 0
)

data class NewsItem(
    val title: String,
    val imageUrl: String,
    val source: String,
    val publishedAt: String,
    val url: String
)