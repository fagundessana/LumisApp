package com.example.lumis.view.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.lumis.R
import com.example.lumis.view.components.BotaoContinuar
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.components.LumisPageTitle
import com.example.lumis.view.components.LumisTab
import com.example.lumis.view.components.LumisTabSelector
import com.example.lumis.view.theme.LumisBorder
import com.example.lumis.view.theme.LumisSurface
import com.example.lumis.view.theme.*

// ============================================================
// TELA DE PLANOS
// ============================================================

data class Plano(
    val id: String,
    val nome: String,
    val descricao: String,
    val preco: String,
    val valorCentavos: Long,
    val precoPeriodo: String,
    val caracteristicas: List<String>,
    val popular: Boolean = false
)

@Composable
fun PlanosScreen(
    onVoltarServicos: () -> Unit = {},
    onAssinarPlano: (Plano, String) -> Unit = { _, _ -> },
    onNotificationsClick: () -> Unit = {}
) {
    var sel by remember { mutableStateOf("regular") }
    var limpezasPersonalizadas by remember { mutableIntStateOf(3) }
    var mostrarPagamento by remember { mutableStateOf(false) }
    var formaPagamento by remember { mutableStateOf("Pix") }
    val valorPersonalizado = 12_990L + (limpezasPersonalizadas - 2) * 6_000L
    val lista = listOf(
        Plano(
            id = "basico",
            nome = stringResource(R.string.basic_plan),
            descricao = stringResource(R.string.basic_plan_description),
            preco = "R$ 99,90",
            valorCentavos = 9_990,
            precoPeriodo = "/mês",
            caracteristicas = listOf(
                stringResource(R.string.basic_feature_1),
                stringResource(R.string.basic_feature_2),
                stringResource(R.string.basic_feature_3),
                stringResource(R.string.basic_feature_4)
            )
        ),
        Plano(
            id = "regular",
            nome = stringResource(R.string.regular_plan),
            descricao = stringResource(R.string.regular_plan_description),
            preco = "R$ 179,90",
            valorCentavos = 17_990,
            precoPeriodo = "/mês",
            caracteristicas = listOf(
                stringResource(R.string.regular_feature_1),
                stringResource(R.string.regular_feature_2),
                stringResource(R.string.regular_feature_3),
                stringResource(R.string.regular_feature_4)
            ),
            popular = true
        ),
        Plano(
            id = "premium",
            nome = stringResource(R.string.premium_plan),
            descricao = stringResource(R.string.premium_plan_description),
            preco = "R$ 399,90",
            valorCentavos = 39_990,
            precoPeriodo = "/mês",
            caracteristicas = listOf(
                stringResource(R.string.premium_feature_1),
                stringResource(R.string.premium_feature_2),
                stringResource(R.string.premium_feature_3),
                stringResource(R.string.premium_feature_4),
                stringResource(R.string.premium_feature_5)
            )
        ),
        Plano(
            id = "personalizado",
            nome = stringResource(R.string.custom_plan),
            descricao = stringResource(R.string.custom_plan_description),
            preco = "R$ ${"%.2f".format(valorPersonalizado / 100.0).replace('.', ',')}",
            valorCentavos = valorPersonalizado,
            precoPeriodo = "/mês",
            caracteristicas = listOf(
                stringResource(R.string.custom_feature_1, limpezasPersonalizadas),
                stringResource(R.string.custom_feature_2),
                stringResource(R.string.custom_feature_3),
                stringResource(R.string.custom_feature_4)
            )
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LumisBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // App Bar / Topo
            LumisTopBar(onNotificationsClick = onNotificationsClick)

            LumisTabSelector(
                selected = LumisTab.PLANOS,
                onSelected = { if (it == LumisTab.SERVICOS) onVoltarServicos() },
                modifier = Modifier.padding(top = 8.dp)
            )

            // Conteúdo scrollável (lista de planos + faq)
            LazyColumn(
                modifier = Modifier.weight(1f),
                // Reserva espaço para que o último card e a FAQ possam subir totalmente
                // acima do CTA fixo, inclusive em telas menores.
                contentPadding = PaddingValues(bottom = 112.dp)
            ) {
                // Cabeçalho
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        LumisPageTitle(
                            eyebrow = stringResource(R.string.lumis_plans).uppercase(),
                            title = stringResource(R.string.choose_ideal_plan),
                            subtitle = stringResource(R.string.compare_plan_benefits)
                        )
                    }
                }

                // Cards dos planos
                items(lista) { plano ->
                    PlanCard(
                        plano = plano,
                        sel = sel == plano.id,
                        onClick = { sel = plano.id }
                    )
                }
                if (sel == "personalizado") {
                    item {
                        Column(Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                            Text(stringResource(R.string.cleanings_per_month_question), fontWeight = FontWeight.Bold, color = LumisTextPrimary)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                (2..4).forEach { quantidade ->
                                    androidx.compose.material3.FilterChip(
                                        selected = limpezasPersonalizadas == quantidade,
                                        onClick = { limpezasPersonalizadas = quantidade },
                                        label = { Text("$quantidade") }
                                    )
                                }
                            }
                        }
                    }
                }

                                // FAQ
                item {
                    Spacer(Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.faq).uppercase(),
                            color = LumisGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        FAQItem(
                            pergunta = stringResource(R.string.faq_products_question),
                            resposta = stringResource(R.string.faq_products_answer)
                        )
                        Spacer(Modifier.height(6.dp))
                        FAQItem(
                            pergunta = stringResource(R.string.faq_cancel_question),
                            resposta = stringResource(R.string.faq_cancel_answer)
                        )
                        Spacer(Modifier.height(6.dp))
                        FAQItem(
                            pergunta = stringResource(R.string.faq_home_question),
                            resposta = stringResource(R.string.faq_home_answer)
                        )
                    }
                }
            }

            // Botão CTA Principal
            CtaComprar(
                planoSelecionado = lista.find { it.id == sel } ?: lista[1],
                onAssinar = { mostrarPagamento = true }
            )
        }
    }

    if (mostrarPagamento) {
        AlertDialog(
            onDismissRequest = { mostrarPagamento = false },
            title = { Text(stringResource(R.string.how_to_pay)) },
            text = {
                Column {
                    listOf(
                        "Pix" to R.string.pix,
                        "Cartão de crédito" to R.string.credit_card,
                        "Cartão de débito" to R.string.debit_card
                    ).forEach { (opcao, rotulo) ->
                        Row(
                            Modifier.fillMaxWidth().clickable { formaPagamento = opcao }.padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = formaPagamento == opcao, onClick = { formaPagamento = opcao })
                            Text(stringResource(rotulo), color = LumisTextPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarPagamento = false
                    onAssinarPlano(lista.first { it.id == sel }, formaPagamento)
                }) { Text(stringResource(R.string.continue_label)) }
            },
            dismissButton = { TextButton(onClick = { mostrarPagamento = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }
}

// ============================================================
// CARD DE PLANO (clicável)
// ============================================================

@Composable
private fun PlanCard(
    plano: Plano,
    sel: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(LumisSurface)
            .border(
                width = if (sel) 2.dp else 1.dp,
                color = if (sel) LumisGreen else LumisBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
    ) {
        // Conteúdo interno com padding
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        plano.nome,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumisTextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        plano.descricao,
                        fontSize = 12.sp,
                        color = LumisTextSecondary
                    )
                }
                Spacer(Modifier.weight(1f))
                if (plano.popular) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(LumisGreenLight)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            stringResource(R.string.most_popular),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumisGreen
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Preço
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    plano.preco,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = LumisGreen
                )
                Text(
                    plano.precoPeriodo,
                    fontSize = 12.sp,
                    color = LumisTextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            Column {
                plano.caracteristicas.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = LumisGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(feature, fontSize = 13.sp, color = LumisTextPrimary)
                    }
                }
            }
        }
    }
}

// ============================================================
// FAQ (expandível)
// ============================================================

@Composable
private fun FAQItem(pergunta: String, resposta: String) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LumisSurface)
            .clickable { expanded = !expanded }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pergunta,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = LumisTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.Remove else Icons.Default.Add,
                    contentDescription = if (expanded) "Recolher" else "Expandir",
                    tint = LumisGreen,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            if (expanded) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = resposta,
                    fontSize = 12.sp,
                    color = LumisTextSecondary
                )
            }
        }
    }
}

// ============================================================
// BOTÃO CTA PRINCIPAL
// ============================================================

@Composable
private fun CtaComprar(planoSelecionado: Plano, onAssinar: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = LumisSurface,
        shadowElevation = 12.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            BotaoContinuar(
                onClick = onAssinar,
                texto = stringResource(R.string.subscribe_plan_cta, planoSelecionado.nome, planoSelecionado.preco, planoSelecionado.precoPeriodo)
            )
        }
    }
}
