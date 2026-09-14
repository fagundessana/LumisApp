// Navegação do grupo: nomes das telas que aparecem na barra de baixo.
// A gente usou sealed class porque assim o Kotlin ajuda a não errar o nome
// da rota (se escrever errado, nem compila).

package com.example.lumis.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

// Cada objeto aqui é uma "página": nome da rota + texto + ícone.
sealed class LumisRoute(val route: String, val label: String, val icon: ImageVector) {
    data object Home : LumisRoute("home", "Home", Icons.Filled.Home)
    data object Cidade : LumisRoute("cidade", "Cidade", Icons.Filled.Apartment)
    data object Explorar : LumisRoute("explorar", "Explorar", Icons.Filled.Search)
    data object Agenda : LumisRoute("agenda", "Agenda", Icons.Filled.CalendarMonth)
    data object Perfil : LumisRoute("perfil", "Perfil", Icons.Filled.Person)

    companion object {
        // A ordem que os botões aparecem na barra de baixo.
        // Pra adicionar uma tela nova: cria o objeto aqui, a tela, e o
        // composable() lá no MainActivity. Só isso!
        val bottomItems: List<LumisRoute> = listOf(Home, Cidade, Explorar, Agenda, Perfil)
    }
}
