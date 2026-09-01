package com.example.lumis.ui.screens.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.R
import kotlin.math.max

// ---------------------------------------------------------------------------------------------
// CORES E TOKENS DE DESIGN (baseado no Figma "Lumis 2.0")
// ---------------------------------------------------------------------------------------------

object ProfileColors {
    val Background = Color(0xFFF5F6FA)
    val CardBackground = Color(0xFFFFFFFF)
    val Primary = Color(0xFF3FBF8F)
    val PrimaryDark = Color(0xFF2FA57A)
    val PrimaryLight = Color(0xFFDFF6EC)

    val HeaderBlueTop = Color(0xFFBFE0F5)
    val HeaderBlueBottom = Color(0xFFEAF5FB)

    val TextPrimary = Color(0xFF1E1E1E)
    val TextSecondary = Color(0xFF8A8A8E)
    val Divider = Color(0xFFEDEDF2)

    val ChipRedBg = Color(0xFFFBDCDC)
    val ChipLavenderBg = Color(0xFFE6E1FA)
    val ChipLavenderIcon = Color(0xFFC65B7A)
    val ChipGreenBg = Color(0xFFD9F3E4)
    val ChipGreenIcon = Color(0xFF2E86C1)

    val BadgeRedBg = Color(0xFFF9C8CE)
    val BadgeRedText = Color(0xFFB5495F)
    val BadgeYellowBg = Color(0xFFFCEFC3)
    val BadgeYellowText = Color(0xFFB98A1E)
    val BadgeGreenBg = Color(0xFFCDF3E2)
    val BadgeGreenText = Color(0xFF1F9E6E)
}

private val CardShape = RoundedCornerShape(20.dp)
private val PillShape = RoundedCornerShape(50)

// ---------------------------------------------------------------------------------------------
// MODELO DE DADOS (MOCK)
// ---------------------------------------------------------------------------------------------

data class FuncionarioProfile(
    val nome: String = "Ana Beatriz Santos",
    val localizacao: String = "São Paulo, Brasil",
    val telefone: String = "11 0800-21123",
    val avaliacao: String = "4.8 estrelas",
    val horas: String = "156 horas",
    val meses: String = "12 meses",
    val taxaConclusao: String = "98%",
    val pontualidade: String = "94%",
    val desempenhoSelecionado: NivelDesempenho = NivelDesempenho.MUITO_BOM,
    val avaliacoesPorAno: List<Float> = listOf(0.35f, 0.55f, 0.3f, 0.65f, 0.5f, 0.85f, 0.4f, 0.7f, 0.6f, 0.45f, 0.8f, 0.55f),
    val avaliacoesPorMes: List<Float> = listOf(0.6f, 0.85f, 0.45f, 0.7f, 0.9f, 0.5f, 0.75f, 0.65f),
    // Dados hipotético do ano
    val anoGanhos: String = "R$ 18.720,00",
    val anoPendentes: String = "R$ 1.240,00",
    val anoUltimoPagamento: String = "R$ 1.560,00",
    val anoHorasExtras: String = "84h",
    val anoBonus: String = "R$ 2.300,00",
    // Dados hipotético do mês
    val mesGanhos: String = "R$ 1.560,00",
    val mesPendentes: String = "R$ 320,00",
    val mesUltimoPagamento: String = "R$ 1.240,00",
    val mesHorasExtras: String = "7h",
    val mesBonus: String = "R$ 190,00",
    val valorPorServico: String = "R$ 150,00"
)

enum class NivelDesempenho(val label: String) {
    RUIM("Ruim"),
    REGULAR("Regular"),
    MUITO_BOM("Muito Bom")
}

enum class PeriodoServico(val label: String) {
    ANO("ano"),
    MES("mes")
}

enum class NavItem(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    HOME("Home", Icons.Outlined.Home),
    CIDADE("Cidade", Icons.Outlined.LocationCity),
    EXPLORAR("Explorar", Icons.Outlined.Search),
    AGENDA("Agenda", Icons.Outlined.CalendarMonth),
    PERFIL("Perfil", Icons.Outlined.Person)
}

// Calcula o nível de desempenho a partir dos dados do funcionário
fun calcularDesempenho(profile: FuncionarioProfile): NivelDesempenho {
    val conclusao = profile.taxaConclusao.replace("%", "").replace(",", ".").toFloatOrNull() ?: 0f
    val pontualidade = profile.pontualidade.replace("%", "").replace(",", ".").toFloatOrNull() ?: 0f
    val avaliacao = profile.avaliacao.filter { it.isDigit() || it == '.' }.toFloatOrNull() ?: 0f
    val score = conclusao * 0.5f + pontualidade * 0.3f + (avaliacao / 5f) * 100f * 0.2f
    return when {
        score >= 80f -> NivelDesempenho.MUITO_BOM
        score >= 55f -> NivelDesempenho.REGULAR
        else -> NivelDesempenho.RUIM
    }
}

// ---------------------------------------------------------------------------------------------
// TELA PRINCIPAL
// ---------------------------------------------------------------------------------------------

@Composable
fun FuncionarioProfileScreen(
    onNavigateToServicos: () -> Unit = {},
    profile: FuncionarioProfile = FuncionarioProfile()
) {
    var periodoSelecionado by remember { mutableStateOf(PeriodoServico.ANO) }
    var desempenhoSelecionado by remember { mutableStateOf(calcularDesempenho(profile)) }
    var navSelecionado by remember { mutableStateOf(NavItem.EXPLORAR) }

    Scaffold(
        containerColor = ProfileColors.Background,
        bottomBar = {
            BottomNavBar(
                selected = navSelecionado,
                onSelect = { item ->
                    navSelecionado = item
                    if (item == NavItem.EXPLORAR) {
                        onNavigateToServicos()
                    }
                }
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isWideScreen = maxWidth > 600.dp
            val contentMaxWidth = if (isWideScreen) 460.dp else maxWidth
            val horizontalPadding = if (isWideScreen) (maxWidth - contentMaxWidth) / 2 else 0.dp

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    ProfileHeaderSection(
                        profile = profile,
                        horizontalPadding = horizontalPadding
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ProfileColors.CardBackground)
                            .padding(horizontal = horizontalPadding + 20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        ServiceToggleSection(
                            selecionado = periodoSelecionado,
                            onSelect = { periodoSelecionado = it }
                        )

                        MetricsRow(profile)

                        PerformanceSection(
                            selecionado = desempenhoSelecionado,
                            onSelect = { desempenhoSelecionado = it }
                        )

                        val chartData = when (periodoSelecionado) {
                            PeriodoServico.ANO -> profile.avaliacoesPorAno
                            PeriodoServico.MES -> profile.avaliacoesPorMes
                        }

                        AnimatedContent(
                            targetState = periodoSelecionado,
                            transitionSpec = {
                                (fadeIn(tween(300)) + androidx.compose.animation.slideInVertically(
                                    initialOffsetY = { it / 4 },
                                    animationSpec = tween(300)
                                )).togetherWith(
                                    fadeOut(tween(200)) + androidx.compose.animation.slideOutVertically(
                                        targetOffsetY = { -it / 4 },
                                        animationSpec = tween(200)
                                    )
                                )
                            },
                            label = "periodo"
                        ) { periodo ->
                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                val data = when (periodo) {
                                    PeriodoServico.ANO -> profile.avaliacoesPorAno
                                    PeriodoServico.MES -> profile.avaliacoesPorMes
                                }
                                RatingsChartCard(data)
                                PaymentsCard(profile, periodo)
                            }
                        }

                        ActionButtons(
                            onCalendarioClick = { },
                            onAvaliacoesClick = { }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// 1. HEADER (fundo azul + container branco com cantos superiores arredondados)
// ---------------------------------------------------------------------------------------------

@Composable
fun ProfileHeaderSection(
    profile: FuncionarioProfile,
    horizontalPadding: androidx.compose.ui.unit.Dp
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        // Fundo azul gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(ProfileColors.HeaderBlueTop, ProfileColors.HeaderBlueBottom)
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding + 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo Lumis (XML)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ProfileColors.Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.lumis),
                        contentDescription = "Logo Lumis",
                        tint = Color.White,
                        modifier = Modifier
                            .size(26.dp)
                            .graphicsLayer(scaleX = 0.9f, scaleY = 0.9f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.7f))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Configurações",
                        tint = ProfileColors.TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Container branco com cantos superiores arredondados
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp)
                .background(
                    color = ProfileColors.CardBackground,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
        ) {
            // Avatar + Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = horizontalPadding + 20.dp, end = horizontalPadding + 20.dp, top = 28.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar circular sobrepondo a transição azul/branco
                Box(
                    modifier = Modifier
                        .offset(y = (-34).dp)
                        .size(82.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(3.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(74.dp)
                            .clip(CircleShape)
                            .background(ProfileColors.Divider),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.lumis),
                            contentDescription = "Foto do funcionário",
                            tint = ProfileColors.Primary,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Informações de identificação (alinhadas ao centro do avatar)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = (-34).dp)
                ) {
                    Text(
                        text = profile.nome,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = ProfileColors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoRow(icon = Icons.Outlined.LocationOn, text = profile.localizacao)
                    Spacer(modifier = Modifier.height(2.dp))
                    InfoRow(icon = Icons.Outlined.Call, text = profile.telefone)
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Botão editar (alinhado ao centro do avatar)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .offset(y = (-34).dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = ProfileColors.Divider,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Editar",
                        tint = ProfileColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Editar",
                        fontSize = 10.sp,
                        color = ProfileColors.Primary
                    )
                }
            }

            // Três métricas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding + 20.dp)
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatChip(
                    icon = Icons.Filled.Star,
                    iconTint = Color(0xFFF5B942),
                    bgColor = ProfileColors.ChipRedBg,
                    text = profile.avaliacao,
                    modifier = Modifier.weight(1f)
                )
                StatChip(
                    icon = Icons.Outlined.HourglassBottom,
                    iconTint = ProfileColors.ChipLavenderIcon,
                    bgColor = ProfileColors.ChipLavenderBg,
                    text = profile.horas,
                    modifier = Modifier.weight(1f)
                )
                StatChip(
                    icon = Icons.Outlined.CalendarMonth,
                    iconTint = ProfileColors.ChipGreenIcon,
                    bgColor = ProfileColors.ChipGreenBg,
                    text = profile.meses,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ProfileColors.TextSecondary,
            modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = ProfileColors.TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    bgColor: Color,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(PillShape)
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = ProfileColors.TextPrimary,
            maxLines = 1
        )
    }
}

// ---------------------------------------------------------------------------------------------
// 2. SERVIÇOS REALIZADOS (TOGGLE ano / mês)
// ---------------------------------------------------------------------------------------------

@Composable
fun ServiceToggleSection(
    selecionado: PeriodoServico,
    onSelect: (PeriodoServico) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Serviços realizados",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = ProfileColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(PillShape)
                .background(ProfileColors.Background)
                .padding(4.dp)
        ) {
            PeriodoServico.values().forEach { periodo ->
                ToggleOption(
                    text = periodo.label,
                    selected = periodo == selecionado,
                    onClick = { onSelect(periodo) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ToggleOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) ProfileColors.Primary else Color.Transparent
    val textColor = if (selected) Color.White else ProfileColors.TextSecondary

    Box(
        modifier = modifier
            .clip(PillShape)
            .background(bg)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

// ---------------------------------------------------------------------------------------------
// 3. MÉTRICAS (dois cards separados lado a lado)
// ---------------------------------------------------------------------------------------------

@Composable
fun MetricsRow(profile: FuncionarioProfile) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            label = "taxa de conclusão",
            value = profile.taxaConclusao,
            modifier = Modifier.weight(1f)
        )
        MetricCard(
            label = "pontualidade",
            value = profile.pontualidade,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun MetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = ProfileColors.Background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = ProfileColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = ProfileColors.TextPrimary
            )
        }
    }
}

// ---------------------------------------------------------------------------------------------
// 4. DESEMPENHO (BADGES)
// ---------------------------------------------------------------------------------------------

@Composable
fun PerformanceSection(
    selecionado: NivelDesempenho,
    onSelect: (NivelDesempenho) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Desempenho",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = ProfileColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NivelDesempenho.values().forEach { nivel ->
                val (bg, textColor) = when (nivel) {
                    NivelDesempenho.RUIM -> ProfileColors.BadgeRedBg to ProfileColors.BadgeRedText
                    NivelDesempenho.REGULAR -> ProfileColors.BadgeYellowBg to ProfileColors.BadgeYellowText
                    NivelDesempenho.MUITO_BOM -> ProfileColors.BadgeGreenBg to ProfileColors.BadgeGreenText
                }
                val isSelected = nivel == selecionado
                val borderWidth by animateDpAsState(
                    targetValue = if (isSelected) 2.dp else 0.dp,
                    animationSpec = tween(250),
                    label = "border"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .graphicsLayer {
                            alpha = if (isSelected) 1f else 0.85f
                        }
                        .clip(RoundedCornerShape(14.dp))
                        .background(bg)
                        .then(
                            if (borderWidth > 0.dp) Modifier.border(
                                width = borderWidth,
                                color = textColor,
                                shape = RoundedCornerShape(14.dp)
                            ) else Modifier
                        )
                        .clickable { onSelect(nivel) }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nivel.label,
                        color = textColor,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// 5. GRÁFICO DE AVALIAÇÕES
// ---------------------------------------------------------------------------------------------

@Composable
fun RatingsChartCard(valores: List<Float>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = ProfileColors.Background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "avaliações ao longo do tempo",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = ProfileColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))
            BarChart(
                values = valores,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
    }
}

@Composable
private fun BarChart(values: List<Float>, modifier: Modifier = Modifier) {
    val barColor = ProfileColors.Primary
    Canvas(modifier = modifier) {
        if (values.isEmpty()) return@Canvas

        val maxValue = max(values.max(), 0.01f)
        val spacing = 10.dp.toPx()
        val barCount = values.size
        val barWidth = (size.width - spacing * (barCount - 1)) / barCount
        val cornerRadius = 6.dp.toPx()

        values.forEachIndexed { index, value ->
            val barHeight = size.height * (value / maxValue)
            val left = index * (barWidth + spacing)
            val top = size.height - barHeight

            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, top),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}

// ---------------------------------------------------------------------------------------------
// 6. PAGAMENTOS
// ---------------------------------------------------------------------------------------------

@Composable
fun PaymentsCard(profile: FuncionarioProfile, periodo: PeriodoServico) {
    val isAno = periodo == PeriodoServico.ANO

    val ganhos = if (isAno) profile.anoGanhos else profile.mesGanhos
    val pendentes = if (isAno) profile.anoPendentes else profile.mesPendentes
    val ultimo = if (isAno) profile.anoUltimoPagamento else profile.mesUltimoPagamento
    val horasExtras = if (isAno) profile.anoHorasExtras else profile.mesHorasExtras
    val bonus = if (isAno) profile.anoBonus else profile.mesBonus

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = ProfileColors.Background),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pagamentos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = ProfileColors.TextPrimary
                )
                Text(
                    text = if (isAno) "No ano" else "No mês",
                    fontSize = 12.sp,
                    color = ProfileColors.Primary,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            val itens = listOf(
                "Ganhos no mês" to ganhos,
                "Ganhos pendentes" to pendentes,
                "Último pagamento" to ultimo,
                "Valor por serviço" to profile.valorPorServico,
                "Horas extras" to horasExtras,
                "Bônus" to bonus
            )

            itens.forEach { (label, valor) ->
                PaymentRow(label = label, valor = valor)
            }
        }
    }
}

@Composable
private fun PaymentRow(label: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = ProfileColors.TextSecondary
        )
        Text(
            text = valor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = ProfileColors.TextPrimary
        )
    }
}

// ---------------------------------------------------------------------------------------------
// 7. BOTÕES DE AÇÃO
// ---------------------------------------------------------------------------------------------

@Composable
fun ActionButtons(
    onCalendarioClick: () -> Unit,
    onAvaliacoesClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = onCalendarioClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = ProfileColors.Primary,
                contentColor = Color.White
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(text = "Calendario", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        OutlinedButton(
            onClick = onAvaliacoesClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = PillShape,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = ProfileColors.Primary
            ),
            border = BorderStroke(1.dp, ProfileColors.Primary)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(text = "Verificar suas avaliações", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// 8. BOTTOM NAVIGATION (com círculo flutuante no item ativo)
// ---------------------------------------------------------------------------------------------

@Composable
fun BottomNavBar(
    selected: NavItem,
    onSelect: (NavItem) -> Unit
) {
    Surface(
        color = ProfileColors.CardBackground,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            NavItem.values().forEach { item ->
                val isSelected = item == selected
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onSelect(item) }
                        .padding(horizontal = 4.dp)
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-14).dp)
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(2.dp, ProfileColors.PrimaryLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(ProfileColors.PrimaryLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = ProfileColors.PrimaryDark,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ProfileColors.Primary,
                            modifier = Modifier.offset(y = (-10).dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(6.dp))
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = ProfileColors.TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            color = ProfileColors.TextSecondary
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// PREVIEWS
// ---------------------------------------------------------------------------------------------

@Preview(name = "Celular - Pixel 4", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun FuncionarioProfileScreenPreviewPhone() {
    MaterialTheme {
        FuncionarioProfileScreen()
    }
}

@Preview(name = "Celular pequeno", showBackground = true, widthDp = 320, heightDp = 780)
@Composable
fun FuncionarioProfileScreenPreviewSmallPhone() {
    MaterialTheme {
        FuncionarioProfileScreen()
    }
}

@Preview(name = "Tablet", showBackground = true, device = Devices.PIXEL_TABLET)
@Composable
fun FuncionarioProfileScreenPreviewTablet() {
    MaterialTheme {
        FuncionarioProfileScreen()
    }
}
