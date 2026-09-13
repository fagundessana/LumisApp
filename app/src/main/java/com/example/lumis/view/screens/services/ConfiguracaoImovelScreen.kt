package com.example.lumis.view.screens.services

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.example.lumis.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.components.BotaoContinuar
import com.example.lumis.view.components.LinhaProgresso
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.theme.*

// =========================================================
// TELA DE CONFIGURAÇÃO DO IMÓVEL (3ª etapa)
// =========================================================

@Composable
fun ConfiguracaoImovelScreen(
    servico: String? = null,
    data: String? = null,
    horario: String? = null,
    onBack: () -> Unit = {},
    onConfirm: (quartos: Int, banheiros: Int, tamanho: String) -> Unit = { _, _, _ -> }
) {
    var quartos by remember { mutableIntStateOf(2) }
    var banheiros by remember { mutableIntStateOf(1) }
    var tamanhoSelecionado by remember { mutableStateOf("60–100m²") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LumisBackground)
    ) {

        // =================================================
        // TOPO
        // =================================================
        LumisTopBar(onBack = onBack)

        LinhaProgresso(etapaAtual = 2)

        // =================================================
        // CONTEÚDO
        // =================================================
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            // SUBTÍTULO
            Text(
                text = "CONFIGURAÇÃO",
                color = LumisBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Detalhes do imóvel",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LumisTextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // QUARTOS
            ContadorLinha(
                titulo = "Quartos",
                valor = quartos,
                onDiminuir = { if (quartos > 1) quartos-- },
                onAumentar = { quartos++ }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // BANHEIROS
            ContadorLinha(
                titulo = "Banheiros",
                valor = banheiros,
                onDiminuir = { if (banheiros > 1) banheiros-- },
                onAumentar = { banheiros++ }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // TAMANHO
            Text(
                text = "Tamanho aproximado",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = LumisTextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TamanhoPilula(
                    texto = "Até 60m²",
                    selecionado = tamanhoSelecionado == "Até 60m²"
                ) { tamanhoSelecionado = "Até 60m²" }

                TamanhoPilula(
                    texto = "60–100m²",
                    selecionado = tamanhoSelecionado == "60–100m²"
                ) { tamanhoSelecionado = "60–100m²" }

                TamanhoPilula(
                    texto = "100–150m²",
                    selecionado = tamanhoSelecionado == "100–150m²"
                ) { tamanhoSelecionado = "100–150m²" }
            }

            Spacer(modifier = Modifier.height(6.dp))

            TamanhoPilula(
                texto = "Acima de 150m²",
                selecionado = tamanhoSelecionado == "Acima de 150m²"
            ) { tamanhoSelecionado = "Acima de 150m²" }
        }

        // =================================================
        // BOTÃO CONTINUAR + SUBTOTAL
        // =================================================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LumisSurface)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            BotaoContinuar(
                onClick = { onConfirm(quartos, banheiros, tamanhoSelecionado) }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Quartos: $quartos | Banheiros: $banheiros | Tamanho: $tamanhoSelecionado",
                fontSize = 12.sp,
                color = LumisTextSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// =========================================================
// LINHA CONTADOR (quartos/banheiros)
// =========================================================

@Composable
private fun ContadorLinha(
    titulo: String,
    valor: Int,
    onDiminuir: () -> Unit,
    onAumentar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(LumisSurface)
            .border(1.dp, Color(0xFFE7E9EC), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = titulo,
            fontSize = 15.sp,
            color = LumisTextPrimary
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Botão −
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE7E9EC))
                    .clickable(onClick = onDiminuir),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "−",
                    fontSize = 20.sp,
                    color = LumisTextSecondary
                )
            }

            // Valor
            Text(
                text = valor.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LumisTextPrimary,
                modifier = Modifier
                    .width(48.dp)
                    .padding(horizontal = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Botão +
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(LumisGreen)
                    .clickable(onClick = onAumentar),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// =========================================================
// BOTÃO DE TAMANHO
// =========================================================

@Composable
private fun TamanhoPilula(
    texto: String,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selecionado) LumisGreen else LumisSurface)
            .border(
                width = if (selecionado) 0.dp else 1.dp,
                color = Color(0xFFE7E9EC),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (selecionado) Color.White else LumisTextPrimary
        )
    }
}
