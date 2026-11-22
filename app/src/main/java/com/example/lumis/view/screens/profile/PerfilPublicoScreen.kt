package com.example.lumis.view.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lumis.R
import com.example.lumis.data.User
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.screens.employee_profile.ProfileColors
import java.io.File

@Composable
fun PerfilPublicoScreen(
    pessoa: User,
    mediaAvaliacoes: Double? = null,
    quantidadeAvaliacoes: Int = 0,
    totalConcluidos: Int = 0,
    onVoltar: () -> Unit,
    onContratar: (() -> Unit)? = null
) {
    val profissional = pessoa.tipoUsuario != "Cliente"
    LazyColumn(
        Modifier.fillMaxSize().background(ProfileColors.Background),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Box(Modifier.fillMaxWidth().height(276.dp)) {
                if (pessoa.bannerPerfilPath.isNotBlank() && File(pessoa.bannerPerfilPath).exists()) {
                    AsyncImage(File(pessoa.bannerPerfilPath), null, Modifier.fillMaxWidth().height(160.dp), contentScale = ContentScale.Crop)
                } else {
                    Box(Modifier.fillMaxWidth().height(160.dp).background(Brush.linearGradient(listOf(ProfileColors.PrimaryDark, ProfileColors.Primary))))
                }
                LumisTopBar(onBack = onVoltar)
                Surface(
                    Modifier.align(Alignment.BottomCenter).padding(horizontal = 20.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp), color = ProfileColors.CardBackground, shadowElevation = 4.dp
                ) {
                    Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(78.dp).clip(CircleShape).background(ProfileColors.PrimaryLight), contentAlignment = Alignment.Center) {
                            if (pessoa.fotoPerfilPath.isNotBlank() && File(pessoa.fotoPerfilPath).exists()) {
                                AsyncImage(File(pessoa.fotoPerfilPath), pessoa.nomeUsuario, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            } else Icon(Icons.Outlined.Person, null, Modifier.size(38.dp), tint = ProfileColors.PrimaryDark)
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(pessoa.nomeUsuario, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                            Text(if (profissional) stringResource(R.string.verified_professional) else stringResource(R.string.lumis_client), color = ProfileColors.PrimaryDark, fontWeight = FontWeight.SemiBold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.LocationOn, null, Modifier.size(16.dp), tint = ProfileColors.Primary)
                                Spacer(Modifier.width(4.dp))
                                Text(pessoa.localizacao.ifBlank { stringResource(R.string.location_not_provided) }, color = ProfileColors.TextSecondary)
                            }
                        }
                    }
                }
            }
        }
        item {
            Column(Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (profissional) {
                        ResumoPublico(Icons.Filled.Star, mediaAvaliacoes?.let { "%.1f".format(it) } ?: "—", stringResource(R.string.ratings_count, quantidadeAvaliacoes), Modifier.weight(1f))
                    }
                    ResumoPublico(Icons.Outlined.TaskAlt, totalConcluidos.toString(), stringResource(R.string.services_completed_count), Modifier.weight(1f))
                }
                if (pessoa.biografia.isNotBlank()) InformacaoPublica(stringResource(R.string.about), pessoa.biografia)
                if (profissional) {
                    InformacaoPublica(stringResource(R.string.specialties), pessoa.especialidades.ifBlank { stringResource(R.string.not_provided_plural) })
                    InformacaoPublica(stringResource(R.string.experience), pessoa.experiencia.ifBlank { stringResource(R.string.not_provided_singular) })
                    InformacaoPublica(stringResource(R.string.service_region), pessoa.regiaoAtuacao.ifBlank { pessoa.localizacao })
                    if (pessoa.valorMinimoCentavos > 0) {
                        InformacaoPublica(stringResource(R.string.minimum_price), stringResource(R.string.price_per_service, "%.2f".format(pessoa.valorMinimoCentavos / 100.0).replace('.', ',')))
                    }
                    onContratar?.let { contratar ->
                        Button(
                            onClick = contratar, Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = ProfileColors.Primary)
                        ) {
                            Icon(Icons.Outlined.CleaningServices, null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(R.string.hire_professional), fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Text(
                        stringResource(R.string.public_profile_privacy),
                        color = ProfileColors.TextSecondary, style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun ResumoPublico(icone: androidx.compose.ui.graphics.vector.ImageVector, valor: String, legenda: String, modifier: Modifier) {
    Surface(modifier, shape = RoundedCornerShape(18.dp), color = ProfileColors.CardBackground, border = BorderStroke(1.dp, ProfileColors.Divider)) {
        Column(Modifier.padding(15.dp)) {
            Icon(icone, null, tint = ProfileColors.Primary)
            Spacer(Modifier.height(6.dp))
            Text(valor, fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
            Text(legenda, color = ProfileColors.TextSecondary, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun InformacaoPublica(titulo: String, conteudo: String) {
    Surface(shape = RoundedCornerShape(18.dp), color = ProfileColors.CardBackground, border = BorderStroke(1.dp, ProfileColors.Divider)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(titulo, fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
            Spacer(Modifier.height(5.dp))
            Text(conteudo, color = ProfileColors.TextSecondary)
        }
    }
}
