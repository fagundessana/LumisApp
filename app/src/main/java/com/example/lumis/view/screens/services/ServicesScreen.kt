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
import androidx.compose.ui.res.stringResource
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
import com.example.lumis.view.screens.home.model.ProfessionalUi
import com.example.lumis.R

@Composable
fun ServicesScreen(
    onAbrirPlanos: () -> Unit = {},
    onAbrirAgendamento: (Servico) -> Unit = {},
    servicoInicial: String? = null,
    profissionais: List<ProfessionalUi> = emptyList(),
    profissionalSelecionadoId: Int? = null,
    onSelecionarProfissional: (Int) -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    val controller = remember { ServicesController() }
    var busca by rememberSaveable { mutableStateOf("") }
    var categoria by rememberSaveable { mutableStateOf("Todos") }
    val categorias = listOf("Todos", "Rotina", "Pesada", "Mudança", "Temporada")
    val rotulosCategorias = mapOf(
        "Todos" to stringResource(R.string.category_all),
        "Rotina" to stringResource(R.string.category_routine),
        "Pesada" to stringResource(R.string.category_deep),
        "Mudança" to stringResource(R.string.category_moving),
        "Temporada" to stringResource(R.string.category_vacation)
    )
    val nomesServicos = mapOf(
        "Limpeza Padrão" to stringResource(R.string.service_standard),
        "Limpeza Pesada" to stringResource(R.string.service_deep),
        "Limpeza Pós-Mudança" to stringResource(R.string.service_moving),
        "Limpeza Airbnb / Temporada" to stringResource(R.string.service_airbnb)
    )
    val descricoesServicos = mapOf(
        "Limpeza Padrão" to stringResource(R.string.service_standard_description),
        "Limpeza Pesada" to stringResource(R.string.service_deep_description),
        "Limpeza Pós-Mudança" to stringResource(R.string.service_moving_description),
        "Limpeza Airbnb / Temporada" to stringResource(R.string.service_airbnb_description)
    )

    LaunchedEffect(servicoInicial) {
        servicoInicial?.let { nome ->
            ServicoRepository.todos.firstOrNull { it.nome == nome }?.let {
                controller.selecionarServico(it)
                busca = it.nome
                categoria = "Todos"
            }
        }
    }

    val servicosFiltrados = controller.servicos.filter { servico ->
        val nomeLocalizado = nomesServicos[servico.nome] ?: servico.nome
        val descricaoLocalizada = descricoesServicos[servico.nome] ?: servico.descricao
        val correspondeBusca = busca.isBlank() ||
            servico.nome.contains(busca, ignoreCase = true) ||
            servico.descricao.contains(busca, ignoreCase = true) ||
            nomeLocalizado.contains(busca, ignoreCase = true) ||
            descricaoLocalizada.contains(busca, ignoreCase = true)
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
                        eyebrow = stringResource(R.string.lumis_services).uppercase(),
                        title = stringResource(R.string.find_ideal_cleaning),
                        subtitle = stringResource(R.string.compare_services_hint)
                    )
                }
                if (profissionais.isNotEmpty()) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(stringResource(R.string.choose_professional), style = MaterialTheme.typography.titleMedium, color = LumisTextPrimary)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(profissionais, key = { it.id }) { profissional ->
                                    FilterChip(
                                        selected = profissional.id == profissionalSelecionadoId,
                                        onClick = { onSelecionarProfissional(profissional.id) },
                                        label = { Text(profissional.nome.substringBefore(' ')) },
                                        leadingIcon = if (profissional.id == profissionalSelecionadoId) {
                                            { Icon(Icons.Outlined.Check, null, Modifier.size(16.dp)) }
                                        } else null,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = LumisGreenLight,
                                            selectedLabelColor = LumisGreenDark
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    LumisTextField(
                        value = busca,
                        onValueChange = { busca = it },
                        label = stringResource(R.string.search_service),
                        placeholder = stringResource(R.string.search_service_hint),
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
                                label = { Text(rotulosCategorias[item] ?: item) },
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
                            Text(stringResource(R.string.no_service_found), style = MaterialTheme.typography.titleMedium)
                            Text(
                                stringResource(R.string.try_another_search),
                                color = LumisTextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    items(servicosFiltrados, key = { it.nome }) { servico ->
                        CardServico(
                            servico = servico,
                            nomeExibicao = nomesServicos[servico.nome] ?: servico.nome,
                            descricaoExibicao = descricoesServicos[servico.nome] ?: servico.descricao,
                            selecionado = controller.servicoSelecionado == servico.nome,
                            onClick = { controller.selecionarServico(servico) }
                        )
                    }
                }
            }

            val selecionado = controller.servicos.firstOrNull { it.nome == controller.servicoSelecionado }
            Surface(shadowElevation = 10.dp, color = MaterialTheme.colorScheme.surface) {
                LumisPrimaryButton(
                    text = stringResource(
                        R.string.continue_with_service,
                        selecionado?.let { nomesServicos[it.nome] ?: it.nome } ?: stringResource(R.string.the_service)
                    ),
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
    nomeExibicao: String,
    descricaoExibicao: String,
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
                    text = nomeExibicao.take(1),
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
                        text = nomeExibicao,
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
                            contentDescription = if (selecionado) stringResource(R.string.selected) else null,
                            tint = if (selecionado) Color(0xFF102018) else markerColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
                Text(
                    text = descricaoExibicao.replace("\n", " "),
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
                    Text(stringResource(R.string.four_hours), color = LumisTextSecondary, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.from_value, servico.preco),
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
