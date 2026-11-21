package com.example.lumis.view.screens.city

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Recycling
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lumis.view.components.LumisPageTitle
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisBorder
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisGreenDark
import com.example.lumis.view.theme.LumisGreenLight
import com.example.lumis.view.theme.LumisSurface
import com.example.lumis.view.theme.LumisTextPrimary
import com.example.lumis.view.theme.LumisTextSecondary
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ModoTema
import com.example.lumis.R

private enum class PainelCidade { ECO_PONTOS, GUIA }

private data class Ecoponto(
    val nome: String,
    val endereco: String,
    val distancia: String,
    val horario: String,
    val materiais: String
)

private val ecopontos = listOf(
    Ecoponto("Ecoponto Praça Verde", "Av. das Palmeiras, 240", "650 m", "Aberto até 18h", "Papel, plástico, vidro e metal"),
    Ecoponto("Coleta Comunitária Centro", "Rua XV de Novembro, 88", "1,2 km", "Aberto até 17h", "Eletrônicos, pilhas e recicláveis"),
    Ecoponto("Ponto Limpo Municipal", "Rua das Acácias, 510", "2,4 km", "Abre amanhã às 8h", "Móveis, entulho leve e recicláveis")
)

@Composable
fun CidadeConscienteScreen(
    modifier: Modifier = Modifier,
    localizacao: String = "",
    onNotificationsClick: () -> Unit = {}
) {
    var painel by rememberSaveable { mutableStateOf(PainelCidade.ECO_PONTOS) }
    var pontoSelecionado by remember { mutableStateOf<Ecoponto?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(LumisBackground)
    ) {
        LumisTopBar(onNotificationsClick = onNotificationsClick)

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LumisPageTitle(
                    eyebrow = stringResource(R.string.conscious_city).uppercase(),
                    title = stringResource(R.string.city_care_title),
                    subtitle = stringResource(R.string.city_care_subtitle)
                )
            }
            item { ImpactoHeroCard(localizacao) }
            item {
                AcoesCidade(
                    painel = painel,
                    onEcopontos = { painel = PainelCidade.ECO_PONTOS },
                    onDados = { painel = PainelCidade.GUIA }
                )
            }
            item {
                AnimatedContent(
                    targetState = painel,
                    transitionSpec = { fadeIn(tween(180)).togetherWith(fadeOut(tween(120))) },
                    label = "painelCidade"
                ) { destino ->
                    when (destino) {
                        PainelCidade.ECO_PONTOS -> EcopontosCard(onSelect = { pontoSelecionado = it })
                        PainelCidade.GUIA -> DadosAmbientaisCard()
                    }
                }
            }
            item { DicaSustentavelCard() }
        }
    }

    pontoSelecionado?.let { ponto ->
        EcopontoDialog(ponto = ponto, onDismiss = { pontoSelecionado = null })
    }
}

@Composable
private fun ImpactoHeroCard(localizacao: String) {
    Surface(shape = RoundedCornerShape(24.dp), color = Color.Transparent) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(LumisGreenDark, LumisGreen, Color(0xFF75D7A4))))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Eco, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.size(12.dp))
                Column {
                    Text(stringResource(R.string.sustainable_guide), color = Color.White.copy(alpha = 0.82f), style = MaterialTheme.typography.labelLarge)
                    Text(
                        if (localizacao.isBlank()) stringResource(R.string.cleaner_neighborhood) else localizacao,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.city_hero_explanation), color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ImpactoMetrica("1", stringResource(R.string.separate), Modifier.weight(1f))
                ImpactoMetrica("2", stringResource(R.string.clean), Modifier.weight(1f))
                ImpactoMetrica("3", stringResource(R.string.deliver), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ImpactoMetrica(valor: String, legenda: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.14f))
            .padding(horizontal = 10.dp, vertical = 12.dp)
    ) {
        Text(valor, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        Text(legenda, color = Color.White.copy(alpha = 0.78f), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun AcoesCidade(
    painel: PainelCidade,
    onEcopontos: () -> Unit,
    onDados: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(stringResource(R.string.what_do_you_need), style = MaterialTheme.typography.titleMedium, color = LumisTextPrimary)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AcaoCidadeButton(Icons.Outlined.LocationOn, stringResource(R.string.ecopoints), painel == PainelCidade.ECO_PONTOS, onEcopontos, Modifier.weight(1f))
            AcaoCidadeButton(Icons.Outlined.Eco, stringResource(R.string.disposal_guide), painel == PainelCidade.GUIA, onDados, Modifier.weight(1f))
        }
    }
}

@Composable
private fun AcaoCidadeButton(
    icon: ImageVector,
    label: String,
    selecionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(18.dp),
        color = if (selecionado) LumisGreenLight else LumisSurface,
        border = BorderStroke(1.dp, if (selecionado) LumisGreen else LumisBorder)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = if (selecionado) LumisGreenDark else LumisTextSecondary)
            Spacer(Modifier.height(7.dp))
            Text(label, color = LumisTextPrimary, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun EcopontosCard(onSelect: (Ecoponto) -> Unit) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = LumisSurface,
        border = BorderStroke(1.dp, LumisBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.nearby_ecopoints), style = MaterialTheme.typography.titleMedium, color = LumisTextPrimary)
                    Text(stringResource(R.string.sorted_by_location), style = MaterialTheme.typography.bodySmall, color = LumisTextSecondary)
                }
                Surface(shape = CircleShape, color = LumisGreenLight) {
                    Icon(Icons.Outlined.LocationOn, null, Modifier.padding(9.dp).size(20.dp), LumisGreenDark)
                }
            }
            Spacer(Modifier.height(8.dp))
            ecopontos.forEachIndexed { index, ponto ->
                EcopontoRow(ponto, onClick = { onSelect(ponto) })
                if (index != ecopontos.lastIndex) Box(Modifier.fillMaxWidth().height(1.dp).background(LumisBorder))
            }
        }
    }
}

@Composable
private fun EcopontoRow(ponto: Ecoponto, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(LumisGreenLight),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Outlined.Recycling, contentDescription = null, tint = LumisGreenDark) }
        Spacer(Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(ponto.nome, color = LumisTextPrimary, style = MaterialTheme.typography.titleSmall)
            Text(
                "${ponto.endereco} • ${ponto.distancia}",
                color = LumisTextSecondary,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(ponto.horario, color = LumisGreenDark, style = MaterialTheme.typography.labelSmall)
        }
        Icon(Icons.Outlined.ChevronRight, contentDescription = stringResource(R.string.view_details), tint = LumisTextSecondary)
    }
}

@Composable
private fun EcopontoDialog(ponto: Ecoponto, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Outlined.Recycling, contentDescription = null, tint = LumisGreen) },
        title = { Text(ponto.nome) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(ponto.endereco, color = LumisTextPrimary)
                Text("${ponto.distancia} • ${ponto.horario}", color = LumisGreenDark)
                Text(stringResource(R.string.accepts_materials, ponto.materiais), color = LumisTextSecondary)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.got_it)) } }
    )
}

@Composable
private fun DadosAmbientaisCard() {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = LumisSurface,
        border = BorderStroke(1.dp, LumisBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text(stringResource(R.string.disposal_guide), style = MaterialTheme.typography.titleMedium, color = LumisTextPrimary)
            GuiaDescarte(stringResource(R.string.paper_and_cardboard), stringResource(R.string.paper_disposal_tip))
            GuiaDescarte(stringResource(R.string.glass_and_metal), stringResource(R.string.glass_disposal_tip))
            GuiaDescarte(stringResource(R.string.electronics), stringResource(R.string.electronics_disposal_tip))
            Text(
                stringResource(R.string.ecopoints_demo_notice),
                style = MaterialTheme.typography.bodySmall,
                color = LumisTextSecondary
            )
        }
    }
}

@Composable
private fun GuiaDescarte(titulo: String, descricao: String) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(LumisGreenLight).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Recycling, null, tint = LumisGreenDark)
        Spacer(Modifier.size(12.dp))
        Column {
            Text(titulo, style = MaterialTheme.typography.titleSmall, color = LumisTextPrimary)
            Text(descricao, style = MaterialTheme.typography.bodySmall, color = LumisTextSecondary)
        }
    }
}

@Composable
private fun DicaSustentavelCard() {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(LumisGreenLight).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Eco, contentDescription = null, tint = LumisGreenDark)
        Spacer(Modifier.size(12.dp))
        Column {
            Text(stringResource(R.string.weekly_tip), color = LumisGreenDark, style = MaterialTheme.typography.labelLarge)
            Text(stringResource(R.string.weekly_tip_text), color = LumisTextPrimary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CidadeConscienteScreenPreview() {
    LumisTheme(modo = ModoTema.SISTEMA) { CidadeConscienteScreen() }
}
