package com.example.socorristajunior

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.socorristajunior.ui.theme.SocorristaJuniorTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.socorristajunior.ui.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import com.example.socorristajunior.ui.emergencies.EmergenciesScreen
import com.example.socorristajunior.ui.quiz.QuizScreen


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
    }
}