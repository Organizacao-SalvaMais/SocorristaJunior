package com.example.socorristajunior.ui.screens.quiz.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


// Você precisa definir o que é "BottomRight"
private val Offset.Companion.BottomRight: Offset
    get() = Offset(1f, 1f) // Exemplo: se for (1, 1)

// Você precisa definir o que é "TopLeft"
private val Offset.Companion.TopLeft: Offset
    get() = Offset(0f, 0f) // Exemplo: se for (0, 0)

// --- 1. A ROTA "INTELIGENTE" (Conecta o ViewModel à View) ---
// (Esta é a função que você chama no seu NavGraph em MainActivity.kt)
@Composable
fun QuizHomeRoute(
    viewModel: QuizHomeViewModel = hiltViewModel(),
    onNavigateToQuiz: (categoryId: Int) -> Unit
) {
    // Observa o estado do ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Escuta eventos de navegação
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { categoryId ->
            onNavigateToQuiz(categoryId)
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Chama a tela "burra"
        QuizHomeScreen(
            uiState = uiState,
            onStartQuiz = viewModel::onCategorySelected // Passa a referência da função
        )
    }
}


// --- 2. A TELA "BURRA" (Apenas desenha a UI) ---
@Composable
private fun QuizHomeScreen(
    uiState: QuizHomeUiState,
    onStartQuiz: (categoryId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    /*val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.background
        ),
        start = Offset.TopLeft,
        end = Offset.BottomRight
    )*/

    // Container principal
    Box(
        modifier = modifier
            .fillMaxSize()
            /*.background(backgroundBrush)*/
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 800.dp) // Limita a largura máxima
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Cabeçalho ---
            val headerGradient = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.primary
                )
            )
/*
            // Ícone do cabeçalho
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(headerGradient)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))*/

            /*// Título
            Text(
                text = "Quiz de Primeiros Socorros",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    brush = headerGradient, // Aplica o gradiente ao texto
                    textAlign = TextAlign.Center,
                    lineHeight = 44.sp
                ),
            )*/

            Spacer(modifier = Modifier.height(16.dp))

           /* // Descrição
            Text(
                text = "Teste seus conhecimentos sobre situações de emergência e aprenda a salvar vidas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )*/

            Spacer(modifier = Modifier.height(32.dp))

            // --- Grid de Dificuldades (Responsivo) ---
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                val isWide = this.maxWidth > 600.dp
                if (isWide) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        uiState.categories.forEach { item ->
                            DifficultyCard(
                                item = item,
                                onClick = { onStartQuiz(item.categoryId) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        uiState.categories.forEach { item ->
                            DifficultyCard(
                                item = item,
                                onClick = { onStartQuiz(item.categoryId) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Card de Rodapé (Estatísticas) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    uiState.stats.forEach { stat ->
                        StatItem(
                            label = stat.label,
                            value = stat.value,
                            color = stat.color
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaço extra no final
        }
    }
}

// --- 3. COMPOSABLES AUXILIARES  ---

@Composable
fun DifficultyCard(
    item: QuizHomeItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(2.dp, item.color)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ícone do Card
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(32.dp),

                )
            }

            // Textos
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Botão
            Button(
                onClick = { onClick() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = item.color,
                    contentColor = if (item.color == Color(0xFFFFC107)) // Cor 'Warning'
                        MaterialTheme.colorScheme.onSurface // Texto escuro
                    else
                        Color.White // Texto claro
                )
            ) {
                Text("Começar")
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
