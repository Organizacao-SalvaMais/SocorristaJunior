package com.example.socorristajunior.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.socorristajunior.ui.emergencies.EmergencyStep

@Composable
fun EmergencyDetailContent(steps: List<EmergencyStep>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        steps.forEach { step ->
            StepCard(step = step)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StepCard(step: EmergencyStep) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(), shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ){
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onPrimary)
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) { Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(50) // formato "pill"
                )
                .padding(horizontal = 12.dp, vertical = 6.dp) // espaço dentro da caixinha
        ) {
            Text(
                text = "Passo ${step.stepNumber} de ${step.totalSteps}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }

            Text(
                text = step.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Espaço reservado para imagem ilustrativa
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = MaterialTheme.colorScheme.onBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "[Imagem ilustrativa]",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFD9D9D9))
            }

            Text(
                text = step.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start
            )
        }
    }
}

