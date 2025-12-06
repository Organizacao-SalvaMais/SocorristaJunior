package com.example.socorristajunior.ui.screens.quiz.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.ArrowBack



private val Offset.Companion.BottomRight: Offset
    get() = Offset(1f, 1f)

private val Offset.Companion.TopLeft: Offset
    get() = Offset(0f, 0f)


@Composable
fun QuizHomeRoute(
    viewModel: QuizHomeViewModel = hiltViewModel(),
    onNavigateToQuiz: (categoryId: Int) -> Unit,
    onNavigateBack: () -> Unit
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
        QuizHomeScreen(
            uiState = uiState,
            onStartQuiz = viewModel::onCategorySelected,
            onNavigateBack = onNavigateBack
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizHomeScreen(
    uiState: QuizHomeUiState,
    onStartQuiz: (categoryId: Int) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF1E93AB), Color(0xFF156579))
                        )
                    )
            ) {
                TopAppBar(
                    title = { Text("Quiz de Primeiros Socorros", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Voltar",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = Color.White,
                        titleContentColor = Color.White
                    )
                )
            }
        }
    ) { innerPadding ->
        // Container principal
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF3F2EC))
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 800.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // --- Grid de Cards (Responsivo) ---
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
            }
        }
    }
}

// --- COMPOSABLES AUXILIARES ---

@Composable
fun DifficultyCard(
    item: QuizHomeItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF1E93AB), Color(0xFF156579))
    )

    Card(
        modifier = modifier.clickable { onClick() },
        border = BorderStroke(3.dp, gradient),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B8FA3)
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // IMAGEM NO TOPO DO CARD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color(0xFF156579)) // Cor de fundo caso a imagem não preencha tudo
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit, // Não corta nada
                    alignment = Alignment.Center
                )

                // Overlay escuro
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )
            }

            // CONTEÚDO DO CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.95f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF156579),
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = "Começar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
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