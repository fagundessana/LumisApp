package com.example.lumis.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lumis.navigation.LumisRoute
import com.example.lumis.ui.theme.LumisColors
import com.example.lumis.ui.theme.Questrial

/**
 * Barra de baixo do app (Home, Cidade, Explorar, Agenda, Perfil).
 * Ela é clicável de verdade: cada botão manda o navController pra rota
 * certa. O "Explorar" do meio é maiorzinho com a bolinha, igual ao protótipo.
 * Pra criar uma página nova: adiciona a rota no LumisNav + o composable()
 * no MainActivity. Se for item da barra, põe na lista bottomItems também.
 */
@Composable
fun LumisBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Descobre em qual tela a gente tá pra pintar o ícone certo de verde.
    val backStack = navController.currentBackStackEntryAsState().value
    val currentRoute = backStack?.destination?.route

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = LumisColors.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LumisRoute.bottomItems.forEach { item ->
                val selected = currentRoute == item.route
                if (item is LumisRoute.Explorar) {
                    ExplorerItem(
                        label = item.label,
                        icon = item.icon,
                        onClick = { navigateTo(navController, item.route) }
                    )
                } else {
                    BottomItem(
                        selected = selected,
                        label = item.label,
                        icon = item.icon,
                        onClick = { navigateTo(navController, item.route) }
                    )
                }
            }
        }
    }
}

private fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun BottomItem(
    selected: Boolean,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val color = if (selected) LumisColors.Primary else LumisColors.InactiveNav
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontFamily = Questrial,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = color
        )
    }
}

@Composable
private fun ExplorerItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(LumisColors.ExplorerCircle),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = LumisColors.PrimaryHover,
                modifier = Modifier.size(26.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontFamily = Questrial,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = LumisColors.PrimaryHover
        )
    }
}
