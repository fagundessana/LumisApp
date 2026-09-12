package com.example.lumis.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.example.lumis.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.ui.theme.LumisGreen
import com.example.lumis.ui.theme.LumisBackground
import com.example.lumis.ui.theme.LumisSurface
import com.example.lumis.ui.theme.LumisTextPrimary
import com.example.lumis.ui.theme.LumisTextSecondary
import java.time.YearMonth

// ============================================================
// PALETA (usa as mesmas da Claud e da servicesScreen)
// ============================================================
private val VerdeLumis = LumisGreen
private val AzulLumis = Color(0xFF508ABF)
private val Fundo = LumisBackground
private val CinzaTexto = LumisTextSecondary

// ============================================================
// TELA DE AGENDAMENTO
// ============================================================

@Composable
fun AgendamentoScreen(
    servicoSelecionado: String? = null,
    onBack: () -> Unit = {},
    onConfirm: (dia: Int, horario: String) -> Unit = { _, _ -> }
) {

    // Data atual / padrão
    val hoje = YearMonth.now()
    var ano by remember { mutableStateOf(hoje.year) }
    var mes by remember { mutableStateOf(hoje.month) }
    var diaSelecionado by remember { mutableStateOf(15) }

    // Horário selecionado
    var horarioSelecionado by remember { mutableStateOf("10:00") }

    // Dias do mês atual (simples, sem validar os dias da semana)
    val dias = (1..mes.length(hoje.isLeapYear)).toList()

    // Horários disponíveis
    val horarios = listOf("08:00", "09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
    ) {
        // -------------------------------------------------
        // TOPO
        // -------------------------------------------------
        TopoAgendamento(
            servico = servicoSelecionado,
            onBack = onBack
        )

        // -------------------------------------------------
        // CONTEÚDO
        // -------------------------------------------------
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Título
            Text(
                text = "AGENDAMENTO",
                color = AzulLumis,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Quando você precisa?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LumisTextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mês atual
            Text(
                text = formatarMes(mes, ano),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = LumisTextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CALENDÁRIO
            CalendarioBox(
                dias = dias,
                diaAtual = diaSelecionado,
                onDiaSelecionado = { diaSelecionado = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // HORÁRIO
            Text(
                text = "Horário de Início",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = LumisTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Grid de horários (2 linhas)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                horarios.take(4).forEach { h ->
                    HorarioPill(
                        horario = h,
                        selecionado = horarioSelecionado == h,
                        onClick = { horarioSelecionado = h }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                horarios.drop(4).forEach { h ->
                    HorarioPill(
                        horario = h,
                        selecionado = horarioSelecionado == h,
                        onClick = { horarioSelecionado = h }
                    )
                }
            }
        }

        // -------------------------------------------------
        // BOTÃO CONTINUAR
        // -------------------------------------------------
        Button(
            onClick = { onConfirm(diaSelecionado, horarioSelecionado) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeLumis
            )
        ) {
            Text(
                text = "Continuar",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// ============================================================
// TOPO (com serviço selecionado e voltar)
// ============================================================

@Composable
fun TopoAgendamento(
    servico: String?,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(LumisSurface)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Botão voltar + logo + texto
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIos,
                    contentDescription = "Voltar",
                    tint = LumisTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo Lumis",
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(text = "Lumis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = VerdeLumis)
                if (servico != null) {
                    Text(text = servico, fontSize = 11.sp, color = CinzaTexto)
                }
            }
        }

        // Ícones direita
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notificações",
                tint = CinzaTexto,
                modifier = Modifier.size(22.dp)
            )
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Perfil",
                tint = VerdeLumis,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ============================================================
// CALENDÁRIO (grid de dias)
// ============================================================

@Composable
fun CalendarioBox(
    dias: List<Int>,
    diaAtual: Int,
    onDiaSelecionado: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(LumisSurface)
            .border(1.dp, Color(0xFFE7E9EC), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Dias da semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb").forEach {
                Text(it, fontSize = 11.sp, color = CinzaTexto, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.weight(1f))
            }
        }
        Spacer(Modifier.height(12.dp))

        // Grade de dias
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(240.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(dias) { dia ->
                DiaCelula(
                    dia = dia,
                    selecionado = dia == diaAtual,
                    onClick = { onDiaSelecionado(dia) }
                )
            }
        }
    }
}

@Composable
fun DiaCelula(
    dia: Int,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(if (selecionado) VerdeLumis else Color.Transparent)
            .border(if (selecionado) 0.dp else 1.dp, Color(0xFFE7E9EC), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dia.toString(),
            fontSize = 14.sp,
            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
            color = if (selecionado) Color.White else LumisTextPrimary
        )
    }
}

// ============================================================
// HORÁRIO (pill)
// ============================================================

@Composable
fun HorarioPill(
    horario: String,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (selecionado) AzulLumis else LumisSurface)
            .border(1.dp, Color(0xFFE7E9EC), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = horario,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (selecionado) Color.White else CinzaTexto
        )
    }
}

// ============================================================
// UTIL — nome do mês
// ============================================================

private fun formatarMes(mes: java.time.Month, ano: Int): String {
    val nome = when (mes.value) {
        1 -> "Janeiro"
        2 -> "Fevereiro"
        3 -> "Março"
        4 -> "Abril"
        5 -> "Maio"
        6 -> "Junho"
        7 -> "Julho"
        8 -> "Agosto"
        9 -> "Setembro"
        10 -> "Outubro"
        11 -> "Novembro"
        12 -> "Dezembro"
        else -> "Mês"
    }
    return "$nome $ano"
}
