package com.example.lumis.view.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.lumis.controller.AuthController
import com.example.lumis.view.components.LumisCard
import com.example.lumis.view.components.LumisPageTitle
import com.example.lumis.view.components.LumisTag
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisGreenLight
import com.example.lumis.view.theme.LumisTextPrimary
import com.example.lumis.view.theme.LumisTextSecondary

@Composable
fun ProfileChoiceScreen(authController: AuthController) {
    var visivel by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visivel = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(LumisGreenLight.copy(alpha = 0.72f), LumisBackground, LumisBackground)
                )
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        IconButton(onClick = authController::abrirLogin) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = "Voltar")
        }
        Spacer(Modifier.size(20.dp))

        AnimatedVisibility(
            visible = visivel,
            enter = fadeIn(tween(360)) + slideInVertically(tween(360)) { it / 10 }
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(22.dp)) {
                LumisTag("ACESSO LUMIS")
                LumisPageTitle(
                    title = "Como você quer usar a Lumis?",
                    subtitle = "Escolha seu perfil agora. Você poderá completar seus dados na próxima etapa."
                )
                ProfileOptionCard(
                    icon = Icons.Outlined.PersonOutline,
                    title = "Sou cliente",
                    description = "Quero encontrar profissionais, contratar serviços e organizar meus agendamentos.",
                    action = "Continuar como cliente",
                    onClick = { authController.cadastrarComo("Cliente") }
                )
                ProfileOptionCard(
                    icon = Icons.Outlined.CleaningServices,
                    title = "Sou profissional",
                    description = "Quero apresentar meus serviços, receber oportunidades e gerenciar minha agenda.",
                    action = "Continuar como profissional",
                    onClick = { authController.cadastrarComo("Profissional") }
                )
            }
        }
    }
}

@Composable
private fun ProfileOptionCard(
    icon: ImageVector,
    title: String,
    description: String,
    action: String,
    onClick: () -> Unit
) {
    LumisCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .size(54.dp)
                    .background(LumisGreenLight, RoundedCornerShape(16.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(icon, contentDescription = null, tint = LumisGreen, modifier = Modifier.size(26.dp))
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(title, style = MaterialTheme.typography.titleLarge, color = LumisTextPrimary)
                Text(description, style = MaterialTheme.typography.bodyMedium, color = LumisTextSecondary)
                Spacer(Modifier.width(1.dp))
                Text(action, style = MaterialTheme.typography.labelLarge, color = LumisGreen)
            }
        }
    }
}
