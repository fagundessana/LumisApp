package com.example.lumis.ui.screens

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.R
import com.example.lumis.ui.theme.LumisBackground
import com.example.lumis.ui.theme.LumisBlue
import com.example.lumis.ui.theme.LumisGreen
import com.example.lumis.ui.theme.LumisSurface
import com.example.lumis.ui.theme.LumisTextPrimary
import com.example.lumis.ui.theme.LumisTextSecondary

// =========================================================
// TELA DE SERVIÇOS ADICIONAIS (4ª etapa)
// =========================================================

@Composable
fun ServicosAdicionaisScreen(
    servico: String? = null,
    datahora: String? = null,
    onBack: () -> Unit = {},
    onConfirm: (subtotal: Double, adicionaisEscolhidos: List<String>) -> Unit = { _, _ -> }
) {
    var geladeira by remember { mutableStateOf(true) }
    var forno by remember { mutableStateOf(true) }
    var armarios by remember { mutableStateOf(false) }
    var vidros by remember { mutableStateOf(false) }
    var roupas by remember { mutableStateOf(false) }
    
    val valorBase = 220.00
    var adicionais = 0.0
    val listaEscolhidos = mutableListOf<String>()

    if (geladeira) { adicionais += 40.0; listaEscolhidos.add("Geladeira") }
    if (forno) { adicionais += 35.0; listaEscolhidos.add("Forno") }
    if (armarios) { adicionais += 50.0; listaEscolhidos.add("Armários") }
    if (vidros) { adicionais += 45.0; listaEscolhidos.add("Vidros") }
    if (roupas) { adicionais += 60.0; listaEscolhidos.add("Roupas") }

    val subtotal = valorBase + adicionais

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LumisBackground)
    ) {
        // TopBar com logo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(LumisSurface)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIos,
                        contentDescription = "Voltar",
                        tint = LumisTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(6.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo Lumis",
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text("Lumis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = LumisGreen)
                    servico?.let { Text(text = it, fontSize = 11.sp, color = LumisTextSecondary) }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = null,
                    tint = LumisTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = LumisGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "ADICIONAIS",
                color = LumisBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Serviços adicionais",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LumisTextPrimary
            )
            Spacer(Modifier.height(16.dp))

            AdicionalCard(
                nome = "Limpeza de Geladeira",
                preco = "+ R$ 40,00",
                icon = Icons.Default.Home,
                ativo = geladeira,
                onChange = { geladeira = !geladeira }
            )
            Spacer(Modifier.height(10.dp))
            AdicionalCard(
                nome = "Limpeza de Forno",
                preco = "+ R$ 35,00",
                icon = Icons.Default.Star,
                ativo = forno,
                onChange = { forno = !forno }
            )
            Spacer(Modifier.height(10.dp))
            AdicionalCard(
                nome = "Limpeza Interna de Armários",
                preco = "+ R$ 50,00",
                icon = Icons.Default.Home,
                ativo = armarios,
                onChange = { armarios = !armarios }
            )
            Spacer(Modifier.height(10.dp))
            AdicionalCard(
                nome = "Limpeza de Vidros",
                preco = "+ R$ 45,00",
                icon = Icons.Default.Home,
                ativo = vidros,
                onChange = { vidros = !vidros }
            )
            Spacer(Modifier.height(10.dp))
            AdicionalCard(
                nome = "Passar Roupas",
                preco = "+ R$ 60,00",
                icon = Icons.Default.Star,
                ativo = roupas,
                onChange = { roupas = !roupas }
            )
        }

        // Botão Continuar + Subtotal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LumisSurface)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = { onConfirm(subtotal, listaEscolhidos) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = LumisGreen)
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Total: R$ %.2f".format(subtotal).replace(".", ","),
                fontSize = 13.sp,
                color = LumisTextSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

// =========================================================
// CARD DE ADICIONAL (toggle com switch visual)
// =========================================================

@Composable
private fun AdicionalCard(
    nome: String,
    preco: String,
    icon: ImageVector,
    ativo: Boolean,
    onChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (ativo) LumisGreen.copy(0.08f) else LumisSurface)
            .border(
                width = if (ativo) 1.5.dp else 1.dp,
                color = if (ativo) LumisGreen else Color(0xFFE7E9EC),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onChange)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (ativo) LumisGreen.copy(0.15f) else Color(0xFFF2F2F2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (ativo) LumisGreen else LumisTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(nome, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = LumisTextPrimary)
                Spacer(Modifier.height(4.dp))
                Text(preco, fontSize = 13.sp, color = LumisGreen)
            }
        }
        // Switch custom
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(26.dp)
                .clip(RoundedCornerShape(50))
                .background(if (ativo) LumisGreen else Color(0xFFDDDDDD)),
            contentAlignment = if (ativo) Alignment.CenterEnd else Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}
