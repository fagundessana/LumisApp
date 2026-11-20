package com.example.lumis.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.screens.employee_profile.ProfileColors
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ModoTema

fun ModoTema.rotulo(): String = when (this) {
    ModoTema.CLARO -> "Claro"
    ModoTema.ESCURO -> "Escuro"
    ModoTema.SISTEMA -> "Sistema"
}

// Card "Tema" exibido nas configurações; mostra o modo atual e abre o modal ao ser tocado.
@Composable
fun TemaCard(
    modoAtual: ModoTema,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ProfileColors.CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ProfileColors.PrimaryLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = null,
                    tint = ProfileColors.PrimaryDark,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tema",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = ProfileColors.TextPrimary
                )
                Text(
                    text = modoAtual.rotulo(),
                    fontSize = 12.sp,
                    color = ProfileColors.TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = ProfileColors.TextSecondary
            )
        }
    }
}

// Modal com as três opções. Selecionar aplica o tema e fecha o modal.
@Composable
fun TemaDialog(
    modoAtual: ModoTema,
    onSelecionar: (ModoTema) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ProfileColors.CardBackground,
        shape = RoundedCornerShape(24.dp),
        title = {
            Text(
                text = "Tema",
                fontWeight = FontWeight.Bold,
                color = ProfileColors.TextPrimary
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                ModoTema.values().forEach { modo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onSelecionar(modo)
                                onDismiss()
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = modo == modoAtual,
                            onClick = {
                                onSelecionar(modo)
                                onDismiss()
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = ProfileColors.Primary,
                                unselectedColor = ProfileColors.TextSecondary
                            )
                        )
                        Text(
                            text = modo.rotulo(),
                            fontSize = 15.sp,
                            color = ProfileColors.TextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Fechar", color = ProfileColors.Primary)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun TemaCardPreview() {
    LumisTheme(modo = ModoTema.ESCURO) {
        TemaCard(modoAtual = ModoTema.SISTEMA, onClick = {})
    }
}