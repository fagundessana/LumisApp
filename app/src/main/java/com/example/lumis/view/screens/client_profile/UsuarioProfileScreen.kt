package com.example.lumis.view.screens.client_profile

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
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lumis.R
import com.example.lumis.view.screens.employee_profile.ProfileColors
import com.example.lumis.controller.ModoIdioma
import com.example.lumis.view.components.IdiomaSelector
import java.io.File

data class UsuarioProfile(
    val nome: String,
    val localizacao: String,
    val fotoUrl: String? = null,
    val biografia: String = "",
    val bannerUrl: String? = null,
    val planoAtivo: String? = null,
    val totalAgendamentos: Int = 0,
    val totalConcluidos: Int = 0,
    val atividadesRecentes: List<AtividadePerfil> = emptyList()
)

data class AtividadePerfil(val servico: String, val data: String, val status: String)

@Composable
fun UsuarioProfileScreen(
    profile: UsuarioProfile,
    onEditarClick: () -> Unit = {},
    onConfigClick: () -> Unit = {},
    onMembroLumisClick: () -> Unit = {},
    onNotificacoesClick: () -> Unit = {},
    onServicosClick: () -> Unit = {},
    onAgendaClick: () -> Unit = {},
    modoIdioma: ModoIdioma = ModoIdioma.SISTEMA,
    onModoIdiomaChange: (ModoIdioma) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(ProfileColors.Background),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { CabecalhoCliente(profile, onEditarClick, onConfigClick) }
        item {
            Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                if (profile.biografia.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = ProfileColors.CardBackground,
                        border = BorderStroke(1.dp, ProfileColors.Divider)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(16.dp)) {
                            Text(stringResource(R.string.about_me), fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                            Spacer(Modifier.height(5.dp))
                            Text(profile.biografia, color = ProfileColors.TextSecondary)
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ResumoPerfil(stringResource(R.string.scheduled_services_short), profile.totalAgendamentos.toString(), Modifier.weight(1f))
                    ResumoPerfil(stringResource(R.string.status_completed), profile.totalConcluidos.toString(), Modifier.weight(1f))
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ProfileColors.PrimaryLight,
                    border = BorderStroke(1.dp, ProfileColors.Primary.copy(alpha = 0.35f)),
                    onClick = onMembroLumisClick
                ) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.WorkspacePremium, null, tint = ProfileColors.PrimaryDark)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                profile.planoAtivo ?: stringResource(R.string.discover_lumis_plans),
                                fontWeight = FontWeight.Bold,
                                color = ProfileColors.TextPrimary
                            )
                            Text(
                                if (profile.planoAtivo == null) stringResource(R.string.plan_benefits_hint)
                                else stringResource(R.string.active_plan),
                                color = ProfileColors.TextSecondary
                            )
                        }
                        Icon(Icons.Outlined.ChevronRight, null, tint = ProfileColors.PrimaryDark)
                    }
                }

                Text(stringResource(R.string.quick_access), fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AtalhoPerfil(Icons.Outlined.CleaningServices, stringResource(R.string.services), onServicosClick, Modifier.weight(1f))
                    AtalhoPerfil(Icons.Outlined.CalendarMonth, stringResource(R.string.nav_schedule), onAgendaClick, Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AtalhoPerfil(Icons.Outlined.Notifications, stringResource(R.string.notifications), onNotificacoesClick, Modifier.weight(1f))
                    AtalhoPerfil(Icons.Outlined.WorkspacePremium, stringResource(R.string.plans), onMembroLumisClick, Modifier.weight(1f))
                }

                if (profile.atividadesRecentes.isNotEmpty()) {
                    Text(stringResource(R.string.recent_activity), fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = ProfileColors.CardBackground,
                        border = BorderStroke(1.dp, ProfileColors.Divider)
                    ) {
                        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                            profile.atividadesRecentes.forEachIndexed { index, atividade ->
                                Row(Modifier.fillMaxWidth().padding(vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(38.dp).clip(RoundedCornerShape(12.dp)).background(ProfileColors.PrimaryLight), contentAlignment = Alignment.Center) {
                                        Icon(Icons.Outlined.CleaningServices, null, tint = ProfileColors.PrimaryDark)
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text(atividade.servico, fontWeight = FontWeight.Medium, color = ProfileColors.TextPrimary)
                                        Text(atividade.data, color = ProfileColors.TextSecondary)
                                    }
                                    Text(
                                        stringResource(
                                            when (atividade.status) {
                                                "Cancelado" -> R.string.status_cancelled
                                                "Concluído" -> R.string.status_completed
                                                else -> R.string.status_scheduled
                                            }
                                        ),
                                        color = ProfileColors.PrimaryDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                if (index != profile.atividadesRecentes.lastIndex) {
                                    Box(Modifier.fillMaxWidth().height(1.dp).background(ProfileColors.Divider))
                                }
                            }
                        }
                    }
                }
                IdiomaSelector(modoIdioma, onModoIdiomaChange)

                Button(
                    onClick = onEditarClick,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ProfileColors.Primary)
                ) {
                    Icon(Icons.Outlined.Edit, null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.edit_profile), fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun CabecalhoCliente(
    profile: UsuarioProfile,
    onEditarClick: () -> Unit,
    onConfigClick: () -> Unit
) {
    Box(Modifier.fillMaxWidth().height(292.dp)) {
        if (!profile.bannerUrl.isNullOrBlank() && File(profile.bannerUrl).exists()) {
            AsyncImage(File(profile.bannerUrl), null, Modifier.fillMaxWidth().aspectRatio(2f), contentScale = ContentScale.Crop)
        } else {
            Box(
                Modifier.fillMaxWidth().height(150.dp)
                    .background(Brush.linearGradient(listOf(ProfileColors.PrimaryDark, ProfileColors.Primary)))
            )
        }
        Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(ProfileColors.Primary), contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.lumis), "Lumis", tint = Color.White, modifier = Modifier.size(25.dp))
            }
            Row {
                IconButton(onClick = onEditarClick, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(ProfileColors.CardBackground.copy(alpha = 0.82f))) {
                    Icon(Icons.Outlined.Edit, stringResource(R.string.edit_profile), tint = ProfileColors.PrimaryDark)
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = onConfigClick, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(ProfileColors.CardBackground.copy(alpha = 0.82f))) {
                    Icon(Icons.Outlined.Settings, stringResource(R.string.settings), tint = ProfileColors.PrimaryDark)
                }
            }
        }
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter).padding(horizontal = 20.dp).fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            color = ProfileColors.CardBackground,
            shadowElevation = 4.dp
        ) {
            Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(76.dp).clip(CircleShape).background(ProfileColors.PrimaryLight),
                    contentAlignment = Alignment.Center
                ) {
                    if (!profile.fotoUrl.isNullOrBlank() && File(profile.fotoUrl).exists()) {
                        AsyncImage(File(profile.fotoUrl), profile.nome, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Icon(Icons.Outlined.Person, null, Modifier.size(36.dp), tint = ProfileColors.PrimaryDark)
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(profile.nome, fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                    Text(stringResource(R.string.lumis_client), color = ProfileColors.PrimaryDark, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.LocationOn, null, Modifier.size(16.dp), tint = ProfileColors.Primary)
                        Spacer(Modifier.width(3.dp))
                        Text(profile.localizacao, color = ProfileColors.TextSecondary)
                    }
                }
            }
        }
    }
}

@Composable
private fun AtalhoPerfil(icon: ImageVector, titulo: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(92.dp),
        shape = RoundedCornerShape(16.dp),
        color = ProfileColors.CardBackground,
        border = BorderStroke(1.dp, ProfileColors.Divider)
    ) {
        Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = ProfileColors.Primary)
            Spacer(Modifier.height(8.dp))
            Text(titulo, color = ProfileColors.TextPrimary, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ResumoPerfil(titulo: String, valor: String, modifier: Modifier = Modifier) {
    Surface(modifier, shape = RoundedCornerShape(18.dp), color = ProfileColors.CardBackground, border = BorderStroke(1.dp, ProfileColors.Divider)) {
        Column(Modifier.padding(16.dp)) {
            Text(valor, color = ProfileColors.PrimaryDark, fontWeight = FontWeight.Bold)
            Text(titulo, color = ProfileColors.TextSecondary)
        }
    }
}
