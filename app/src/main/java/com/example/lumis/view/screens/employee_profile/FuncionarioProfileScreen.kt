package com.example.lumis.view.screens.employee_profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lumis.view.theme.TemaApp.corTema
import java.io.File

object ProfileColors {
    val Background: Color get() = corTema(Color(0xFFF5F6FA), Color(0xFF171918))
    val CardBackground: Color get() = corTema(Color.White, Color(0xFF242725))
    val Primary = Color(0xFF50BF87)
    val PrimaryDark = Color(0xFF317252)
    val PrimaryLight: Color get() = corTema(Color(0xFFE7F5EC), Color(0xFF1F3A2E))
    val TextPrimary: Color get() = corTema(Color(0xFF1A1A1A), Color(0xFFF2F4F3))
    val TextSecondary: Color get() = corTema(Color(0xFF666666), Color(0xFFADB5B0))
    val Divider: Color get() = corTema(Color(0xFFE7E9EC), Color(0xFF3A403C))
    val ChipRedBg: Color get() = corTema(Color(0xFFFBDCDC), Color(0xFF4A2A2A))
    val ChipGreenIcon = Color(0xFF2E86C1)
    val BadgeRedText: Color get() = corTema(Color(0xFFB5495F), Color(0xFFFF9BAD))
    val BadgeGreenText: Color get() = corTema(Color(0xFF1F9E6E), Color(0xFF5FD3A0))
}

data class FuncionarioProfile(
    val nome: String,
    val localizacao: String,
    val telefone: String,
    val fotoPath: String? = null,
    val bannerPath: String? = null,
    val biografia: String = "",
    val especialidades: String = "",
    val regiaoAtuacao: String = "",
    val experiencia: String = "",
    val valorMinimoCentavos: Long = 0,
    val mediaAvaliacoes: Double? = null,
    val quantidadeAvaliacoes: Int = 0
)

@Composable
fun FuncionarioProfileScreen(
    profile: FuncionarioProfile,
    onEditarClick: () -> Unit = {},
    onConfigClick: () -> Unit = {},
    onCalendarioClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(ProfileColors.Background),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { CabecalhoProfissional(profile, onEditarClick, onConfigClick) }
        item {
            Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (profile.mediaAvaliacoes != null) {
                    Surface(shape = RoundedCornerShape(18.dp), color = ProfileColors.CardBackground, border = BorderStroke(1.dp, ProfileColors.Divider)) {
                        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, null, tint = Color(0xFFFFB800), modifier = Modifier.size(30.dp))
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text("%.1f de 5".format(profile.mediaAvaliacoes), fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                                Text("${profile.quantidadeAvaliacoes} avaliações registradas", color = ProfileColors.TextSecondary)
                            }
                        }
                    }
                } else {
                    Surface(shape = RoundedCornerShape(18.dp), color = ProfileColors.CardBackground, border = BorderStroke(1.dp, ProfileColors.Divider)) {
                        Text("Ainda não há avaliações registradas.", Modifier.padding(16.dp), color = ProfileColors.TextSecondary)
                    }
                }

                InformacaoCard("Sobre mim", profile.biografia.ifBlank { "Adicione uma apresentação ao seu perfil." })
                InformacaoCard("Especialidades", profile.especialidades.ifBlank { "Especialidades ainda não informadas." })
                InformacaoCard("Região de atendimento", profile.regiaoAtuacao.ifBlank { profile.localizacao })
                InformacaoCard("Experiência", profile.experiencia.ifBlank { "Experiência ainda não informada." })
                if (profile.valorMinimoCentavos > 0) {
                    InformacaoCard("Valor mínimo", "R$ ${"%.2f".format(profile.valorMinimoCentavos / 100.0).replace('.', ',')} por serviço")
                }

                Button(
                    onClick = onCalendarioClick,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ProfileColors.Primary)
                ) {
                    Icon(Icons.Outlined.CalendarMonth, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Abrir agenda", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun CabecalhoProfissional(profile: FuncionarioProfile, onEditarClick: () -> Unit, onConfigClick: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(270.dp)) {
        if (!profile.bannerPath.isNullOrBlank() && File(profile.bannerPath).exists()) {
            AsyncImage(File(profile.bannerPath), null, Modifier.fillMaxWidth().aspectRatio(2f), contentScale = ContentScale.Crop)
        } else {
            Box(Modifier.fillMaxWidth().height(150.dp).background(Brush.linearGradient(listOf(ProfileColors.PrimaryDark, ProfileColors.Primary))))
        }
        Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onEditarClick) { Icon(Icons.Outlined.Edit, "Editar", tint = Color.White) }
            IconButton(onClick = onConfigClick) { Icon(Icons.Outlined.Settings, "Configurações", tint = Color.White) }
        }
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 20.dp).fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = ProfileColors.CardBackground,
            shadowElevation = 4.dp
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(76.dp).clip(CircleShape).background(ProfileColors.PrimaryLight), contentAlignment = Alignment.Center) {
                    if (!profile.fotoPath.isNullOrBlank() && File(profile.fotoPath).exists()) {
                        AsyncImage(File(profile.fotoPath), profile.nome, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else Icon(Icons.Outlined.Person, null, Modifier.size(36.dp), tint = ProfileColors.PrimaryDark)
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(profile.nome, fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.LocationOn, null, Modifier.size(16.dp), tint = ProfileColors.Primary)
                        Text(profile.localizacao, color = ProfileColors.TextSecondary)
                    }
                    if (profile.telefone.isNotBlank()) Text(profile.telefone, color = ProfileColors.TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun InformacaoCard(titulo: String, conteudo: String) {
    Surface(shape = RoundedCornerShape(18.dp), color = ProfileColors.CardBackground, border = BorderStroke(1.dp, ProfileColors.Divider)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(titulo, fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
            Spacer(Modifier.height(5.dp))
            Text(conteudo, color = ProfileColors.TextSecondary)
        }
    }
}
