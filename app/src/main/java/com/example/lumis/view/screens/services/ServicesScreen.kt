package com.example.lumis.view.screens.services

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.lumis.controller.ServicesController
import com.example.lumis.model.Servico
import com.example.lumis.model.ServicoRepository
import com.example.lumis.view.components.LumisCard
import com.example.lumis.view.components.LumisPageTitle
import com.example.lumis.view.components.LumisPrimaryButton
import com.example.lumis.view.components.LumisTab
import com.example.lumis.view.components.LumisTabSelector
import com.example.lumis.view.components.LumisTextField
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisBlue
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisGreenDark
import com.example.lumis.view.theme.LumisGreenLight
import com.example.lumis.view.theme.LumisTextPrimary
import com.example.lumis.view.theme.LumisTextSecondary

@Composable
fun ServicesScreen(
    onAbrirPlanos: () -> Unit = {},
    onAbrirAgendamento: (Servico) -> Unit = {},
    servicoInicial: String? = null,
    onNotificationsClick: () -> Unit = {}
) {
    val controller = remember { ServicesController() }
    var busca by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("Todos") }
    val categorias = listOf("Todos", "Rotina", "Pesada", "Mudança", "Temporada")

    LaunchedEffect(servicoInicial) {
        servicoInicial?.let { nome ->
            ServicoRepository.todos.firstOrNull { it.nome == nome }?.let(controller::selecionarServico)
        }
    }

    val servicosFiltrados = controller.servicos.filter { servico ->
        val correspondeBusca = busca.isBlank() ||
            servico.nome.contains(busca, ignoreCase = true) ||
            servico.descricao.contains(busca, ignoreCase = true)
        val correspondeCategoria = when (categoria) {
            "Rotina" -> servico.nome.contains("Padrão", true)
            "Pesada" -> servico.nome.contains("Pesada", true)
            "Mudança" -> servico.nome.contains("Mudança", true)
            "Temporada" -> servico.nome.contains("Airbnb", true)
            else -> true
        }
        correspondeBusca && correspondeCategoria
    }

    Surface(modifier = Modifier.fillMaxSize(), color = LumisBackground) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LumisBackground)
        ) {
            LumisTopBar(onNotificationsClick = onNotificationsClick)
            LumisTabSelector(
                selected = LumisTab.SERVICOS,
                onSelected = { if (it == LumisTab.PLANOS) onAbrirPlanos() },
                modifier = Modifier.padding(top = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    LumisPageTitle(
                        eyebrow = "SERVIÇOS LUMIS",
                        title = "Encontre a limpeza ideal",
                        subtitle = "Compare opções, escolha o melhor serviço e agende em poucos passos."
                    )
                }
                item {
                    LumisTextField(
                        value = busca,
                        onValueChange = { busca = it },
                        label = "Buscar serviço",
                        placeholder = "Tipo de limpeza ou necessidade",
                        leadingIcon = Icons.Outlined.Search,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categorias) { item ->
                            FilterChip(
                                selected = categoria == item,
                                onClick = { categoria = item },
                                label = { Text(item) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = LumisGreenLight,
                                    selectedLabelColor = LumisGreenDark
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = categoria == item,
                                    borderColor = MaterialTheme.colorScheme.outline,
                                    selectedBorderColor = LumisGreen
                                )
                            )
                        }
                    }
                }

                if (servicosFiltrados.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 42.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Nenhum serviço encontrado", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Tente outro termo ou remova os filtros.",
                                color = LumisTextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    items(servicosFiltrados, key = { it.nome }) { servico ->
                        CardServico(
                            servico = servico,
                            selecionado = controller.servicoSelecionado == servico.nome,
                            onClick = { controller.selecionarServico(servico) }
                        )
                    }
                }
            }

            val selecionado = controller.servicos.firstOrNull { it.nome == controller.servicoSelecionado }
            Surface(shadowElevation = 10.dp, color = MaterialTheme.colorScheme.surface) {
                LumisPrimaryButton(
                    text = "Continuar com ${selecionado?.nome ?: "o serviço"}",
                    onClick = { selecionado?.let(onAbrirAgendamento) },
                    enabled = selecionado != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                )
            }
        }
    }
}

@Composable
private fun CardServico(
    servico: Servico,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selecionado) 1f else 0.985f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 500f),
        label = "serviceCardScale"
    )
    val markerColor by animateColorAsState(
        targetValue = if (selecionado) LumisGreen else MaterialTheme.colorScheme.outline,
        label = "serviceMarkerColor"
    )

    LumisCard(
        isSelected = selecionado,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(LumisGreenLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = servico.nome.take(1),
                    color = LumisGreenDark,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = servico.nome,
                        color = LumisTextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (selecionado) LumisGreen else Color.Transparent)
                            .then(
                                if (!selecionado) Modifier.background(Color.Transparent) else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = if (selecionado) "Selecionado" else null,
                            tint = if (selecionado) Color(0xFF102018) else markerColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
                Text(
                    text = servico.descricao.replace("\n", " "),
                    color = LumisTextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = LumisTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(5.dp))
                    Text("4 horas", color = LumisTextSecondary, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "A partir de ${servico.preco}",
                        color = LumisBlue,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
        AnimatedVisibility(visible = selecionado) {
            Spacer(Modifier.height(2.dp))
        }
    }
}
