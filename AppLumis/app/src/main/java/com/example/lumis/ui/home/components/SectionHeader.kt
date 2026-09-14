// Cabecçalho padrão das seções (ex: "Categorias" + "Ver todas").
// O grupo fez esse componente pra não repetir o mesmo Row em 3 lugares.

package com.example.lumis.ui.home.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.lumis.ui.theme.LumisColors
import com.example.lumis.ui.theme.Montserrat
import com.example.lumis.ui.theme.Questrial

@Composable
fun SectionHeader(
    title: String,
    actionLabel: String = "Ver todas",
    onActionClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Título grandão à esquerda (o weight empurra o botão pra direita).
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            fontFamily = Montserrat,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp,
            color = LumisColors.TextPrimary
        )
        // Botãozinho verde "Ver todas" à direita.
        TextButton(onClick = onActionClick) {
            Text(
                text = actionLabel,
                fontFamily = Questrial,
                fontSize = 12.sp,
                color = LumisColors.Primary
            )
        }
    }
}
