package com.example.socorristajunior

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.socorristajunior.ui.theme.SocorristaJuniorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SocorristaJuniorTheme {

                AppNavigator()
            }
        }
    }
}


@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "funcionalidades") {
        composable("funcionalidades") {
            FuncionalidadesScreen(navController = navController)
        }
        composable("quiz") {
            QuizScreen(navController = navController)
        }
        composable("emergencias") {
            EmergenciasScreen(navController = navController)
        }
    }
}


@Composable
fun FuncionalidadesScreen(navController: NavController) {
    Surface(
        color = Color(0xFFFAF7F2),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SALVAR+",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF424242)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Card do Quiz
            FeatureCard(
                icon = Icons.Filled.Psychology,
                iconColor = Color(0xFFE57373),
                title = "Quiz Interativo",
                description = "Teste seus conhecimentos sobre primeiros socorros com nosso quiz educativo.",
                buttonText = "Iniciar Quiz",
                buttonColor = Color(0xFFF2D16B),
                onClick = {
                    navController.navigate("quiz")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card de Emergências
            FeatureCard(
                icon = Icons.Default.Warning,
                iconColor = Color(0xFFD92B2B),
                title = "Emergências",
                description = "Passo a passo para situações de emergência.",
                buttonText = "Ver Procedimentos",
                buttonColor = Color(0xFFD92B2B),
                onClick = {
                    navController.navigate("emergencias")
                }
            )
        }
    }
}


@Composable
fun FeatureCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String,
    buttonText: String,
    buttonColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF424242)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                val textColor = if (buttonColor == Color(0xFFF2D16B)) Color.Black else Color.White
                Text(text = buttonText, color = textColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}



@Composable
fun QuizScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Tela do Quiz", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Voltar")
            }
        }
    }
}

@Composable
fun EmergenciasScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Tela de Emergências", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Voltar")
            }
        }
    }
}



@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun FuncionalidadesScreenPreview() {
    SocorristaJuniorTheme {
        FuncionalidadesScreen(navController = rememberNavController())
    }
}