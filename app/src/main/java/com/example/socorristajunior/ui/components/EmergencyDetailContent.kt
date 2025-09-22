package com.example.socorristajunior.ui.components

// ... Imports ...
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.socorristajunior.ui.emergencies.EmergencyStep

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmergencyDetailContent(steps: List<EmergencyStep>) {
    val pagerState = rememberPagerState(pageCount = { steps.size })
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp)) { page ->
            StepCard(step = steps[page])
        }
        Row(Modifier.height(50.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            repeat(steps.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                Box(modifier = Modifier.padding(4.dp).size(12.dp).clip(RoundedCornerShape(6.dp)).background(color))
            }
        }
    }
}

@Composable
fun StepCard(step: EmergencyStep) {
    Card(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(24.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Passo ${step.stepNumber} de ${step.totalSteps}", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Icon(imageVector = step.icon, contentDescription = null, modifier = Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = step.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = step.description, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        }
    }
}