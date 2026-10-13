package com.example.lumis.view.screens.profile

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.controller.AuthController
import com.example.lumis.data.LocalizacaoHelper
import com.example.lumis.data.User
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.screens.employee_profile.ProfileColors
import kotlinx.coroutines.launch

// ---------------------------------------------------------------------------------------------
// TELA EDITAR PERFIL — mesmo estilo dos perfis (ProfileColors, cards 20dp, pill 50, campos 14dp)
// Permite alterar nome de usuário + região onde mora (texto livre ou GPS híbrido).
// O GPS respeita o switch nativo "exata x aproximada" do Android 12+.
// ---------------------------------------------------------------------------------------------

private val CardShape = RoundedCornerShape(20.dp)
private val FieldShape = RoundedCornerShape(14.dp)
private val PillShape = RoundedCornerShape(50)

@Composable
fun EditarPerfilScreen(
    usuario: User,
    authController: AuthController,
    onVoltar: () -> Unit,
    onSalvo: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nome by remember(usuario.id) { mutableStateOf(usuario.nomeUsuario) }
    var localizacao by remember(usuario.id) { mutableStateOf(usuario.localizacao) }
    var mensagemErro by remember { mutableStateOf("") }
    var salvando by remember { mutableStateOf(false) }
    var buscandoGps by remember { mutableStateOf(false) }
    var msgGps by remember { mutableStateOf<String?>(null) }

    // Pede COARSE + FINE juntas: o Android mostra sozinho a opção
    // "Usar localização exata" (FINE) vs aproximada (só COARSE).
    val permissaoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        val temAlguma = (granted[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
            (granted[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
        if (temAlguma) {
            buscandoGps = true
            msgGps = null
            mensagemErro = ""
            scope.launch {
                try {
                    localizacao = LocalizacaoHelper.obterRegiaoAtual(context)
                    msgGps = "Região detectada! Confira e ajuste se precisar."
                } catch (e: Exception) {
                    mensagemErro = e.message ?: "Não foi possível detectar. Digite manualmente."
                } finally {
                    buscandoGps = false
                }
            }
        } else {
            mensagemErro = "Permissão negada. Digite sua região manualmente."
        }
    }

    fun pedirLocalizacaoViaGps() {
        mensagemErro = ""
        msgGps = null
        if (LocalizacaoHelper.temAlgumaPermissao(context)) {
            buscandoGps = true
            scope.launch {
                try {
                    localizacao = LocalizacaoHelper.obterRegiaoAtual(context)
                    msgGps = "Região detectada! Confira e ajuste se precisar."
                } catch (e: Exception) {
                    mensagemErro = e.message ?: "Não foi possível detectar. Digite manualmente."
                } finally {
                    buscandoGps = false
                }
            }
        } else {
            permissaoLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    fun salvar() {
        mensagemErro = ""
        if (nome.isBlank()) {
            mensagemErro = "Digite seu nome de usuário"
            return
        }
        salvando = true
        scope.launch {
            val erro = authController.atualizarPerfil(
                nomeUsuario = nome,
                localizacao = localizacao
            )
            salvando = false
            if (erro != null) {
                mensagemErro = erro
            } else {
                onSalvo()
            }
        }
    }

    Scaffold(
        containerColor = ProfileColors.Background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LumisTopBar(onBack = onVoltar)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título
                Column {
                    Text(
                        text = "Editar perfil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProfileColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Mantenha seu nome e sua região atualizados.",
                        fontSize = 13.sp,
                        color = ProfileColors.TextSecondary
                    )
                }

                // Card resumo (mesmo padrão da ContaScreen)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = CardShape,
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
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(ProfileColors.Divider),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = ProfileColors.Primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = usuario.email,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = ProfileColors.TextPrimary
                            )
                            Text(
                                text = if (usuario.tipoUsuario == "Cliente") "Cliente" else "Profissional",
                                fontSize = 12.sp,
                                color = ProfileColors.TextSecondary
                            )
                        }
                    }
                }

                // Campo nome
                Column {
                    Text(
                        text = "Nome de usuário",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ProfileColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(
                                text = "Seu nome",
                                fontSize = 14.sp,
                                color = ProfileColors.TextSecondary
                            )
                        },
                        singleLine = true,
                        shape = FieldShape,
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ProfileColors.Background,
                            unfocusedContainerColor = ProfileColors.Background,
                            focusedBorderColor = ProfileColors.Primary,
                            unfocusedBorderColor = ProfileColors.Divider,
                            cursorColor = ProfileColors.Primary
                        )
                    )
                }

                // Campo localização
                Column {
                    Text(
                        text = "Onde você mora?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ProfileColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = localizacao,
                        onValueChange = { localizacao = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Ex: São Paulo, SP",
                                fontSize = 14.sp,
                                color = ProfileColors.TextSecondary
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = ProfileColors.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        singleLine = true,
                        maxLines = 1,
                        shape = FieldShape,
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ProfileColors.Background,
                            unfocusedContainerColor = ProfileColors.Background,
                            focusedBorderColor = ProfileColors.Primary,
                            unfocusedBorderColor = ProfileColors.Divider,
                            cursorColor = ProfileColors.Primary
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = ::pedirLocalizacaoViaGps,
                        enabled = !buscandoGps,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = PillShape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = ProfileColors.CardBackground,
                            contentColor = ProfileColors.Primary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ProfileColors.Primary)
                    ) {
                        if (buscandoGps) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = ProfileColors.Primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Detectando...",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.MyLocation,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Usar minha localização atual",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    if (msgGps != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = msgGps!!,
                            fontSize = 12.sp,
                            color = ProfileColors.PrimaryDark
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Você pode digitar ou usar o GPS. O Android pergunta se é exata ou aproximada — aproximada já basta para a cidade.",
                        fontSize = 11.sp,
                        color = ProfileColors.TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (mensagemErro.isNotEmpty()) {
                    Text(
                        text = mensagemErro,
                        color = androidx.compose.ui.graphics.Color.Red,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Salvar
                Button(
                    onClick = ::salvar,
                    enabled = !salvando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = PillShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProfileColors.Primary,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )
                ) {
                    if (salvando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = androidx.compose.ui.graphics.Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Salvando...", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    } else {
                        Text(text = "Salvar alterações", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// PREVIEWS (mesmo padrão das telas de perfil)
// ---------------------------------------------------------------------------------------------

@Preview(name = "Celular - Pixel 4", showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun EditarPerfilPreviewPhone() {
    MaterialTheme {
        // Preview sem AuthController real: usa contexto falso apenas para layout.
        // Em execução real o AuthController vem do MainActivity.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ProfileColors.Background),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Editar perfil — preview de layout", color = ProfileColors.TextSecondary)
        }
    }
}
