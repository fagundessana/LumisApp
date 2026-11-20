package com.example.lumis.view.screens.city.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.example.lumis.view.screens.city.CidadeColors
import com.example.lumis.view.screens.city.components.CircularFillIndicator
import com.example.lumis.view.screens.city.model.BinFillItem
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ModoTema
import com.example.lumis.view.theme.Questrial

// Card principal de preenchimento da lixeira
// Chip "+ Ver mais" no topo-esquerdo, CircularFillIndicator no centro,
// sub-card cinza com lista de ruas e botão de filtro à direita
@Composable
fun BinFillCard(
    modifier: Modifier = Modifier,
    fraction: Float = com.example.lumis.view.screens.city.model.CidadeUiModel.binFillFraction,
    label: String = com.example.lumis.view.screens.city.model.CidadeUiModel.binFillLabel,
    items: List<BinFillItem> = com.example.lumis.view.screens.city.model.CidadeUiModel.binFillItems,
    onVerMais: () -> Unit = {},
    onFiltrar: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = LumisColors.Surface,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Chip "+ Ver mais" no canto superior esquerdo (alinhado à direita)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                FilterChip(
                    selected = false,
                    onClick = onVerMais,
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = CidadeColors.ChipVerMaisBackground,
                        labelColor = CidadeColors.ChipVerMaisText,
                        selectedContainerColor = CidadeColors.ChipVerMaisBackground,
                        selectedLabelColor = CidadeColors.ChipVerMaisText
                    ),
                    label = {
                        Text(
                            text = "+ Ver mais",
                            fontFamily = Questrial,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // CircularFillIndicator centralizado
            CircularFillIndicator(
                fraction = fraction,
                label = label,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sub-card cinza claro com lista de ruas + botão filtro
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CidadeColors.SubCardBackground
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${item.name} - ${item.fillPercent} %",
                                fontFamily = Questrial,
                                fontSize = 13.sp,
                                color = LumisColors.TextPrimary
                            )
                            if (index == items.lastIndex) {
                                // Botão circular de filtro apenas na última linha
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(CidadeColors.FilterButtonBackground)
                                        .clickable(onClick = onFiltrar),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.FilterAlt,
                                        contentDescription = "Filtrar",
                                        tint = CidadeColors.FilterButtonIcon,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                        if (index != items.lastIndex) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9FB, widthDp = 360)
@Composable
private fun BinFillCardPreview() {
    LumisTheme(modo = ModoTema.SISTEMA) {
        BinFillCard()
    }
}
