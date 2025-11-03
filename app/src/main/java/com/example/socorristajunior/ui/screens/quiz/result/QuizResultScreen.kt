package com.example.socorristajunior.ui.screens.quiz.result
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrackChanges
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.socorristajunior.ui.screens.quiz.result.ResultNavigation
import com.example.socorristajunior.ui.screens.quiz.result.QuizResultsViewModel

// --- 1. A ROTA "INTELIGENTE" ---
@Composable
fun QuizResultRoute(
    viewModel: QuizResultsViewModel = hiltViewModel(),
    onRestart: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when(event) {
                ResultNavigation.OnRestart -> onRestart()
            }
        }
    }

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.background
        ),
      /*  start = Offset.TopLeft,
        end = Offset.BottomRight*/
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        QuizResultsScreen(
            uiState = uiState,
            onRestart = viewModel::onRestart
        )
    }
}

// --- 2. A TELA "BURRA" ---
@Composable
private fun QuizResultsScreen(
    uiState: QuizResultUiState,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 600.dp) // max-w-2xl
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- Ícone de Troféu ---
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(uiState.feedbackColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star, // (Substitua por Troféu se tiver)
                        contentDescription = "Resultado",
                        tint = uiState.feedbackColor,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Textos de Feedback ---
                Text(
                    text = uiState.feedbackTitle,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.feedbackMessage,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- Grid de Estatísticas ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ResultStatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.TrackChanges,
                        label = "Acertos",
                        value = uiState.scoreText,
                        color = MaterialTheme.colorScheme.primary
                    )
                    ResultStatCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Star,
                        label = "Aproveitamento",
                        value = uiState.percentageText,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Botão de Ação (Apenas Reiniciar) ---
                Button(
                    onClick = onRestart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fazer Novamente", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Aviso de Rodapé ---
        Text(
            text = "Lembre-se: Este quiz é educativo. Em emergências reais, sempre chame ajuda profissional (192 ou 193).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}


// --- 3. COMPOSABLE AUXILIAR "BURRO" ---

@Composable
fun ResultStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}