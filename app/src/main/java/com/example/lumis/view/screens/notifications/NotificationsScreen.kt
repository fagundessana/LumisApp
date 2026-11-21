package com.example.lumis.view.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.screens.employee_profile.ProfileColors
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ModoTema

// ---------------------------------------------------------------------------------------------
// MODELO DE NOTIFICAÇÃO
// ---------------------------------------------------------------------------------------------

data class Notification(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val horario: String,
    val lida: Boolean = false
)

// A tela começa vazia até existir uma fonte persistente de notificações reais.
// Não exibimos eventos, pagamentos ou avaliações inventados.
private val notificacoesLocais = mutableStateListOf<Notification>()

// ---------------------------------------------------------------------------------------------
// TELA DE NOTIFICAÇÕES
// ---------------------------------------------------------------------------------------------

@Composable
fun NotificationsScreen(
    onBack: () -> Unit = {}
) {
    val notificacoes = remember { notificacoesLocais }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = LumisBackground
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                LumisTopBar(onBack = onBack)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Notificações",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProfileColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (notificacoes.isEmpty()) {
                        // Estado vazio
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(ProfileColors.PrimaryLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.NotificationsNone,
                                        contentDescription = null,
                                        tint = ProfileColors.Primary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                                Text(
                                    text = "Nenhuma notificação por enquanto",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ProfileColors.TextPrimary
                                )
                                Text(
                                    text = "Quando houver novidades, você verá aqui.",
                                    fontSize = 13.sp,
                                    color = ProfileColors.TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(notificacoes) { notificacao ->
                                NotificationCard(
                                    notificacao = notificacao,
                                    onClick = {
                                        val index = notificacoes.indexOfFirst { it.id == notificacao.id }
                                        if (index >= 0 && !notificacao.lida) {
                                            val updated = notificacao.copy(lida = true)
                                            notificacoes[index] = updated
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// CARD DE NOTIFICAÇÃO
// ---------------------------------------------------------------------------------------------

@Composable
private fun NotificationCard(
    notificacao: Notification,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notificacao.lida) ProfileColors.CardBackground else ProfileColors.PrimaryLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = notificacao.titulo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProfileColors.TextPrimary
                    )
                    if (!notificacao.lida) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(ProfileColors.Primary)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notificacao.descricao,
                    fontSize = 13.sp,
                    color = ProfileColors.TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notificacao.horario,
                    fontSize = 11.sp,
                    color = ProfileColors.TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ProfileColors.TextSecondary,
                modifier = Modifier.size(20.dp).padding(start = 12.dp)
            )
        }
    }
}

// ---------------------------------------------------------------------------------------------
// PREVIEW
// ---------------------------------------------------------------------------------------------

@androidx.compose.ui.tooling.preview.Preview(name = "Notificações", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun NotificationsScreenPreview() {
    LumisTheme(modo = ModoTema.SISTEMA) {
        NotificationsScreen()
    }
}
