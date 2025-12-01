package com.example.socorristajunior.ui.screens.noticias

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.socorristajunior.Data.model.Noticia
import com.example.socorristajunior.Data.model.TipoConteudo
import com.example.socorristajunior.ui.viewmodel.NoticiasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticiasScreen(
    viewModel: NoticiasViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atualizações em Saúde") },
                // 2. Adiciona o botão de navegação na esquerda
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // --- BARRA DE FILTROS HORIZONTAL ---
            if (!state.isLoading && state.error == null) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.filtrosDisponiveis) { filtro ->
                        val isSelected = state.filtroSelecionado == filtro
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.aplicarFiltro(filtro) },
                            label = { Text(filtro) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                            } else null
                        )
                    }
                }
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
            // -----------------------------------

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else if (state.error != null) {
                    Text("Erro: ${state.error}", color = MaterialTheme.colorScheme.error)
                } else {
                    // LISTA DE NOTÍCIAS (Filtrada)
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.noticiasExibidas) { noticia ->
                            NoticiaCard(noticia)
                        }

                        // Mensagem se o filtro não retornar nada
                        if (state.noticiasExibidas.isEmpty()) {
                            item {
                                Text(
                                    "Nenhuma notícia encontrada com este filtro.",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoticiaCard(noticia: Noticia) {
    val context = LocalContext.current
    val tipo = noticia.getTipoConteudo()

    // Define ícone baseado no tipo (PDF, Video ou Artigo)
    val icone: ImageVector = when (tipo) {
        TipoConteudo.PDF -> Icons.Default.PictureAsPdf
        TipoConteudo.VIDEO -> Icons.Default.PlayCircle
        TipoConteudo.ARTIGO -> Icons.Default.Article
    }

    // Define cor do ícone
    val corIcone = when (tipo) {
        TipoConteudo.PDF -> MaterialTheme.colorScheme.error // Vermelho para PDF
        TipoConteudo.VIDEO -> MaterialTheme.colorScheme.primary // Azul/Padrão para Vídeo
        TipoConteudo.ARTIGO -> MaterialTheme.colorScheme.secondary // Outra cor para artigo
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(noticia.link))
                context.startActivity(intent)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Ícone lateral
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = corIcone.copy(alpha = 0.1f), // Fundo clarinho
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        imageVector = icone,
                        contentDescription = tipo.label,
                        colorFilter = ColorFilter.tint(corIcone),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                // Tags (Tipo + Tema)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Badge do Tipo (Video/PDF)
                    SuggestionChip(
                        onClick = {},
                        label = { Text(tipo.label, style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.height(24.dp),
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        border = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Badge do Tema
                    noticia.temaPrincipal?.let { tema ->
                        Text(
                            text = tema.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = noticia.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = noticia.resumo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}