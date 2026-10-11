package com.example.lumis.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.lumis.ui.components.*
import com.example.lumis.ui.screens.profile.BottomNavBar
import com.example.lumis.ui.screens.profile.NavItem
import com.example.lumis.ui.theme.*

// ======== DATA CLASSES ========
data class CleaningType(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val priceFrom: Boolean = true,
    val icon: ImageVector
)

data class PlanInfo(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val period: String,
    val features: List<String>,
    val isPopular: Boolean = false
)

// ======== MOCK DATA - SERVIÇOS ========
private val cleaningTypes = listOf(
    CleaningType(
        id = "padrao",
        title = "Limpeza Padrão",
        description = "Limpeza completa da rotina para sua casa sempre organizada, com foco na higienização geral.",
        price = "R\$ 120,00",
        icon = Icons.Outlined.Home
    ),
    CleaningType(
        id = "pesada",
        title = "Limpeza Pesada",
        description = "Limpeza profunda com foco em gorduras difíceis, reentrâncias e remoção de acúmulos.",
        price = "R\$ 220,00",
        icon = Icons.Outlined.Build
    ),
    CleaningType(
        id = "pos_mudanca",
        title = "Limpeza Pós-Mudança",
        description = "Ideal para preparar o imóvel antes de entrar ou devolver o imóvel após a mudança.",
        price = "R\$ 350,00",
        icon = Icons.Outlined.Inventory
    ),
    CleaningType(
        id = "airbnb",
        title = "Limpeza Airbnb / Temporada",
        description = "Preparação expressa e rigorosa entre reservas com o padrão de limpeza hoteleira.",
        price = "R\$ 180,00",
        icon = Icons.Outlined.Hotel
    )
)

// ======== MOCK DATA - PLANOS ========
private val plans = listOf(
    PlanInfo(
        id = "basico",
        title = "Básico",
        description = "Perfeito para quem busca praticidade com economia",
        price = "R\$ 99,90",
        period = "/mês",
        features = listOf(
            "2 limpezas/mês (até 4 cômodos)",
            "1 serviço especializado por trimestre",
            "Atendimento em até 3 dias úteis",
            "Dicas mensais de organização"
        )
    ),
    PlanInfo(
        id = "regular",
        title = "Regular",
        description = "Equilíbrio perfeito — nosso plano mais escolhido",
        price = "R\$ 179,90",
        period = "/mês",
        features = listOf(
            "3 limpezas/mês (até 4 cômodos)",
            "2 serviços especializados por trimestre",
            "Atendimento em até 2 dias úteis",
            "Relatórios de impacto ambiental",
            "Suporte prioritário por WhatsApp"
        ),
        isPopular = true
    ),
    PlanInfo(
        id = "premium",
        title = "Premium",
        description = "Conforto total e suporte prioritário",
        price = "R\$ 399,90",
        period = "/mês",
        features = listOf(
            "Limpeza semanal dedicada",
            "Todos os serviços especializados",
            "Atendimento em até 24h",
            "Equipe fixa ou exclusiva"
        )
    )
)

// ======== TELA PRINCIPAL (COM TABS) ========
@Composable
fun ServicesScreen(
    onNavigateToProfile: () -> Unit = {},
    onNavigateToExplorar: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(LumisTab.SERVICOS) }
    var selectedTypeId by remember { mutableStateOf(cleaningTypes[1].id) }
    var selectedPlanId by remember { mutableStateOf(plans[1].id) }

    val context = LocalContext.current

    val view = LocalView.current
    LaunchedEffect(Unit) {
        if (!view.isInEditMode) {
            val window = (view.context as? android.app.Activity)?.window ?: return@LaunchedEffect
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    Scaffold(
        containerColor = LumisBackground,
        topBar = {
            Column(Modifier.background(LumisSurface).statusBarsPadding()) {
                LumisTopBar()
                Spacer(Modifier.height(8.dp))
                LumisTabSelector(selected = selectedTab, onSelected = { selectedTab = it })
                Spacer(Modifier.height(4.dp))
            }
        },
        bottomBar = {
            Column {
                LumisBottomBar(
                    buttonText = if (selectedTab == LumisTab.SERVICOS) "Continuar" else "Assinar plano",
                    onClick = {
                        if (selectedTab == LumisTab.SERVICOS) {
                            val selected = cleaningTypes.first { it.id == selectedTypeId }
                            Toast.makeText(
                                context,
                                "Selecionado: ${selected.title} — ${selected.price}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val selected = plans.first { it.id == selectedPlanId }
                            Toast.makeText(
                                context,
                                "Plano ${selected.title} — ${selected.price}${selected.period}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
                BottomNavBar(
                    selected = NavItem.EXPLORAR,
                    onSelect = { item ->
                        when (item) {
                            NavItem.EXPLORAR -> onNavigateToExplorar()
                            NavItem.PERFIL -> onNavigateToProfile()
                            else -> {}
                        }
                    }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            LumisTab.SERVICOS -> ServicosTab(
                modifier = Modifier.padding(padding),
                selectedTypeId = selectedTypeId,
                onTypeSelected = { selectedTypeId = it }
            )
            LumisTab.PLANOS -> PlanosTab(
                modifier = Modifier.padding(padding),
                selectedPlanId = selectedPlanId,
                onPlanSelected = { selectedPlanId = it }
            )
        }
    }
}

// ======== ABA SERVIÇOS ========
@Composable
private fun ServicosTab(
    modifier: Modifier = Modifier,
    selectedTypeId: String,
    onTypeSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp)
    ) {
        item {
            LumisScreenHeader(
                label = "Categoria",
                title = "Qual tipo de limpeza\nvocê precisa?"
            )
        }
        items(cleaningTypes) { type ->
            CleaningTypeCard(
                type = type,
                isSelected = type.id == selectedTypeId,
                onClick = { onTypeSelected(type.id) }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CleaningTypeCard(
    type: CleaningType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    LumisCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        isSelected = isSelected,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone circular colorido
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(LumisGreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = type.icon,
                    contentDescription = null,
                    tint = LumisGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(type.title, style = MaterialTheme.typography.titleMedium, color = LumisTextPrimary)
                Spacer(Modifier.height(4.dp))
                Text(
                    type.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LumisTextSecondary
                )
            }
            Spacer(Modifier.width(12.dp))
            SelectionIndicator(isSelected = isSelected)
        }
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                type.price,
                style = MaterialTheme.typography.titleMedium,
                color = LumisGreen,
                fontWeight = FontWeight.Bold
            )
            if (type.priceFrom) {
                Spacer(Modifier.width(4.dp))
                Text(
                    "a partir de",
                    style = MaterialTheme.typography.bodySmall,
                    color = LumisTextTertiary
                )
            }
        }
    }
}

@Composable
private fun SelectionIndicator(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(if (isSelected) LumisGreen else LumisSurface)
            .border(
                width = if (isSelected) 0.dp else 1.5.dp,
                color = LumisBorder,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

// ======== ABA PLANOS ========
@Composable
private fun PlanosTab(
    modifier: Modifier = Modifier,
    selectedPlanId: String,
    onPlanSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp)
    ) {
        item {
            LumisScreenHeader(
                label = "Planos",
                title = "Escolha o plano ideal\npara você"
            )
        }
        items(plans) { plan ->
            PlanCard(
                plan = plan,
                isSelected = plan.id == selectedPlanId,
                onClick = { onPlanSelected(plan.id) }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun PlanCard(
    plan: PlanInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    LumisCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        isSelected = isSelected,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        plan.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = LumisTextPrimary
                    )
                    if (plan.isPopular) {
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(LumisGreen)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "MAIS POPULAR",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    plan.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LumisTextSecondary
                )
            }
            Spacer(Modifier.width(12.dp))
            SelectionIndicator(isSelected = isSelected)
        }

        Spacer(Modifier.height(12.dp))

        // Preço
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                plan.price,
                style = MaterialTheme.typography.headlineSmall,
                color = LumisGreen,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(4.dp))
            Text(
                plan.period,
                style = MaterialTheme.typography.bodyMedium,
                color = LumisTextSecondary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        // Features
        plan.features.forEach { feature ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 3.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = LumisGreen,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    feature,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LumisTextPrimary
                )
            }
        }
    }
}
