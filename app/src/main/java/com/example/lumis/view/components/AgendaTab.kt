package com.example.lumis.view.components

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

data class AgendamentoRegistro(
    val servico: String,
    val data: String,
    val horario: String,
    val endereco: String = "",
    val extras: List<String> = emptyList(),
    val total: Double = 0.0,
    val status: String = "Agendado"
)

@Composable
fun AgendaTab(registros: List<AgendamentoRegistro> = emptyList()onNotificationsClick: () -> Unit = {}) {
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
                text = "Agenda",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LumisTextPrimary
            )
            Text(
                text = "Seus serviços agendados",
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
                        text = "Nenhum agendamento ainda",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumisTextPrimary
                    )
                    Text(
                        text = "Quando você confirmar um serviço,\nele aparece aqui.",
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
                    CardAgendamento(item)
                }
            }
        }
    }
}

@Composable
private fun CardAgendamento(item: AgendamentoRegistro) {
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
                        .background(LumisGreenLight, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = item.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2FA57A)
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
                    text = "Extras: ${item.extras.joinToString(", ")}",
                    fontSize = 12.sp,
                    color = LumisTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Total: R$ ${"%.2f".format(item.total).replace(".", ",")}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LumisBlue
            )

            if (item.endereco.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Endereço: ${item.endereco}",
                    fontSize = 12.sp,
                    color = LumisTextSecondary
                )
            }
        }
    }
}