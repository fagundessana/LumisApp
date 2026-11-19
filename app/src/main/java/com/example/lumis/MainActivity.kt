package com.example.lumis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.lumis.controller.AuthController
import com.example.lumis.view.MainScaffold
import com.example.lumis.view.screens.auth.CadastroScreen
import com.example.lumis.view.screens.auth.LoginScreen
import com.example.lumis.view.screens.splashscreen.SplashScreen
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ThemeMode
import com.example.lumis.view.theme.ThemeRepository
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MS = 1800L

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 1) Lê a preferência de tema salva (claro / escuro / padrão do sistema)
            val themeRepo = remember { ThemeRepository(applicationContext) }
            val mode by themeRepo.themeMode.collectAsState(initial = ThemeMode.SYSTEM)

            // 2) Converte a preferência em "escuro ou não"
            val dark = when (mode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            // 3) Passa o resultado para o tema (esta é a única chamada de LumisTheme do app)
            LumisTheme(darkTheme = dark) {
                val authController = remember { AuthController(applicationContext) }
                AuthRoot(authController)
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// RAIZ: splash -> login/cadastro -> app (perfil conforme o tipo de usuário)
// ---------------------------------------------------------------------------------------------

@Composable
fun AuthRoot(authController: AuthController) {
    var mostrarSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        mostrarSplash = false
    }

    // Restaura a sessão do último usuário logado
    LaunchedEffect(Unit) {
        authController.restaurarSessao()
    }

    val usuario = authController.usuarioLogado

    Crossfade(targetState = mostrarSplash, label = "splash") { splashVisivel ->
        when {
            splashVisivel -> SplashScreen()
            usuario != null -> MainScaffold(
                usuario = usuario,
                authController = authController,
                onLogout = { authController.logout() }
            )
            authController.mostrarCadastro -> CadastroScreen(authController)
            else -> LoginScreen(authController)
        }
    }
}