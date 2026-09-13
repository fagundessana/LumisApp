package com.example.lumis.view.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.controller.ServicesController
import com.example.lumis.model.Servico
import com.example.lumis.view.components.BotaoContinuar
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.theme.*

// ---------------------------------------------------------
// TELA PRINCIPAL (SERVIÇOS)
// ---------------------------------------------------------

@Composable
fun ServicesScreen(
    onAbrirPlanos: () -> Unit = {},
    onAbrirAgendamento: (Servico) -> Unit = {}
) {
    val controller = remember { ServicesController() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LumisBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LumisBackground)
        ) {
            LumisTopBar()

            Abas(onAbrirPlanos = onAbrirPlanos)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE0E0E0))
                ) {
                    Box(
                        modifier = Modifier
                            .width(52.dp)
                            .fillMaxSize()
                            .background(LumisTeal)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "CATEGORIA",
                    color = LumisBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Qual tipo de limpeza você\nprecisa?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(controller.servicos) { servico ->
                        CardServico(
                            servico = servico,
                            selecionado = controller.servicoSelecionado == servico.nome,
                            onClick = {
                                controller.selecionarServico(servico)
                            }
                        )
                    }
                }
            }

            BotaoContinuar(
                onClick = {
                    controller.servicos
                        .find { it.nome == controller.servicoSelecionado }
                        ?.let(onAbrirAgendamento)
                },
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
            )
        }
    }
}

// ---------------------------------------------------------
// ABAS
// ---------------------------------------------------------

@Composable
private fun Abas(onAbrirPlanos: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 10.dp)
            .height(29.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(Color(0xFFE7E7E7))
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(7.dp))
                .background(LumisGreenVivid),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Serviços",
                color = Color.White,
                fontSize = 11.sp
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clickable { onAbrirPlanos() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Planos",
                color = LumisGreySecondary,
                fontSize = 11.sp
            )
        }
    }
}

// ---------------------------------------------------------
// CARD DE SERVIÇO
// ---------------------------------------------------------

@Composable
private fun CardServico(
    servico: Servico,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    val corBorda = if (selecionado) LumisGreenVivid else Color(0xFFD5D5D5)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(LumisSurface)
            .border(width = 1.dp, color = corBorda, shape = RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = servico.nome,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Box(
                modifier = Modifier
                    .size(17.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        color = if (selecionado) LumisGreenVivid else Color(0xFF999999),
                        shape = CircleShape
                    )
                    .background(if (selecionado) LumisGreenVivid else Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (selecionado) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selecionado",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = servico.descricao,
            fontSize = 9.sp,
            lineHeight = 12.sp,
            color = LumisGreySecondary
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = servico.preco,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = LumisBlue
        )
    }
}