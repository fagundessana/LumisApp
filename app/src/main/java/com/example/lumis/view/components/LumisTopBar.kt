package com.example.lumis.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lumis.R
import com.example.lumis.view.theme.LumisBlue
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisSurface
import com.example.lumis.view.theme.LumisTextPrimary
import com.example.lumis.view.theme.TemaApp.corTema

val LocalTemNotificacoesNaoLidas = staticCompositionLocalOf { false }

@Composable
fun LumisTopBar(
    onBack: (() -> Unit)? = null,
    onNotificationsClick: (() -> Unit)? = null,
    sobreImagem: Boolean = false
) {
    val temNaoLidas = LocalTemNotificacoesNaoLidas.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (sobreImagem) Color.Transparent else LumisSurface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (sobreImagem) Color.Black.copy(alpha = 0.38f) else Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIos,
                        contentDescription = "Voltar",
                        tint = if (sobreImagem) Color.White else LumisTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LumisGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.lumis),
                    contentDescription = "Logo Lumis",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        if (onNotificationsClick != null) Box(Modifier.size(42.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (sobreImagem) Color.Black.copy(alpha = 0.38f)
                        else corTema(claro = Color(0xFFEAF0F4), escuro = Color(0xFF262F36))
                    )
                    .clickable { onNotificationsClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.NotificationsNone,
                    contentDescription = "Notificações",
                    tint = if (sobreImagem) Color.White else LumisBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (temNaoLidas) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-1).dp, y = 1.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE45869))
                )
            }
        } else Spacer(Modifier.size(42.dp))
    }
}
