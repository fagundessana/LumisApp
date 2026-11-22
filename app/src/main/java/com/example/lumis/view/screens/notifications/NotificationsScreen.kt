package com.example.lumis.view.screens.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.lumis.R
import com.example.lumis.data.Notificacao
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.screens.employee_profile.ProfileColors
import com.example.lumis.view.theme.LumisBackground
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun NotificationsScreen(
    notificacoes: List<Notificacao>,
    onBack: () -> Unit = {},
    onMarcarLida: (Long) -> Unit = {},
    onMarcarTodas: () -> Unit = {}
) {
    val hoje = LocalDate.now()
    val deHoje = notificacoes.filter { it.dataLocal() == hoje }
    val anteriores = notificacoes.filterNot { it.dataLocal() == hoje }

    Column(Modifier.fillMaxSize().background(LumisBackground)) {
        LumisTopBar(onBack = onBack)
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.notifications), Modifier.weight(1f), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
            if (notificacoes.any { !it.lida }) {
                TextButton(onClick = onMarcarTodas) { Text(stringResource(R.string.mark_as_read), color = ProfileColors.PrimaryDark) }
            }
        }

        if (notificacoes.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(bottom = 72.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.size(78.dp).clip(CircleShape).background(ProfileColors.PrimaryLight), contentAlignment = Alignment.Center) {
                        Icon(Icons.Outlined.NotificationsNone, null, Modifier.size(36.dp), tint = ProfileColors.Primary)
                    }
                    Text(stringResource(R.string.no_notifications_title), fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                    Text(stringResource(R.string.no_notifications_hint), color = ProfileColors.TextSecondary, textAlign = TextAlign.Center)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (deHoje.isNotEmpty()) {
                    item { TituloGrupo(stringResource(R.string.today)) }
                    items(deHoje, key = { it.id }) { NotificacaoCard(it) { onMarcarLida(it.id) } }
                }
                if (anteriores.isNotEmpty()) {
                    item { TituloGrupo(stringResource(R.string.previous_notifications)) }
                    items(anteriores, key = { it.id }) { NotificacaoCard(it) { onMarcarLida(it.id) } }
                }
            }
        }
    }
}

@Composable
private fun TituloGrupo(texto: String) {
    Text(texto, Modifier.padding(top = 8.dp, bottom = 4.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
}

@Composable
private fun NotificacaoCard(notificacao: Notificacao, onClick: () -> Unit) {
    val (icone, cor) = iconeNotificacao(notificacao.tipo)
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = ProfileColors.CardBackground,
        border = BorderStroke(1.dp, if (notificacao.lida) ProfileColors.Divider else ProfileColors.Primary.copy(alpha = .35f)),
        shadowElevation = if (notificacao.lida) 0.dp else 2.dp
    ) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.Top) {
            Box(Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(ProfileColors.PrimaryLight), contentAlignment = Alignment.Center) {
                Icon(icone, null, tint = cor)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(notificacao.titulo, Modifier.weight(1f), fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                    Text(notificacao.horaFormatada(), fontSize = 11.sp, color = ProfileColors.TextSecondary)
                    if (!notificacao.lida) {
                        Spacer(Modifier.width(7.dp))
                        Box(Modifier.size(8.dp).clip(CircleShape).background(ProfileColors.Primary))
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(notificacao.descricao, fontSize = 13.sp, color = ProfileColors.TextSecondary)
            }
        }
    }
}

private fun iconeNotificacao(tipo: String): Pair<ImageVector, Color> = when (tipo) {
    "FAVORITO" -> Icons.Outlined.Favorite to Color(0xFF15945F)
    "DESFAVORITO" -> Icons.Outlined.FavoriteBorder to Color(0xFF4E7865)
    "AGENDAMENTO" -> Icons.Outlined.EventAvailable to Color(0xFF15945F)
    "REAGENDAMENTO" -> Icons.Outlined.EventRepeat to Color(0xFF3478B8)
    "CONCLUSAO" -> Icons.Outlined.TaskAlt to Color(0xFF15945F)
    "AVALIACAO" -> Icons.Outlined.Star to Color(0xFFFFB000)
    else -> Icons.Outlined.NotificationsNone to Color(0xFF15945F)
}

private fun Notificacao.dataLocal(): LocalDate = Instant.ofEpochMilli(criadoEmMillis).atZone(ZoneId.systemDefault()).toLocalDate()
private fun Notificacao.horaFormatada(): String = Instant.ofEpochMilli(criadoEmMillis).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm"))
