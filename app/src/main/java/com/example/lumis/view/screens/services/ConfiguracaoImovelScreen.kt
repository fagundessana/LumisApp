package com.example.lumis.view.screens.services

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.data.CepService
import com.example.lumis.view.components.BotaoContinuar
import com.example.lumis.view.components.LinhaProgresso
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisBlue
import com.example.lumis.view.theme.LumisBorder
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisGreenDark
import com.example.lumis.view.theme.LumisGreenLight
import com.example.lumis.view.theme.LumisSurface
import com.example.lumis.view.theme.LumisTextPrimary
import com.example.lumis.view.theme.LumisTextSecondary
import kotlinx.coroutines.delay
import com.example.lumis.R

@Composable
fun ConfiguracaoImovelScreen(
    servico: String? = null,
    data: String? = null,
    horario: String? = null,
    quartosInicial: Int = 2,
    banheirosInicial: Int = 1,
    tamanhoInicial: String = "60–100m²",
    endereco: String = "",
    onBack: () -> Unit = {},
    onConfirm: (quartos: Int, banheiros: Int, tamanho: String, endereco: String) -> Unit = { _, _, _, _ -> },
    onNotificationsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val mensagemCepInvalido = stringResource(R.string.invalid_cep)
    val mensagemAguardeCep = stringResource(R.string.wait_cep)
    val mensagemEnderecoIncompleto = stringResource(R.string.complete_address)
    var quartos by remember { mutableIntStateOf(quartosInicial) }
    var banheiros by remember { mutableIntStateOf(banheirosInicial) }
    var tamanhoSelecionado by remember { mutableStateOf(tamanhoInicial) }
    var cep by remember { mutableStateOf("") }
    var logradouro by remember { mutableStateOf(endereco) }
    var numero by remember { mutableStateOf("") }
    var complemento by remember { mutableStateOf("") }
    var bairro by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var uf by remember { mutableStateOf("") }
    var buscandoCep by remember { mutableStateOf(false) }
    var erroCep by remember { mutableStateOf<String?>(null) }
    val cepNumerico = cep.filter(Char::isDigit)

    LaunchedEffect(cepNumerico) {
        erroCep = null
        if (cepNumerico.length != 8) return@LaunchedEffect
        delay(350)
        buscandoCep = true
        runCatching { CepService.buscar(cepNumerico) }
            .onSuccess {
                logradouro = it.logradouro
                bairro = it.bairro
                cidade = it.cidade
                uf = it.uf
            }
            .onFailure { erroCep = it.message ?: "Não foi possível consultar o CEP" }
        buscandoCep = false
    }

    Column(Modifier.fillMaxSize().background(LumisBackground)) {
        LumisTopBar(onBack = onBack, onNotificationsClick = onNotificationsClick)
        LinhaProgresso(etapaAtual = 2)

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column {
                Text(stringResource(R.string.property_configuration).uppercase(), color = LumisBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(stringResource(R.string.property_details), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = LumisTextPrimary)
                Text(stringResource(R.string.property_details_hint), fontSize = 13.sp, color = LumisTextSecondary)
            }

            ContadorLinha(stringResource(R.string.bedrooms), quartos, { if (quartos > 1) quartos-- }, { quartos++ })
            ContadorLinha(stringResource(R.string.bathrooms), banheiros, { if (banheiros > 1) banheiros-- }, { banheiros++ })

            Text(stringResource(R.string.approximate_size), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = LumisTextSecondary)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TamanhoPilula("Até 60m²", tamanhoSelecionado == "Até 60m²") { tamanhoSelecionado = "Até 60m²" }
                TamanhoPilula("60–100m²", tamanhoSelecionado == "60–100m²") { tamanhoSelecionado = "60–100m²" }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TamanhoPilula("100–150m²", tamanhoSelecionado == "100–150m²") { tamanhoSelecionado = "100–150m²" }
                TamanhoPilula("Acima de 150m²", tamanhoSelecionado == "Acima de 150m²") { tamanhoSelecionado = "Acima de 150m²" }
            }

            Surface(shape = RoundedCornerShape(20.dp), color = LumisSurface, border = BorderStroke(1.dp, LumisBorder)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(LumisGreenLight), contentAlignment = Alignment.Center) {
                            Icon(Icons.Outlined.LocationOn, null, tint = LumisGreenDark)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(stringResource(R.string.service_address), fontWeight = FontWeight.SemiBold, color = LumisTextPrimary)
                            Text(stringResource(R.string.cep_autofill_hint), fontSize = 12.sp, color = LumisTextSecondary)
                        }
                    }

                    OutlinedTextField(
                        value = cep,
                        onValueChange = { cep = formatarCep(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.cep)) },
                        placeholder = { Text("00000-000") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            when {
                                buscandoCep -> CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                                cepNumerico.length == 8 && erroCep == null -> Icon(Icons.Outlined.CheckCircle, null, tint = LumisGreen)
                            }
                        },
                        supportingText = erroCep?.let { erro -> { Text(erro, color = Color(0xFFB3261E)) } },
                        isError = erroCep != null,
                        singleLine = true
                    )
                    OutlinedTextField(logradouro, { logradouro = it }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.street)) }, singleLine = true)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            numero,
                            { numero = it },
                            Modifier.weight(0.38f),
                            label = { Text(stringResource(R.string.number)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(complemento, { complemento = it }, Modifier.weight(0.62f), label = { Text(stringResource(R.string.complement)) }, singleLine = true)
                    }
                    OutlinedTextField(bairro, { bairro = it }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.neighborhood)) }, singleLine = true)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(cidade, { cidade = it }, Modifier.weight(0.72f), label = { Text(stringResource(R.string.city)) }, singleLine = true)
                        OutlinedTextField(uf, { uf = it.take(2).uppercase() }, Modifier.weight(0.28f), label = { Text(stringResource(R.string.state)) }, singleLine = true)
                    }
                }
            }
        }

        Column(Modifier.fillMaxWidth().background(LumisSurface).padding(horizontal = 20.dp, vertical = 12.dp)) {
            BotaoContinuar(onClick = {
                val enderecoCompleto = montarEndereco(logradouro, numero, complemento, bairro, cidade, uf, cep)
                when {
                    cepNumerico.length != 8 -> Toast.makeText(context, mensagemCepInvalido, Toast.LENGTH_SHORT).show()
                    buscandoCep -> Toast.makeText(context, mensagemAguardeCep, Toast.LENGTH_SHORT).show()
                    erroCep != null -> Toast.makeText(context, erroCep, Toast.LENGTH_SHORT).show()
                    logradouro.isBlank() || numero.isBlank() || bairro.isBlank() || cidade.isBlank() || uf.isBlank() ->
                        Toast.makeText(context, mensagemEnderecoIncompleto, Toast.LENGTH_SHORT).show()
                    else -> onConfirm(quartos, banheiros, tamanhoSelecionado, enderecoCompleto)
                }
            })
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.property_summary, quartos, banheiros, tamanhoSelecionado), fontSize = 12.sp, color = LumisTextSecondary, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

private fun formatarCep(valor: String): String {
    val numeros = valor.filter(Char::isDigit).take(8)
    return if (numeros.length > 5) "${numeros.take(5)}-${numeros.drop(5)}" else numeros
}

private fun montarEndereco(logradouro: String, numero: String, complemento: String, bairro: String, cidade: String, uf: String, cep: String): String =
    buildList {
        add("${logradouro.trim()}, ${numero.trim()}")
        if (complemento.isNotBlank()) add(complemento.trim())
        add(bairro.trim())
        add("${cidade.trim()} - ${uf.trim()}")
        add("CEP ${formatarCep(cep)}")
    }.joinToString(" • ")

@Composable
private fun ContadorLinha(titulo: String, valor: Int, onDiminuir: () -> Unit, onAumentar: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(LumisSurface).border(1.dp, LumisBorder, RoundedCornerShape(14.dp)).padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(titulo, fontSize = 15.sp, color = LumisTextPrimary)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(32.dp).clip(CircleShape).background(LumisBorder).clickable(onClick = onDiminuir), contentAlignment = Alignment.Center) { Text("−", fontSize = 20.sp, color = LumisTextSecondary) }
            Text(valor.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = LumisTextPrimary, modifier = Modifier.width(48.dp), textAlign = TextAlign.Center)
            Box(Modifier.size(32.dp).clip(CircleShape).background(LumisGreen).clickable(onClick = onAumentar), contentAlignment = Alignment.Center) { Text("+", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun TamanhoPilula(texto: String, selecionado: Boolean, onClick: () -> Unit) {
    Box(
        Modifier.clip(RoundedCornerShape(20.dp)).background(if (selecionado) LumisGreen else LumisSurface)
            .border(if (selecionado) 0.dp else 1.dp, LumisBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick).padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = if (selecionado) Color.White else LumisTextPrimary)
    }
}
