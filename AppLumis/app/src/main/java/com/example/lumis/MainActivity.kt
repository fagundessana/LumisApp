// Projeto em grupo - App Lumis (Home)
// MainActivity: é a primeira tela que o Android abre quando o app inicia.
// A gente usa Jetpack Compose aqui, então não tem XML de layout, é tudo código Kotlin.

package com.example.lumis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.lumis.ui.theme.LumisTheme

// A Activity principal. O Android sempre começa por aqui (tá no AndroidManifest).
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Faz o app usar a tela toda, até embaixo da barra de status.
        enableEdgeToEdge()
        // setContent é onde a gente "desenha" a interface com Compose.
        setContent {
            // LumisTheme é o nosso tema (cores + fontes), que o grupo definiu.
            LumisTheme {
                LumisApp()
            }
        }
    }
}

// LumisApp: monta o esqueleto do app (barra de baixo + conteúdo que troca).
// Como fazer pra adicionar uma página nova no futuro:
//  1. Criar o @Composable da tela,
//  2. Criar a rota dela no arquivo LumisNav.kt,
//  3. Adicionar um composable() aqui dentro do NavHost.
@Composable
fun LumisApp() {
    // O navController é quem controla pra qual tela o app vai.
    val navController = rememberNavController()
    // Scaffold já deixa um espaço pronto pra barra de baixo (bottomBar).
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { LumisBottomBar(navController = navController) }
    ) { innerPadding ->
        // NavHost = "onde as telas aparecem". O startDestination é a Home.
        NavHost(
            navController = navController,
            startDestination = LumisRoute.Home.route
        ) {
            // Cada composable() abaixo é uma "página" do app.
            composable(LumisRoute.Home.route) {
                // A Home recebe o innerPadding pra não ficar embaixo da navbar.
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

// Preview: serve pra gente ver a tela no Android Studio sem precisar rodar no celular.
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun LumisAppPreview() {
    LumisTheme {
        LumisApp()
    }
}
