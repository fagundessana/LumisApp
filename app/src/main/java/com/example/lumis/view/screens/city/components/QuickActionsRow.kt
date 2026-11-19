package com.example.lumis.view.screens.city.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.components.lembreteEmBreve
import com.example.lumis.view.screens.city.CidadeColors
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.Questrial

// Linha de 3 ações rápidas lado a lado (peso igual)
// Cada botão: ícone no topo, texto centralizado embaixo
@Composable
fun QuickActionsRow(
    modifier: Modifier = Modifier,
    buttonHeight: androidx.compose.ui.unit.Dp = 80.dp,
    cornerRadius: androidx.compose.ui.unit.Dp = 12.dp
) {
    val onEmBreve = lembreteEmBreve()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Encontrar Ecopontos - verde
        QuickActionButton(
            icon = Icons.Filled.Delete, // ícone de reciclagem/lixeira
            label = "Encontrar\nEcopontos",
            backgroundColor = CidadeColors.ActionEcopotonsBackground,
            iconColor = CidadeColors.ActionIconWhite,
            textColor = CidadeColors.ActionIconWhite,
            onClick = onEmBreve,
            modifier = Modifier.weight(1f).height(buttonHeight),
            cornerRadius = cornerRadius
        )

        // 2. Solicitar Coleta - amarelo
        QuickActionButton(
            icon = Icons.Filled.LocalShipping,
            label = "Solicitar\nColeta",
            backgroundColor = CidadeColors.ActionColetaBackground,
            iconColor = CidadeColors.ActionIconWhite,
            textColor = CidadeColors.ActionIconWhite,
            onClick = onEmBreve,
            modifier = Modifier.weight(1f).height(buttonHeight),
            cornerRadius = cornerRadius
        )

        // 3. Ver Dados Ambientais - verde bem claro
        QuickActionButton(
            icon = Icons.Filled.PieChart,
            label = "Ver Dados\nAmbientais",
            backgroundColor = CidadeColors.ActionDadosBackground,
            iconColor = CidadeColors.ActionDadosText,
            textColor = CidadeColors.ActionDadosText,
            onClick = onEmBreve,
            modifier = Modifier.weight(1f).height(buttonHeight),
            cornerRadius = cornerRadius
        )
    }
}

// Botão individual de ação rápida
@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    backgroundColor: Color,
    iconColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: androidx.compose.ui.unit.Dp = 12.dp
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
        shape = RoundedCornerShape(cornerRadius),
        color = backgroundColor,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                fontFamily = Questrial,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9FB, widthDp = 360)
@Composable
private fun QuickActionsRowPreview() {
    com.example.lumis.view.theme.LumisTheme {
        QuickActionsRow()
    }
}