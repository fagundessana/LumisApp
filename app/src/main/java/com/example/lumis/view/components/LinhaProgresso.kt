package com.example.lumis.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lumis.view.theme.LumisTeal

private const val TotalEtapas = 5

@Composable
fun LinhaProgresso(
    etapaAtual: Int = 0,
    modifier: Modifier = Modifier
) {
    val fracao = ((etapaAtual.coerceIn(0, TotalEtapas - 1) + 1).toFloat() / TotalEtapas)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFE0E0E0))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fracao)
                .fillMaxHeight()
                .background(LumisTeal)
        )
    }
}