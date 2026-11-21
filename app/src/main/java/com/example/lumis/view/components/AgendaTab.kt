package com.example.lumis.view.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisBlue
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisGreenLight
import com.example.lumis.view.theme.LumisSurface
import com.example.lumis.view.theme.LumisTextPrimary
import com.example.lumis.view.theme.LumisTextSecondary
import androidx.compose.ui.res.stringResource
import com.example.lumis.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class AgendamentoRegistro(
    val id: Long,
    val servico: String,
    val data: String,
    val horario: String,
    val endereco: String = "",
    val extras: List<String> = emptyList(),
    val total: Double = 0.0,
    val status: String = "Agendado",
    val avaliacao: Int? = null
)

@Composable
fun AgendaTab(
    registros: List<AgendamentoRegistro> = emptyList(),
    onAvaliar: (Long, Int) -> Unit = { _, _ -> },
    onCancelar: (Long) -> Unit = {},
    onConcluir: (Long) -> Unit = {},
    onReagendar: (Long, String, String) -> Unit = { _, _, _ -> },
    onNotificationsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LumisBackground)
    ) {
        LumisTopBar(onNotificationsClick = onNotificationsClick)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.schedule_title),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LumisTextPrimary
            )
            Text(
                text = stringResource(R.string.scheduled_services),
                fontSize = 13.sp,
                color = LumisTextSecondary
            )
        }

        if (registros.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = LumisGreen,
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = stringResource(R.string.no_appointments),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumisTextPrimary
                    )
                    Text(
                        text = stringResource(R.string.no_appointments_hint),
                        fontSize = 13.sp,
                        color = LumisTextSecondary,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(registros.reversed()) { item ->
                    CardAgendamento(item, onAvaliar, onCancelar, onConcluir, onReagendar)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardAgendamento(
    item: AgendamentoRegistro,
    onAvaliar: (Long, Int) -> Unit,
    onCancelar: (Long) -> Unit,
    onConcluir: (Long) -> Unit,
    onReagendar: (Long, String, String) -> Unit
) {
    var mostrarReagendamento by remember { mutableStateOf(false) }
    var mostrarHorarios by remember { mutableStateOf(false) }
    var novaData by remember { mutableStateOf(item.data) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LumisSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.servico,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = LumisTextPrimary
                )
                Box(
                    modifier = Modifier
                        .background(
                            when (item.status) {
                                "Cancelado" -> Color(0x22C62828)
                                "Concluído" -> LumisGreenLight
                                else -> Color(0x22508ABF)
                            },
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = stringResource(
                            when (item.status) {
                                "Cancelado" -> R.string.status_cancelled
                                "Concluído" -> R.string.status_completed
                                else -> R.string.status_scheduled
                            }
                        ),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (item.status) {
                            "Cancelado" -> Color(0xFFC62828)
                            "Concluído" -> Color(0xFF2FA57A)
                            else -> LumisBlue
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = LumisGreen,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = item.data,
                    fontSize = 13.sp,
                    color = LumisTextSecondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = LumisGreen,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = item.horario,
                    fontSize = 13.sp,
                    color = LumisTextSecondary
                )
            }

            if (item.extras.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.extras_label, item.extras.joinToString(", ")),
                    fontSize = 12.sp,
                    color = LumisTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.total_label, "%.2f".format(item.total).replace(".", ",")),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LumisBlue
            )

            if (item.endereco.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.address_label, item.endereco),
                    fontSize = 12.sp,
                    color = LumisTextSecondary
                )
            }

            if (item.status == "Agendado") {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onCancelar(item.id) }) { Text(stringResource(R.string.cancel)) }
                    TextButton(onClick = { mostrarReagendamento = true }) { Text(stringResource(R.string.reschedule)) }
                    TextButton(onClick = { onConcluir(item.id) }) { Text(stringResource(R.string.complete)) }
                }
            }

            if (item.status == "Concluído") {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(if (item.avaliacao == null) R.string.rate_service else R.string.your_rating),
                    fontSize = 12.sp,
                    color = LumisTextSecondary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    (1..5).forEach { estrela ->
                        Icon(
                            imageVector = if (estrela <= (item.avaliacao ?: 0)) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = stringResource(R.string.stars_description, estrela),
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(28.dp).clickable { onAvaliar(item.id, estrela) }
                        )
                    }
                }
            }
        }
    }

    if (mostrarReagendamento) {
        val hojeUtc = remember {
            LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        }
        val inicial = remember(item.data) {
            runCatching {
                LocalDate.parse(item.data, DateTimeFormatter.ofPattern("d/MM/yyyy"))
                    .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            }.getOrDefault(hojeUtc).coerceAtLeast(hojeUtc)
        }
        val calendario = rememberDatePickerState(
            initialSelectedDateMillis = inicial,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= hojeUtc
            }
        )
        DatePickerDialog(
            onDismissRequest = { mostrarReagendamento = false },
            confirmButton = {
                TextButton(
                    enabled = calendario.selectedDateMillis != null,
                    onClick = {
                        calendario.selectedDateMillis?.let { millis ->
                            novaData = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            mostrarReagendamento = false
                            mostrarHorarios = true
                        }
                    }
                ) { Text(stringResource(R.string.continue_label)) }
            },
            dismissButton = { TextButton(onClick = { mostrarReagendamento = false }) { Text(stringResource(R.string.cancel)) } }
        ) {
            DatePicker(state = calendario, title = { Text(stringResource(R.string.choose_new_date), Modifier.padding(24.dp)) })
        }
    }

    if (mostrarHorarios) {
        val horarios = listOf("08:00", "09:30", "11:00", "13:00", "14:30", "16:00", "17:30")
        var novoHorario by remember { mutableStateOf(item.horario.takeIf { it in horarios } ?: horarios.first()) }
        AlertDialog(
            onDismissRequest = { mostrarHorarios = false },
            title = { Text(stringResource(R.string.choose_new_time)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(novaData, color = LumisTextSecondary)
                    horarios.chunked(3).forEach { linha ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            linha.forEach { horario ->
                                FilterChip(
                                    selected = novoHorario == horario,
                                    onClick = { novoHorario = horario },
                                    label = { Text(horario) }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onReagendar(item.id, novaData, novoHorario)
                        mostrarHorarios = false
                    }
                ) { Text(stringResource(R.string.save)) }
            },
            dismissButton = { TextButton(onClick = { mostrarHorarios = false }) { Text(stringResource(R.string.back)) } }
        )
    }
}
