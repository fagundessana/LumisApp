package com.example.lumis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lumis.navigation.LumisRoute
import com.example.lumis.ui.home.HomeScreen
import com.example.lumis.ui.home.components.LumisBottomBar
import com.example.lumis.ui.placeholder.AgendaScreen
import com.example.lumis.ui.placeholder.CidadeScreen
import com.example.lumis.ui.placeholder.ExplorarScreen
import com.example.lumis.ui.placeholder.PerfilScreen
import com.example.lumis.ui.screens.splash.SplashScreen
import com.example.lumis.ui.theme.LumisTheme
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MS = 1800L

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LumisTheme {
                var mostrarSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(SPLASH_DURATION_MS)
                    mostrarSplash = false
                }

                Crossfade(targetState = mostrarSplash, label = "splash") { splashVisivel ->
                    if (splashVisivel) {
                        SplashScreen()
                    } else {
                        LumisApp()
                    }
                }
            }
        }
    }
}

// LumisApp: monta o esqueleto do app (barra de baixo + conteúdo que troca).
@Composable
fun LumisApp() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { LumisBottomBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LumisRoute.Home.route
        ) {
            composable(LumisRoute.Home.route) {
                HomeScreen(contentPadding = innerPadding)
            }
            composable(LumisRoute.Cidade.route) {
                CidadeScreen(padding = innerPadding)
            }
            composable(LumisRoute.Explorar.route) {
                ExplorarScreen(padding = innerPadding)
            }
            composable(LumisRoute.Agenda.route) {
                AgendaScreen(padding = innerPadding)
            }
            composable(LumisRoute.Perfil.route) {
                PerfilScreen(padding = innerPadding)
            }
        }
    }
}