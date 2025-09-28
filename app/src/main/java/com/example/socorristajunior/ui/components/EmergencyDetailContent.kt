/* package com.example.socorristajunior.ui.components

// ... Imports ...
import android.widget.Button
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socorristajunior.ui.emergencies.EmergencyStep
import org.w3c.dom.Text

// Atualize o EmergencyDetailContent no mesmo arquivo
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmergencyDetailContent(steps: List<EmergencyStep>, emergencyName: String) {
    val pagerState = rememberPagerState(pageCount = { steps.size })

    Column(modifier = Modifier.fillMaxSize()) {
        // Header com nome da emergência
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A6FA9))
                .padding(16.dp)
        ) {
            Text(
                text = emergencyName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Indicador de progresso
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Passo ${pagerState.currentPage + 1} de ${steps.size}",
                color = Color(0xFF666666),
                fontWeight = FontWeight.Medium
            )

            // Barra de progresso
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFEEEEEE))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((pagerState.currentPage + 1).toFloat() / steps.size)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFF1A6FA9))
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) { page ->
            StepCard(step = steps[page])
        }

        // Controles de navegação
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    if (pagerState.currentPage > 0) {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                },
                enabled = pagerState.currentPage > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF666666)
                )
            ) {
                Text("Anterior")
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < steps.size - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                enabled = pagerState.currentPage < steps.size - 1,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1A6FA9)
                )
            ) {
                Text("Próximo")
            }
        }
    }
}

@Composable
fun StepCard(step: EmergencyStep) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Número do passo
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1A6FA9).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.stepNumber.toString(),
                    color = Color(0xFF1A6FA9),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )


                Spacer(modifier = Modifier.height(24.dp))

                // Ícone do passo
                Icon(
                    imageVector = step.icon,
                    contentDescription = null,
                    tint = Color(0xFF1A6FA9),
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Título
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Descrição
                Text(
                    text = step.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF666666),
                    lineHeight = 24.sp
                )
            }
        }
    }
}*/