package com.example.socorristajunior

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.socorristajunior.ui.screens.Profile.ProfileScreen
/*import com.example.socorristajunior.ui.screens.cadastro.CadastroScreen*/
import com.example.socorristajunior.ui.screens.details.EmergencyDetailScreen
import com.example.socorristajunior.ui.screens.editProfile.EditProfileScreen
import com.example.socorristajunior.ui.screens.emergencies.EmergenciesScreen
import com.example.socorristajunior.ui.screens.home.HomeScreen
import com.example.socorristajunior.ui.screens.login.LoginScreen
import com.example.socorristajunior.ui.screens.quiz.QuizHomeScreen
import com.example.socorristajunior.ui.screens.quiz.QuizResultScreen
import com.example.socorristajunior.ui.screens.quiz.QuizQuestionScreen
import com.example.socorristajunior.ui.theme.SocorristaJuniorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        installSplashScreen()
        setContent {
            SocorristaJuniorTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("quizScreen") {
            QuizHomeScreen(navController = navController)
        }
        composable("questionScreen") {
            QuizQuestionScreen( navController = navController)
        }

        // 🔹 TELA DE RESULTADO
        composable(
            route = "quiz_result/{score}/{total}",
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            QuizResultScreen(navController = navController, score = score, total = total)
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("emergencies") {
            EmergenciesScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("edit_profile") {
            EditProfileScreen(navController = navController)
        }



        /* composable("cadastro") {
            CadastroScreen(navController = navController)
        }*/


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
