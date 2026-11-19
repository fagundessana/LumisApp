package com.example.lumis.view.screens.city.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.screens.city.CidadeColors
import com.example.lumis.view.screens.city.model.StatusItem
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.Montserrat
import com.example.lumis.view.theme.Questrial

// Card de resumo com 4 colunas de status (Disponíveis, Atenção, Prioritárias, Urgentes)
// Título "Status da cidade" + ícone MoreHoriz no topo
@Composable
fun StatusSummaryCard(
    modifier: Modifier = Modifier,
    statusItems: List<StatusItem> = com.example.lumis.view.screens.city.model.CidadeUiModel.statusItems
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = LumisColors.White,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cabeçalho: título + ícone MoreHoriz
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Status da cidade",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = LumisColors.TextPrimary
                )
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = "Mais opções",
                    tint = LumisColors.SecondaryText,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4 colunas de status - usando Row com itens de largura igual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                statusItems.forEach { item ->
                    StatusColumn(item = item)
                }
            }
        }
    }
}

// Cada coluna individual de status
@Composable
private fun StatusColumn(
    item: StatusItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Container do ícone (círculo colorido pequeno)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(item.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.color,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Número
        Text(
            text = item.count.toString(),
            fontFamily = Montserrat,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            color = item.color
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Rótulo
        Text(
            text = item.label,
            fontFamily = Questrial,
            fontSize = 12.sp,
            color = item.color,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9FB, widthDp = 360)
@Composable
private fun StatusSummaryCardPreview() {
    com.example.lumis.view.theme.LumisTheme {
        StatusSummaryCard()
    }
}