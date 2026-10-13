package com.example.lumis.view.screens.profile

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lumis.controller.AuthController
import com.example.lumis.controller.LIMITE_BIOGRAFIA
import com.example.lumis.data.FotoPerfilHelper
import com.example.lumis.data.LocalizacaoHelper
import com.example.lumis.data.User
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.screens.employee_profile.ProfileColors
import kotlinx.coroutines.launch
import java.io.File

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
    var telefone by remember(usuario.id) { mutableStateOf(TextFieldValue(aplicarMascaraTelefone(usuario.telefone))) }
    var biografia by remember(usuario.id) { mutableStateOf(usuario.biografia.take(LIMITE_BIOGRAFIA)) }
    var fotoPath by remember(usuario.id) { mutableStateOf(usuario.fotoPerfilPath) }
    var fotoPreview by remember { mutableStateOf<Uri?>(null) }
    var salvandoFoto by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf("") }
    var salvando by remember { mutableStateOf(false) }
    var buscandoGps by remember { mutableStateOf(false) }
    var msgGps by remember { mutableStateOf<String?>(null) }

    // Galeria via Photo Picker moderno: sem permissão de armazenamento.
    // A imagem escolhida é copiada para o armazenamento interno (ver FotoPerfilHelper).
    val galeriaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        fotoPreview = uri
        salvandoFoto = true
        mensagemErro = ""
        scope.launch {
            try {
                fotoPath = FotoPerfilHelper.salvarFotoInterna(context, uri, usuario.id)
                fotoPreview = null
            } catch (e: Exception) {
                fotoPreview = null
                mensagemErro = e.message ?: "Não consegui salvar a foto."
            } finally {
                salvandoFoto = false
            }
        }
    }

    fun abrirGaleria() {
        mensagemErro = ""
        galeriaLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun removerFoto() {
        FotoPerfilHelper.removerFoto(context, usuario.id)
        fotoPath = ""
        fotoPreview = null
    }

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
        if (!telefoneValido(telefone.text)) {
            mensagemErro = "Digite um telefone válido com DDD"
            return
        }
        salvando = true
        scope.launch {
            val erro = authController.atualizarPerfil(
                nomeUsuario = nome,
                localizacao = localizacao,
                telefone = telefone.text,
                fotoPerfilPath = fotoPath,
                biografia = biografia
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
                        text = "Mantenha nome, telefone, região e foto atualizados.",
                        fontSize = 13.sp,
                        color = ProfileColors.TextSecondary
                    )
                }

                // Card resumo com foto (mesmo padrão da ContaScreen)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = CardShape,
                    colors = CardDefaults.cardColors(containerColor = ProfileColors.CardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar: preview da galeria > foto salva > ícone padrão.
                            // O selo de câmera fica FORA do círculo com clip, como irmão
                            // desenhado depois — assim aparece por cima de tudo, sem ser cortado.
                            val modeloFoto: Any? = fotoPreview
                                ?: fotoPath.takeIf { FotoPerfilHelper.fotoExiste(it) }?.let { File(it) }
                            Box(
                                modifier = Modifier.size(72.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .background(ProfileColors.Divider)
                                        .clickable { abrirGaleria() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (salvandoFoto) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            strokeWidth = 2.dp,
                                            color = ProfileColors.Primary
                                        )
                                    } else if (modeloFoto != null) {
                                        AsyncImage(
                                            model = modeloFoto,
                                            contentDescription = "Foto de perfil",
                                            modifier = Modifier
                                                .size(72.dp)
                                                .clip(CircleShape),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Outlined.Person,
                                            contentDescription = null,
                                            tint = ProfileColors.Primary,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                                // Selo de câmera sobre o avatar (acima de tudo)
                                if (!salvandoFoto) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(ProfileColors.Primary)
                                            .border(
                                                width = 2.dp,
                                                color = ProfileColors.CardBackground,
                                                shape = CircleShape
                                            )
                                            .clickable { abrirGaleria() },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.PhotoCamera,
                                            contentDescription = "Alterar foto",
                                            tint = androidx.compose.ui.graphics.Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
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
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Toque na foto para alterar",
                                    fontSize = 11.sp,
                                    color = ProfileColors.PrimaryDark
                                )
                            }
                        }
                        if (fotoPreview != null || FotoPerfilHelper.fotoExiste(fotoPath)) {
                            TextButton(
                                onClick = ::removerFoto,
                                enabled = !salvandoFoto
                            ) {
                                Text(
                                    text = "Remover foto",
                                    fontSize = 12.sp,
                                    color = ProfileColors.BadgeRedText
                                )
                            }
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

                // Campo telefone (opcional, com máscara automática)
                Column {
                    Text(
                        text = "Telefone (opcional)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ProfileColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = telefone,
                        onValueChange = { telefone = aplicarMascaraTelefoneComCursor(telefone, it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = {
                            Text(
                                text = "(11) 99999-9999",
                                fontSize = 14.sp,
                                color = ProfileColors.TextSecondary
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Call,
                                contentDescription = null,
                                tint = ProfileColors.Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        singleLine = true,
                        shape = FieldShape,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ProfileColors.Background,
                            unfocusedContainerColor = ProfileColors.Background,
                            focusedBorderColor = ProfileColors.Primary,
                            unfocusedBorderColor = ProfileColors.Divider,
                            cursorColor = ProfileColors.Primary
                        )
                    )
                }

                // Campo biografia (opcional, exibida no perfil)
                Column {
                    Text(
                        text = "Biografia (opcional)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ProfileColors.TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = biografia,
                        onValueChange = { if (it.length <= LIMITE_BIOGRAFIA) biografia = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Conte um pouco sobre você...",
                                fontSize = 14.sp,
                                color = ProfileColors.TextSecondary
                            )
                        },
                        minLines = 3,
                        maxLines = 5,
                        shape = FieldShape,
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ProfileColors.Background,
                            unfocusedContainerColor = ProfileColors.Background,
                            focusedBorderColor = ProfileColors.Primary,
                            unfocusedBorderColor = ProfileColors.Divider,
                            cursorColor = ProfileColors.Primary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${biografia.length}/$LIMITE_BIOGRAFIA",
                        fontSize = 11.sp,
                        color = ProfileColors.TextSecondary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
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
