package com.example.lumis.view.screens.services

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.R
import com.example.lumis.view.components.BotaoContinuar
import com.example.lumis.view.components.LinhaProgresso
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.theme.*

// ============================================================
// TELA DE RESUMO DO PEDIDO (5ª etapa)
// ============================================================

@Composable
fun ResumoPedidoScreen(
    servico: String,
    data: String,
    horario: String,
    quartos: Int,
    banheiros: Int,
    tamanho: String,
    extras: List<String>,
    total: Double,
    onConfirm: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LumisBackground)
    ) {
        // =============================
        // TOPO
        // =============================
        LumisTopBar(onBack = onBack)

        LinhaProgresso(etapaAtual = 4)

        // =============================
        // CONTEÚDO
        // =============================
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {

            // Categoria
            Text(
                text = "FINALIZAR",
                color = LumisBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))

            // Título
            Text(
                text = "Resumo do pedido",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = LumisTextPrimary
            )
            Spacer(Modifier.height(20.dp))

            // ============ CARD DETALHES ============
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LumisSurface),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Detalhes do Serviço",
                        color = LumisBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))

                    RowBetween("Tipo de Limpeza", servico)
                    RowBetween("Data", data)
                    RowBetween("Horário", horario)
                    RowBetween("Quartos / Banheiros", "$quartos | $banheiros")
                    RowBetween("Tamanho", tamanho)
                    RowBetween("Adicionais", extras.joinToString(", "))
                }
            }

            Spacer(Modifier.height(16.dp))

            // ============ CARD PREÇO ============
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF2F8)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Detalhamento de Preço",
                        color = LumisBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))

                    RowBetween("Limpeza $servico", "R$ 220,00")
                    extras.forEach { extra ->
                        RowBetween(
                            nome = "Adicional: $extra",
                            valor = "+ R$ "
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Geral",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumisTextPrimary
                        )
                        Text(
                            text = "R$ ${"%.2f".format(total).replace(".", ",", true)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = LumisGreen
                        )
                    }
                }
            }
        }

        // =============================
        // BOTÃO CONFIRMAR
        // =============================
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LumisSurface)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            BotaoContinuar(
                onClick = {
                    Toast.makeText(context, "Pedido confirmado! ✅", Toast.LENGTH_LONG).show()
                    onConfirm()
                },
                texto = "Confirmar Agendamento"
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Cancelamento gratuito até 24h antes",
                fontSize = 11.sp,
                color = LumisTextSecondary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

// ============================================================
// COMPONENTES AUXILIARES
// ============================================================

@Composable
private fun RowBetween(nome: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = nome,
            fontSize = 13.sp,
            color = LumisTextSecondary
        )
        Text(
            text = valor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = LumisTextPrimary
        )
    }
}
