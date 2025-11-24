package com.example.socorristajunior.ui.components


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.socorristajunior.ui.screens.details.EmergencyStep
import coil.compose.AsyncImage


@Composable
fun EmergencyDetailContent(
    steps: List<EmergencyStep>,
    corGravidade: Color
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        steps.forEach { step ->
            StepCard(
                step = step,
                corGravidade = corGravidade
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StepCard(
    step: EmergencyStep,
    corGravidade: Color
    ) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    val rotationState by androidx.compose.animation.core.animateFloatAsState( targetValue = if (expanded) 180f else 0f, label = "Rotation")

    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable { expanded = !expanded }
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = corGravidade.copy(alpha = 0.15f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = null
    ){
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Separa texto da seta
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(color = corGravidade, shape = RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 6.dp) // espaço dentro da caixinha
                ) {
                    Text(
                        text = "Passo ${step.stepNumber} de ${step.totalSteps}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expandir passo",
                    modifier = Modifier.rotate(rotationState)
                )
            }

            Text(
                text = step.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Espaço reservado para imagem ilustrativa

            if (!step.photo.isNullOrEmpty()) {
                AsyncImage(
                    model = step.photo,
                    contentDescription = "Imagem de ${step.title}",
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Text(
                text = step.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = if (expanded) Int.MAX_VALUE else 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!expanded) {
                Text(
                    text = "Toque para ver detalhes...",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

