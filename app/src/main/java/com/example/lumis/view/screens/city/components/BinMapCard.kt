package com.example.lumis.view.screens.city.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
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
import com.example.lumis.view.screens.city.model.MapMarker
import com.example.lumis.view.screens.city.model.MarkerType
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ModoTema
import com.example.lumis.view.theme.Questrial
import com.example.lumis.view.theme.TemaApp.corTema

// Card do mapa das lixeiras
// Fundo simples com marcadores posicionados usando Box com offset fixo
// (largura conhecida ~328dp com padding 16dp cada lado em tela 360dp)
@Composable
fun BinMapCard(
    modifier: Modifier = Modifier,
    markers: List<MapMarker> = com.example.lumis.view.screens.city.model.CidadeUiModel.mapMarkers,
    mapHeight: androidx.compose.ui.unit.Dp = 160.dp
) {
    // Largura aproximada do mapa: 360dp - 32dp (padding horizontal) = 328dp
    val mapWidthPx = 328f

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
            // Título
            Text(
                text = "Mapa das lixeiras",
                fontFamily = Questrial,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = LumisColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Área do mapa - fundo simples com marcadores
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(mapHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(corTema(claro = Color(0xFFF0F0EB), escuro = Color(0xFF2A2D2B)))
            ) {
                // Marcadores posicionados por fração da largura/altura conhecida
                markers.forEach { marker ->
                    val markerSize = 28.dp
                    val xPx = (mapWidthPx * marker.xFraction) - (markerSize.value / 2)
                    val yPx = (mapHeight.value * marker.yFraction) - (markerSize.value / 2)

                    Box(
                        modifier = Modifier
                            .size(markerSize)
                            .offset(xPx.dp, yPx.dp)
                    ) {
                        MapMarkerView(marker = marker)
                    }
                }
            }
        }
    }
}

// Visualizador individual de um marcador no mapa
@Composable
private fun MapMarkerView(marker: MapMarker) {
    val (icon, color) = marker.type.icon to marker.type.color

    // Se for o marcador de localização (último), usa ícone de localização verde
    val isLocation = marker.type == MarkerType.LOCALIZACAO

    Box(contentAlignment = Alignment.Center) {
        // Círculo externo branco com borda da cor do status
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.dp, color, CircleShape)
        )
        // Círculo interno colorido com ícone
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isLocation) Icons.Filled.MyLocation else icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(10.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9FB, widthDp = 360)
@Composable
private fun BinMapCardPreview() {
    LumisTheme(modo = ModoTema.SISTEMA) {
        BinMapCard()
    }
}