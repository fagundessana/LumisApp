// Barrinha do topo: endereço + sino, igual ao protótipo.
// É um card branco arredondado com uma Row dentro.

package com.example.lumis.view.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.Montserrat
import com.example.lumis.view.theme.Questrial
import com.example.lumis.view.components.LocalTemNotificacoesNaoLidas
import com.example.lumis.R

@Composable
fun TopLocationBar(
    modifier: Modifier = Modifier,
    address: String = "211B Baker Street, London",
    subtitle: String = "Sua localização atual",
    onLocationClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = LumisColors.Surface,
        border = BorderStroke(1.dp, LumisColors.Border),
        onClick = onLocationClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LumisColors.Primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.lumis),
                    contentDescription = "Logo Lumis",
                    tint = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Meio: endereço + setinha + "Sua localização atual" em verde.
            // O weight(1f) faz essa coluna ocupar todo o espaço do meio.
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = address,
                        fontFamily = Questrial,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumisColors.TextPrimary
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = LumisColors.SecondaryText,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = subtitle,
                    fontFamily = Questrial,
                    fontSize = 11.sp,
                    color = LumisColors.Primary
                )
            }
            // Botão do sino
            CircleIconButton(
                onClick = onNotificationClick,
                contentDescription = "Notificações",
                icon = Icons.Filled.NotificationsNone
            )
        }
    }
}

@Composable
private fun CircleIconButton(
    onClick: () -> Unit,
    contentDescription: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    badge: @Composable (() -> Unit)? = null
) {
    val temNaoLidas = LocalTemNotificacoesNaoLidas.current
    Box(modifier.size(42.dp)) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            color = LumisColors.CardTint,
            border = BorderStroke(1.dp, LumisColors.Border),
            modifier = Modifier.size(40.dp).align(Alignment.Center)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = LumisColors.Tertiary,
                    modifier = Modifier.size(20.dp)
                )
                badge?.invoke()
            }
        }
        if (temNaoLidas) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color(0xFFE45869))
            )
        }
    }
}
