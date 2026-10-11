package com.example.lumis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.controller.AuthController
import com.example.lumis.view.screens.auth.CadastroScreen
import com.example.lumis.view.screens.auth.LoginScreen
import kotlinx.coroutines.delay
import com.example.lumis.view.screens.splashscreen.SplashScreen
import com.example.lumis.view.MainScaffold

private const val SPLASH_DURATION_MS = 1800L

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LumisTheme {
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
                onLogout = { authController.logout() }
            )
            authController.mostrarCadastro -> CadastroScreen(authController)
            else -> LoginScreen(authController)
        }
    }
}