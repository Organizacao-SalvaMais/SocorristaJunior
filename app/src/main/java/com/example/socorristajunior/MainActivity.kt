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
import com.example.socorristajunior.ui.screens.profile.ProfileScreen
import com.example.socorristajunior.ui.screens.cadastro.CadastroScreen
import com.example.socorristajunior.ui.screens.details.EmergencyDetailScreen
import com.example.socorristajunior.ui.screens.editProfile.EditProfileScreen
import com.example.socorristajunior.ui.screens.emergencies.EmergenciesScreen
import com.example.socorristajunior.ui.screens.forgotPassorword.ForgotPasswordScreen
import com.example.socorristajunior.ui.screens.home.HomeScreen
import com.example.socorristajunior.ui.screens.login.LoginScreen
import com.example.socorristajunior.ui.screens.quiz.home.QuizHomeRoute
import com.example.socorristajunior.ui.screens.quiz.question.QuizQuestionRoute
import com.example.socorristajunior.ui.screens.quiz.question.QuizResultArgs
import com.example.socorristajunior.ui.screens.quiz.result.QuizResultRoute
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
        // Rota 1: Tela Inicial do Quiz (Seleção de Dificuldade)
        // (Substituindo seu antigo "quizScreen")
        composable(route = "quiz_home") {
            // CORRETO: Chame a função 'publica' QuizHomeRoute.
            // Ela vai lidar com o ViewModel e chamar a 'private' QuizHomeScreen.
            QuizHomeRoute(
                onNavigateToQuiz = { categoryId ->
                    navController.navigate("quiz_question/$categoryId")
                },
                onNavigateBack = { // Define a ação que deve ser executada ao clicar no botão "voltar"
                    navController.popBackStack() // Informa ao controlador de navegação para voltar à tela anterior na pilha
                }
            )
        }

        // Rota 2: Tela de Pergunta (Question)
        // (Substituindo seu antigo "questionScreen")
        composable(
            route = "quiz_question/{categoryId}", // ⬅️ Recebe o ID
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType }
            )
        ) {
            // O QuizQuestionViewModel (dentro do QuizQuestionRoute)
            // pegará o "categoryId" automaticamente.
            QuizQuestionRoute(
                // Ação de navegação: Ir para a tela de resultados
                onNavigateToResults = { args: QuizResultArgs ->
                    // Navega para os resultados e limpa a tela de perguntas da pilha
                    navController.navigate("quiz_result/${args.score}/${args.totalQuestions}") {
                        // Isso remove a tela de perguntas do "histórico"
                        // para que o usuário não possa "voltar" para ela.
                        popUpTo("quiz_home")
                    }
                }
            )
        }

        // Rota 3: Tela de Resultados (Results)
        // (Substituindo seu antigo "quiz_result")
        composable(
            route = "quiz_result/{score}/{totalQuestions}", // ️ Recebe os args
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("totalQuestions") { type = NavType.IntType }
            )
        ) {
            // O QuizResultsViewModel (dentro do QuizResultsRoute)
            // pegará os argumentos "score" e "totalQuestions" automaticamente.
            QuizResultRoute(
                // Ação de navegação: Voltar para a Home do Quiz
                onRestart = {
                    // Limpa a pilha de navegação e volta para a home do quiz
                    navController.navigate("quiz_home") {
                        popUpTo("quiz_home") { inclusive = true }
                    }
                }
            )
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
        composable("cadastro") {
            CadastroScreen(navController = navController)
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navController = navController)
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