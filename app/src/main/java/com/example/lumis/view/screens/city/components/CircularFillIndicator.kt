package com.example.lumis.view.screens.city.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.screens.city.CidadeColors
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ModoTema
import com.example.lumis.view.theme.Montserrat
import com.example.lumis.view.theme.Questrial

// Indicador circular de preenchimento da lixeira
// Arco desenhado com Canvas - usa múltiplos arcos concêntricos para simular espessura do traço
// sweep = 360 * fração, startAngle = -90 (topo)
// Dentro: ícone de lixeira verde + texto "XX % cheio" em negrito
@Composable
fun CircularFillIndicator(
    modifier: Modifier = Modifier,
    fraction: Float,           // 0.0 a 1.0
    label: String,             // ex: "50 % cheio"
    size: androidx.compose.ui.unit.Dp = 140.dp,
    strokeWidth: androidx.compose.ui.unit.Dp = 12.dp
) {
    val sweepAngle = 360f * fraction.coerceIn(0f, 1f)
    val strokePx = strokeWidth.value
    val radius = (size.value / 2) - (strokePx / 2)

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Canvas com os arcos (usa múltiplos arcos concêntricos para simular espessura do traço)
        Canvas(modifier = Modifier.size(size)) {
            val centerX = size.value / 2
            val centerY = size.value / 2

            // Trilha (fundo do arco) - verde bem claro - desenha múltiplos arcos concêntricos
            for (i in 0 until strokePx.toInt()) {
                val currentRadius = radius - i
                if (currentRadius > 0) {
                    drawArc(
                        color = CidadeColors.ProgressTrackGreen,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(centerX - currentRadius, centerY - currentRadius),
                        size = Size(currentRadius * 2, currentRadius * 2)
                    )
                }
            }

            // Progresso - laranja - desenha múltiplos arcos concêntricos
            if (fraction > 0f) {
                for (i in 0 until strokePx.toInt()) {
                    val currentRadius = radius - i
                    if (currentRadius > 0) {
                        drawArc(
                            color = CidadeColors.ProgressArcOrange,
                            startAngle = -90f,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = Offset(centerX - currentRadius, centerY - currentRadius),
                            size = Size(currentRadius * 2, currentRadius * 2)
                        )
                    }
                }
            }
        }

        // Conteúdo central: ícone + label
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(CidadeColors.StatusDisponivel.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Lixeira",
                    tint = CidadeColors.StatusDisponivel,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                fontFamily = Montserrat,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = LumisColors.TextPrimary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9FB, widthDp = 200, heightDp = 200)
@Composable
private fun CircularFillIndicatorPreview() {
    LumisTheme(modo = ModoTema.SISTEMA) {
        CircularFillIndicator(fraction = 0.5f, label = "50 % cheio")
    }
}