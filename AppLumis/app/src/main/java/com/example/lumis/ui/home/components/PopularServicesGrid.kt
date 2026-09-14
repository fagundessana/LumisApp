// Grade "Serviços populares": os 8 quadradinhos (Casas, Escritórios...).
// A gente divide a lista em fileiras de 4 com o chunked(4) e desenha
// uma Row pra cada fileira. O "Em expansão" é o verdão (isHighlight).

package com.example.lumis.ui.home.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.ui.home.model.HomePreviewData
import com.example.lumis.ui.home.model.PopularService
import com.example.lumis.ui.theme.LumisColors
import com.example.lumis.ui.theme.Questrial

@Composable
fun PopularServicesGrid(
    modifier: Modifier = Modifier,
    onSeeAllClick: () -> Unit = {},
    onServiceClick: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        SectionHeader(title = "Serviços populares", onActionClick = onSeeAllClick)
        val rows = HomePreviewData.popularServices.chunked(4)
        rows.forEachIndexed { index, row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { service ->
                    ServiceCard(
                        service = service,
                        onClick = { onServiceClick(service.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Completa a linha se necessário (não ocorre com 8 itens, mas deixa robusto)
                repeat(4 - row.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            if (index != rows.lastIndex) {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun ServiceCard(
    service: PopularService,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (service.isHighlight) LumisColors.PrimaryHover else LumisColors.CardTint
    val content = if (service.isHighlight) LumisColors.OnButton else LumisColors.Primary
    val labelColor = if (service.isHighlight) LumisColors.OnButton else LumisColors.TextPrimary

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = bg,
        modifier = modifier.height(88.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (service.isHighlight) LumisColors.Primary.copy(alpha = 0.35f)
                        else LumisColors.White
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = service.icon,
                    contentDescription = service.label,
                    tint = if (service.isHighlight) LumisColors.White else content,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = service.label,
                fontFamily = Questrial,
                fontSize = 10.5.sp,
                lineHeight = 13.sp,
                fontWeight = if (service.isHighlight) FontWeight.Bold else FontWeight.Normal,
                color = labelColor,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2
            )
        }
    }
}
