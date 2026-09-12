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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.lumis.ui.theme.LumisBackground
import com.example.lumis.ui.theme.LumisGreen
import com.example.lumis.ui.theme.LumisGreenDark
import com.example.lumis.ui.theme.LumisGreenLight
import com.example.lumis.ui.theme.LumisSurface
import com.example.lumis.ui.theme.LumisTextPrimary
import com.example.lumis.ui.theme.LumisTextSecondary

// ============================================================
// TELA DE PLANOS
// ============================================================

data class Plano(
    val id: String,
    val nome: String,
    val descricao: String,
    val preco: String,
    val precoPeriodo: String,
    val caracteristicas: List<String>,
    val popular: Boolean = false,
    val corFundo: Color,
    val corTexto: Color
)

@Composable
fun PlanosScreen() {
    var sel by remember { mutableStateOf("regular") }
    val lista = listOf(
        Plano(
            id = "basico",
            nome = "Plano Básico",
            descricao = "Perfeito para quem busca praticidade com economia",
            preco = "R$ 99,90",
            precoPeriodo = "/mês",
            caracteristicas = listOf(
                "2 limpezas/mês (até 4 cômodos)",
                "1 serviço especializado por trimestre",
                "Atendimento em até 3 dias úteis",
                "Dicas mensais de organização"
            ),
            corFundo = LumisSurface,
            corTexto = LumisTextPrimary
        ),
        Plano(
            id = "regular",
            nome = "Plano Regular",
            descricao = "Equilíbrio perfeito e o mais escolhido",
            preco = "R$ 179,90",
            precoPeriodo = "/mês",
            caracteristicas = listOf(
                "3 limpezas/mês (até 4 cômodos)",
                "2 serviços especializados por trimestre",
                "Atendimento em até 2 dias úteis",
                "Suporte prioritário por WhatsApp"
            ),
            popular = true,
            corFundo = LumisGreen,
            corTexto = Color.White
        ),
        Plano(
            id = "premium",
            nome = "Plano Premium",
            descricao = "Conforto total e suporte prioritário",
            preco = "R$ 399,90",
            precoPeriodo = "/mês",
            caracteristicas = listOf(
                "Limpeza semanal dedicada",
                "Todos os serviços especializados inclusos",
                "Atendimento em até 24h",
                "Equipe fixa ou exclusiva",
                "Tudo do plano regular + benefícios extras"
            ),
            corFundo = Color.Black,
            corTexto = Color.White
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
            TopBarPlanos()

            // Conteúdo scrollável (lista de planos + faq)
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Cabeçalho
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "PLANOS",
                            color = LumisGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Escolha o plano ideal para você",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumisTextPrimary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Compare nossos pacotes encontre o que melhor atende sua rotina",
                            fontSize = 13.sp,
                            color = LumisTextSecondary
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

                                // FAQ
                item {
                    Spacer(Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "PERGUNTAS FREQUENTES",
                            color = LumisGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))

                        FAQItem(
                            pergunta = "Os produtos de limpeza estão incluídos?",
                            resposta = "Sim, todos os planos incluem produtos eco-friendly de alta qualidade. Não há custo adicional."
                        )
                        Spacer(Modifier.height(6.dp))
                        FAQItem(
                            pergunta = "Posso cancelar quando quiser?",
                            resposta = "Sim, sem multa ou fidelidade. Cancelamento gratuito até 24h antes do agendamento."
                        )
                        Spacer(Modifier.height(6.dp))
                        FAQItem(
                            pergunta = "Funciona em qualquer residência?",
                            resposta = "Sim, atendemos casas, apartamentos e coberturas. Para terrenos/empresas, fale conosco."
                        )
                    }
                }
            }
        }

        // Botão CTA Principal
        CtaComprar(
            texto = "Assinar o Plano Regular — R$ 179,90/mês",
            selecionadoNome = "${(lista.find { it.id == sel })?.nome ?: "Regular"}",
            preco = (lista.find { it.id == sel })?.preco ?: "R$ 179,90"
        )
    }
}

// ============================================================
// TOPO (com logo + ícones)
// ============================================================

@Composable
private fun TopBarPlanos() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LumisSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo + Texto
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(LumisGreen),
                contentAlignment = Alignment.Center
            ) {
                Text("L", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(Modifier.width(10.dp))
            Text("Lumis", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = LumisGreen)
        }

        // Notificação + Perfil
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Default.Add, contentDescription = "Notificações", tint = LumisTextSecondary)
            Icon(Icons.Default.Home, contentDescription = "Perfil", tint = LumisGreen)
        }
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
            .background(if (plano.popular) LumisGreen else LumisSurface)
            .border(
                width = if (plano.popular) 0.dp else 1.dp,
                color = if (plano.popular) Color.Transparent else Color(0xFFE7E9EC),
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
                        color = plano.corTexto
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        plano.descricao,
                        fontSize = 12.sp,
                        color = if (plano.popular) LumisGreenLight else LumisTextSecondary
                    )
                }
                Spacer(Modifier.weight(1f))
                if (plano.popular) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(LumisSurface)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "Mais popular",
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
                    color = if (plano.popular) LumisSurface else LumisGreen
                )
                Text(
                    plano.precoPeriodo,
                    fontSize = 12.sp,
                    color = if (plano.popular) LumisGreenLight else LumisTextSecondary,
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
                            tint = if (plano.popular) LumisGreenLight else LumisGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(feature, fontSize = 13.sp, color = if (plano.popular) LumisGreenLight else LumisTextPrimary)
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
private fun CtaComprar(texto: String, selecionadoNome: String, preco: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(LumisSurface)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "✅ $selecionadoNome assinado!",
                    Toast.LENGTH_LONG
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = LumisGreen
            )
        ) {
            Text(
                text = texto,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
