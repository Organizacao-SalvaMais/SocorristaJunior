package com.example.socorristajunior.ui.screens.home

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.socorristajunior.ui.components.BottomNavigationBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val newsState by viewModel.newsState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color(0xFFE62727))) {
                                append("SALVAR")
                            }
                            append(" +")
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF3F2EC),
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF3F2EC)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Aprenda a Salvar",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE62727),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Text(
                text = "Vidas!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE62727),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            FeaturesCarousel(navController)

            Spacer(modifier = Modifier.height(32.dp))

            NewsCarousel(
                newsState = newsState,
                onNewsClick = { newsUrl ->
                    viewModel.openNewsUrl(newsUrl, context)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturesCarousel(navController: NavController) {
    val features = listOf(
        FeatureItem(
            title = "Emergências",
            description = "Saiba como agir",
            icon = Icons.Default.Warning,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFFE62727), Color(0xFFA40000))
            ),
            route = "emergencies"
        ),
        FeatureItem(
            title = "Teste de Conhecimento",
            description = "Treinamento com quizzes",
            icon = Icons.Default.Psychology,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF1E93AB), Color(0xFF156579))
            ),
            route = "quiz_home"
        ),
        FeatureItem(
            title = "Contatos Emergenciais",
            description = "Saiba para QUEM ligar",
            icon = Icons.Default.ContactEmergency,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF45B7D1), Color(0xFF2A8FA5))
            ),
            route = "emergency_contacts"
        ),
        FeatureItem(
            title = "Unidades Próximas",
            description = "Mapa das unidades emergenciais",
            icon = Icons.Default.LocationOn,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF4CAF50), Color(0xFF388E3C))
            ),
            route = "emergency_units_map"
        ),
        FeatureItem(
            title = "Perfil",
            description = "Seu perfil de usuário",
            icon = Icons.Default.Person,
            gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF9C27B0), Color(0xFF7B1FA2))
            ),
            route = "profile"
        )
    )

    // Rolagem infinita - cria uma lista muito grande que simula infinito
    val infiniteFeatures = remember { generateInfiniteList(features) }
    val pageCount = infiniteFeatures.size

    val pagerState = rememberPagerState(
        initialPage = pageCount / 2,
        pageCount = { pageCount }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Funcionalidades",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height(180.dp)
        ) { page ->
            val actualPage = page % features.size
            val feature = features[actualPage]
            val elevation by animateDpAsState(
                targetValue = if (pagerState.currentPage % features.size == actualPage) 8.dp else 2.dp,
                animationSpec = tween(durationMillis = 300)
            )

            Card(
                onClick = {
                    navController.navigate(feature.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .height(160.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = elevation),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(feature.gradient)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = feature.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = feature.title,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = feature.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Indicadores de página
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            features.forEachIndexed { index, _ ->
                val actualCurrentPage = pagerState.currentPage % features.size
                val color = if (index == actualCurrentPage) {
                    Color(0xFFE62727)
                } else {
                    Color.Gray.copy(alpha = 0.5f)
                }
                val size by animateDpAsState(
                    targetValue = if (index == actualCurrentPage) 12.dp else 8.dp,
                    animationSpec = tween(durationMillis = 300)
                )

                Box(
                    modifier = Modifier
                        .size(size)
                        .clip(RoundedCornerShape(50))
                        .background(color)
                        .padding(4.dp)
                )

                if (index < features.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

// Função auxiliar para criar lista infinita
private fun generateInfiniteList(originalList: List<FeatureItem>): List<FeatureItem> {
    return List(1000) { index ->
        originalList[index % originalList.size]
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsCarousel(
    newsState: NewsState,
    onNewsClick: (String) -> Unit
) {
    val newsList = newsState.news

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Notícias Recentes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            newsState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFFE62727))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Carregando notícias...", color = Color.Gray)
                    }
                }
            }

            newsState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = newsState.error ?: "Erro ao carregar notícias",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                val pagerState = rememberPagerState(pageCount = { newsList.size })

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.height(300.dp)
                ) { page ->
                    val news = newsList[page]
                    val elevation by animateDpAsState(
                        targetValue = if (pagerState.currentPage == page) 8.dp else 2.dp,
                        animationSpec = tween(durationMillis = 300)
                    )

                    Card(
                        onClick = { onNewsClick(news.url) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Imagem real com Coil
                            AsyncImage(
                                model = news.imageUrl,
                                contentDescription = "Imagem da notícia: ${news.title}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                                contentScale = ContentScale.Crop,
                                placeholder = rememberAsyncImagePainter(
                                    model = news.imageUrl,
                                    error = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_report_image)
                                )
                            )

                            // Conteúdo da notícia
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = news.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Fonte e data
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = news.source,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = news.publishedAt,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                // Indicadores de página para notícias
                if (newsList.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        newsList.forEachIndexed { index, _ ->
                            val color = if (index == pagerState.currentPage) {
                                Color(0xFFE62727)
                            } else {
                                Color.Gray.copy(alpha = 0.3f)
                            }
                            val size by animateDpAsState(
                                targetValue = if (index == pagerState.currentPage) 12.dp else 8.dp,
                                animationSpec = tween(durationMillis = 300)
                            )

                            Box(
                                modifier = Modifier
                                    .size(size)
                                    .clip(RoundedCornerShape(50))
                                    .background(color)
                                    .padding(4.dp)
                            )

                            if (index < newsList.size - 1) {
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

data class FeatureItem(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val gradient: Brush,
    val route: String
)