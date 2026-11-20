package com.example.lumis.view.screens.services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Pix
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisBorder
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisGreenDark
import com.example.lumis.view.theme.LumisGreenLight
import com.example.lumis.view.theme.LumisSurface
import com.example.lumis.view.theme.LumisTextPrimary
import com.example.lumis.view.theme.LumisTextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.example.lumis.R

@Composable
fun PagamentoScreen(
    formaPagamento: String,
    total: Double,
    onBack: () -> Unit,
    onConcluir: () -> Unit,
    onNotificationsClick: () -> Unit = {}
) {
    var titular by remember { mutableStateOf("") }
    var numeroCartao by remember { mutableStateOf("") }
    var validade by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var processando by remember { mutableStateOf(false) }
    var aprovado by remember { mutableStateOf(false) }
    var erro by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val usaCartao = formaPagamento.contains("Cartão", ignoreCase = true)
    val usaPix = formaPagamento.equals("Pix", ignoreCase = true)

    fun simularPagamento() {
        erro = when {
            usaCartao && titular.isBlank() -> "Informe o nome impresso no cartão"
            usaCartao && numeroCartao.filter(Char::isDigit).length < 16 -> "Informe um número de cartão válido para a simulação"
            usaCartao && validade.length < 5 -> "Informe a validade"
            usaCartao && cvv.length < 3 -> "Informe o código de segurança"
            else -> null
        }
        if (erro != null) return
        processando = true
        scope.launch {
            delay(1_000)
            processando = false
            aprovado = true
        }
    }

    Column(Modifier.fillMaxSize().background(LumisBackground)) {
        LumisTopBar(onBack = onBack, onNotificationsClick = onNotificationsClick)
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(stringResource(R.string.payment).uppercase(), color = LumisGreenDark, fontWeight = FontWeight.Bold)
            Text(if (aprovado) "Tudo certo!" else stringResource(R.string.payment_simulation), color = LumisTextPrimary, fontWeight = FontWeight.Bold)
            Text(
                if (aprovado) "A simulação foi aprovada. Agora você pode concluir o agendamento."
                else stringResource(R.string.payment_disclaimer),
                color = LumisTextSecondary
            )

            Surface(shape = RoundedCornerShape(20.dp), color = LumisSurface, border = BorderStroke(1.dp, LumisBorder)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(if (usaPix) Icons.Outlined.Pix else Icons.Outlined.CreditCard, null, tint = LumisGreen)
                        Spacer(Modifier.size(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(formaPagamento, color = LumisTextPrimary, fontWeight = FontWeight.SemiBold)
                            Text("Total do pedido", color = LumisTextSecondary)
                        }
                        Text("R$ ${"%.2f".format(total).replace('.', ',')}", color = LumisGreenDark, fontWeight = FontWeight.Bold)
                    }

                    if (!aprovado && usaCartao) {
                        OutlinedTextField(titular, { titular = it }, Modifier.fillMaxWidth(), label = { Text("Nome no cartão") }, singleLine = true)
                        OutlinedTextField(
                            numeroCartao,
                            { numeroCartao = formatarCartao(it) },
                            Modifier.fillMaxWidth(),
                            label = { Text("Número do cartão de teste") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(validade, { validade = formatarValidade(it) }, Modifier.weight(1f), label = { Text("Validade") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                            OutlinedTextField(cvv, { cvv = it.filter(Char::isDigit).take(4) }, Modifier.weight(1f), label = { Text("CVV") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                        }
                    }

                    if (!aprovado && usaPix) {
                        Box(Modifier.fillMaxWidth().background(LumisGreenLight, RoundedCornerShape(14.dp)).padding(18.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Outlined.Pix, null, Modifier.size(54.dp), tint = LumisGreenDark)
                                Text("PIX-LUMIS-DEMONSTRACAO", color = LumisGreenDark, fontWeight = FontWeight.Bold)
                                Text("Código ilustrativo", color = LumisTextSecondary)
                            }
                        }
                    }

                    if (!aprovado && !usaCartao && !usaPix) {
                        Text("A preferência “$formaPagamento” será registrada no agendamento. O pagamento deverá ser combinado no atendimento.", color = LumisTextSecondary)
                    }

                    if (aprovado) {
                        Column(Modifier.fillMaxWidth().background(LumisGreenLight, RoundedCornerShape(16.dp)).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.CheckCircle, null, Modifier.size(52.dp), tint = LumisGreenDark)
                            Text("Pagamento simulado com sucesso", color = LumisGreenDark, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Lock, null, Modifier.size(18.dp), tint = LumisTextSecondary)
                Spacer(Modifier.size(8.dp))
                Text(stringResource(R.string.demo_environment), color = LumisTextSecondary)
            }
            erro?.let { Text(it, color = Color(0xFFB3261E)) }
        }

        Surface(color = LumisSurface, shadowElevation = 10.dp) {
            Button(
                onClick = { if (aprovado) onConcluir() else simularPagamento() },
                enabled = !processando,
                modifier = Modifier.fillMaxWidth().padding(20.dp).height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LumisGreen)
            ) {
                if (processando) CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                else Text(stringResource(if (aprovado) R.string.finish_appointment else R.string.simulate_payment), fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun formatarCartao(valor: String): String = valor.filter(Char::isDigit).take(16).chunked(4).joinToString(" ")

private fun formatarValidade(valor: String): String {
    val numeros = valor.filter(Char::isDigit).take(4)
    return if (numeros.length > 2) "${numeros.take(2)}/${numeros.drop(2)}" else numeros
}
