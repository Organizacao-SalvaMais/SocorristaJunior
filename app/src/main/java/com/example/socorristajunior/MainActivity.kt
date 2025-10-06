package com.example.socorristajunior

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.socorristajunior.ui.details.EmergencyDetailScreen
import com.example.socorristajunior.ui.emergencies.EmergenciesScreen
import com.example.socorristajunior.ui.home.HomeScreen
import com.example.socorristajunior.ui.quiz.QuizScreen
import com.example.socorristajunior.ui.theme.SocorristaJuniorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            SocorristaJuniorTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home"){
        composable("home"){
            HomeScreen(navController = navController)
        }
        composable("quiz") {
            QuizScreen(navController = navController)
        }
        composable("emergencies") {
            EmergenciesScreen(navController = navController)
        }

        // ROTA PARA A TELA DE DETALHES COM ARGUMENTO
        composable(
            route = "emergency_detail/{emergencyId}",
            arguments = listOf(navArgument("emergencyId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Extrai o ID da emergência da rota
            val emergencyId = backStackEntry.arguments?.getInt("emergencyId") ?: 0
            // Chama a tela de detalhes, passando o ID e o navController
            EmergencyDetailScreen(
                emergencyId = emergencyId,
                navController = navController
            )
        }
    }
}
/*
@Composable
fun EmergenciesScreen(navController: NavHostController) {
    TODO("Not yet implemented")
}*/