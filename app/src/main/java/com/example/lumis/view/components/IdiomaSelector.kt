package com.example.lumis.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lumis.R
import com.example.lumis.controller.ModoIdioma
import com.example.lumis.view.screens.employee_profile.ProfileColors

@Composable
fun IdiomaSelector(
    atual: ModoIdioma,
    onSelecionar: (ModoIdioma) -> Unit,
    modifier: Modifier = Modifier
) {
    var aberto by remember { mutableStateOf(false) }
    val rotuloAtual = idiomaRotulo(atual)

    Surface(
        onClick = { aberto = true },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ProfileColors.CardBackground,
        border = BorderStroke(1.dp, ProfileColors.Divider)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Language, null, tint = ProfileColors.Primary)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.language), fontWeight = FontWeight.SemiBold, color = ProfileColors.TextPrimary)
                Text(rotuloAtual, color = ProfileColors.TextSecondary)
            }
            Icon(Icons.Outlined.ChevronRight, null, tint = ProfileColors.TextSecondary)
        }
    }

    if (aberto) {
        AlertDialog(
            onDismissRequest = { aberto = false },
            title = { Text(stringResource(R.string.choose_language)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    ModoIdioma.entries.forEach { modo ->
                        Surface(
                            onClick = {
                                onSelecionar(modo)
                                aberto = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = if (modo == atual) ProfileColors.PrimaryLight else ProfileColors.CardBackground
                        ) {
                            Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(idiomaRotulo(modo), Modifier.weight(1f), color = ProfileColors.TextPrimary)
                                if (modo == atual) Icon(Icons.Outlined.Check, null, tint = ProfileColors.PrimaryDark)
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { aberto = false }) { Text(stringResource(R.string.close)) } }
        )
    }
}

@Composable
private fun idiomaRotulo(modo: ModoIdioma): String = when (modo) {
    ModoIdioma.SISTEMA -> stringResource(R.string.language_system)
    ModoIdioma.PORTUGUES -> stringResource(R.string.language_portuguese)
    ModoIdioma.INGLES -> stringResource(R.string.language_english)
}
