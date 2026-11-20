package com.example.lumis.view.screens.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Brush
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
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ModoTema
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
    onSalvo: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nome by remember(usuario.id) { mutableStateOf(usuario.nomeUsuario) }
    var localizacao by remember(usuario.id) { mutableStateOf(usuario.localizacao) }
    var telefone by remember(usuario.id) { mutableStateOf(TextFieldValue(aplicarMascaraTelefone(usuario.telefone))) }
    var biografia by remember(usuario.id) { mutableStateOf(usuario.biografia.take(LIMITE_BIOGRAFIA)) }
    var especialidades by remember(usuario.id) { mutableStateOf(usuario.especialidades) }
    var regiaoAtuacao by remember(usuario.id) { mutableStateOf(usuario.regiaoAtuacao) }
    var experiencia by remember(usuario.id) { mutableStateOf(usuario.experiencia) }
    var valorMinimo by remember(usuario.id) {
        mutableStateOf(if (usuario.valorMinimoCentavos > 0) "%.2f".format(usuario.valorMinimoCentavos / 100.0).replace('.', ',') else "")
    }
    var fotoPath by remember(usuario.id) { mutableStateOf(usuario.fotoPerfilPath) }
    var fotoPreview by remember { mutableStateOf<Uri?>(null) }
    var salvandoFoto by remember { mutableStateOf(false) }

    // Banner fields
    var bannerPath by remember(usuario.id) { mutableStateOf(usuario.bannerPerfilPath) }
    var bannerPreview by remember { mutableStateOf<Uri?>(null) }
    var salvandoBanner by remember { mutableStateOf(false) }
    // Arquivo que está sendo enquadrado no editor de banner (null = editor fechado)
    var arquivoParaRecortar by remember { mutableStateOf<File?>(null) }

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

    // Galeria para banner
    val galeriaBannerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        mensagemErro = ""
        scope.launch {
            try {
                arquivoParaRecortar = FotoPerfilHelper.copiarOriginalBanner(context, uri, usuario.id)
            } catch (e: Exception) {
                mensagemErro = e.message ?: "Não consegui abrir a imagem."
            }
        }
    }

    fun abrirGaleria() {
        mensagemErro = ""
        galeriaLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun abrirGaleriaBanner() {
        mensagemErro = ""
        galeriaBannerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun removerFoto() {
        FotoPerfilHelper.removerFoto(context, usuario.id)
        fotoPath = ""
        fotoPreview = null
    }

    fun removerBanner() {
        FotoPerfilHelper.removerFoto(context, usuario.id, "banner")
        bannerPath = ""
        bannerPreview = null
    }

    // Camera launcher para tirar foto de perfil
    var fotoCameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && fotoCameraUri != null) {
            fotoPreview = fotoCameraUri
            salvandoFoto = true
            mensagemErro = ""
            scope.launch {
                try {
                    fotoPath = FotoPerfilHelper.salvarFotoInterna(context, fotoCameraUri!!, usuario.id)
                    fotoPreview = null
                    fotoCameraUri = null
                } catch (e: Exception) {
                    fotoPreview = null
                    fotoCameraUri = null
                    mensagemErro = e.message ?: "Não consegui salvar a foto."
                } finally {
                    salvandoFoto = false
                }
            }
        }
    }

    // Camera launcher para banner
    var bannerCameraUri by remember { mutableStateOf<Uri?>(null) }
    val cameraBannerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && bannerCameraUri != null) {
            mensagemErro = ""
            scope.launch {
                try {
                    arquivoParaRecortar = FotoPerfilHelper.promoverBannerTempParaOriginal(context, usuario.id)
                } catch (e: Exception) {
                    mensagemErro = e.message ?: "Não consegui salvar a foto da câmera."
                } finally {
                    bannerCameraUri = null
                }
            }
        }
    }

    fun abrirCamera() {
        mensagemErro = ""
        val arquivoFoto = FotoPerfilHelper.arquivoFoto(context, usuario.id)
        fotoCameraUri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            arquivoFoto
        )
        cameraLauncher.launch(fotoCameraUri)
    }

    fun abrirCameraBanner() {
        mensagemErro = ""
        val arquivoBanner = FotoPerfilHelper.arquivoBannerTemp(context, usuario.id)
        bannerCameraUri = androidx.core.content.FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            arquivoBanner
        )
        cameraBannerLauncher.launch(bannerCameraUri)
    }

    // Permissão de câmera
    val permissaoCameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted: Boolean ->
        if (granted) {
            // Se a permissão foi concedida, abre a câmera
            abrirCamera()
        } else {
            mensagemErro = "Permissão de câmera negada. Não é possível tirar foto."
        }
    }

    // Permissão de câmera para banner
    val permissaoCameraLauncherWrapper = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted: Boolean ->
        if (granted) {
            abrirCameraBanner()
        } else {
            mensagemErro = "Permissão de câmera negada. Não é possível tirar foto do banner."
        }
    }

    // State para controlar o dialog de opções de foto
    var mostrarDialogoFoto by remember { mutableStateOf(false) }

    @Composable
    fun DialogOpcoesFoto() {
        if (mostrarDialogoFoto) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { mostrarDialogoFoto = false },
                title = { Text(text = "Alterar foto", fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                abrirGaleria()
                                mostrarDialogoFoto = false
                            }.padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Outlined.PhotoLibrary, contentDescription = null, tint = ProfileColors.Primary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Escolher da galeria", fontSize = 16.sp, color = ProfileColors.TextPrimary)
                        }
                        Divider(color = ProfileColors.Divider)
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                // Verifica permissão de câmera
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                        abrirCamera()
                                    } else {
                                        permissaoCameraLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                } else {
                                    abrirCamera()
                                }
                                mostrarDialogoFoto = false
                            }.padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Outlined.Camera, contentDescription = null, tint = ProfileColors.Primary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Tirar foto", fontSize = 16.sp, color = ProfileColors.TextPrimary)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { mostrarDialogoFoto = false }) { Text(text = "Cancelar", color = ProfileColors.Primary) }
                }
            )
        }
    }

    // State para controlar o dialog de opções de banner
    var mostrarDialogoBanner by remember { mutableStateOf(false) }

    @Composable
    fun DialogOpcoesBanner() {
        if (mostrarDialogoBanner) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { mostrarDialogoBanner = false },
                title = { Text(text = "Alterar banner", fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (FotoPerfilHelper.fotoExiste(bannerPath)) {
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable {
                                    arquivoParaRecortar = FotoPerfilHelper.arquivoParaAjuste(context, usuario.id, bannerPath)
                                    mostrarDialogoBanner = false
                                }.padding(vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Outlined.Crop, contentDescription = null, tint = ProfileColors.Primary, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "Ajustar enquadramento", fontSize = 16.sp, color = ProfileColors.TextPrimary)
                            }
                            androidx.compose.material3.Divider(color = ProfileColors.Divider)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                abrirGaleriaBanner()
                                mostrarDialogoBanner = false
                            }.padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Outlined.PhotoLibrary, contentDescription = null, tint = ProfileColors.Primary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Escolher da galeria", fontSize = 16.sp, color = ProfileColors.TextPrimary)
                        }
                        androidx.compose.material3.Divider(color = ProfileColors.Divider)
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                // Verifica permissão de câmera
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                        abrirCameraBanner()
                                    } else {
                                        permissaoCameraLauncherWrapper.launch(Manifest.permission.CAMERA)
                                    }
                                } else {
                                    abrirCameraBanner()
                                }
                                mostrarDialogoBanner = false
                            }.padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Outlined.Camera, contentDescription = null, tint = ProfileColors.Primary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = "Tirar foto", fontSize = 16.sp, color = ProfileColors.TextPrimary)
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { mostrarDialogoBanner = false }) { Text(text = "Cancelar", color = ProfileColors.Primary) }
                }
            )
        }
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
                bannerPerfilPath = bannerPath,
                biografia = biografia,
                especialidades = especialidades,
                regiaoAtuacao = regiaoAtuacao,
                experiencia = experiencia,
                valorMinimoCentavos = ((valorMinimo.replace(',', '.').toDoubleOrNull() ?: 0.0) * 100).toLong()
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
            LumisTopBar(onBack = onVoltar, onNotificationsClick = onNotificationsClick)
            DialogOpcoesFoto()
            DialogOpcoesBanner()
            arquivoParaRecortar?.let { arquivo ->
                BannerCropDialog(
                    arquivo = arquivo,
                    onCancelar = { arquivoParaRecortar = null },
                    onConfirmar = { recorte ->
                        arquivoParaRecortar = null
                        salvandoBanner = true
                        mensagemErro = ""
                        scope.launch {
                            try {
                                bannerPath = FotoPerfilHelper.salvarBannerRecortado(context, recorte, usuario.id)
                            } catch (e: Exception) {
                                mensagemErro = e.message ?: "Não consegui salvar o banner."
                            } finally {
                                salvandoBanner = false
                            }
                        }
                    }
                )
            }
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
                                            .clickable { mostrarDialogoFoto = true },
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

                // Banner de capa
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Banner de capa",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = ProfileColors.TextPrimary
                        )
                        if (bannerPreview != null || FotoPerfilHelper.fotoExiste(bannerPath)) {
                            TextButton(
                                onClick = ::removerBanner,
                                enabled = !salvandoBanner
                            ) {
                                Text(
                                    text = "Remover banner",
                                    fontSize = 12.sp,
                                    color = ProfileColors.BadgeRedText
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(BANNER_ASPECTO)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { mostrarDialogoBanner = true },
                        contentAlignment = Alignment.Center
                    ) {
                        val modeloBanner: Any? = bannerPreview
                            ?: bannerPath.takeIf { FotoPerfilHelper.fotoExiste(it) }?.let { File(it) }
                        if (salvandoBanner) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = ProfileColors.Primary
                            )
                        } else if (modeloBanner != null) {
                            AsyncImage(
                                model = modeloBanner,
                                contentDescription = "Banner de capa",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                ProfileColors.PrimaryLight.copy(alpha = 0.3f),
                                                ProfileColors.Primary.copy(alpha = 0.1f)
                                            )
                                        )
                                    )
                                    .clip(RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Adicionar banner",
                                    tint = ProfileColors.Primary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Toque para adicionar banner",
                                    fontSize = 12.sp,
                                    color = ProfileColors.TextSecondary
                                )
                            }
                        }
                    }
                    if (bannerPreview != null || FotoPerfilHelper.fotoExiste(bannerPath)) {
                        TextButton(
                            onClick = ::removerBanner,
                            enabled = !salvandoBanner
                        ) {
                            Text(
                                text = "Remover banner",
                                fontSize = 12.sp,
                                color = ProfileColors.BadgeRedText
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
                if (usuario.tipoUsuario != "Cliente") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = CardShape,
                        colors = CardDefaults.cardColors(containerColor = ProfileColors.CardBackground)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Informações profissionais", fontWeight = FontWeight.Bold, color = ProfileColors.TextPrimary)
                            Text("Esses dados aparecem no seu perfil e substituem informações demonstrativas.", fontSize = 12.sp, color = ProfileColors.TextSecondary)
                            OutlinedTextField(
                                value = especialidades,
                                onValueChange = { especialidades = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Especialidades") },
                                placeholder = { Text("Ex.: residencial, pós-obra, Airbnb") },
                                minLines = 2,
                                shape = FieldShape
                            )
                            OutlinedTextField(
                                value = regiaoAtuacao,
                                onValueChange = { regiaoAtuacao = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Região de atendimento") },
                                placeholder = { Text("Ex.: Curitiba e região") },
                                singleLine = true,
                                shape = FieldShape
                            )
                            OutlinedTextField(
                                value = experiencia,
                                onValueChange = { experiencia = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Experiência") },
                                placeholder = { Text("Conte sua experiência profissional") },
                                minLines = 2,
                                shape = FieldShape
                            )
                            OutlinedTextField(
                                value = valorMinimo,
                                onValueChange = { valorMinimo = it.filter { caractere -> caractere.isDigit() || caractere == ',' || caractere == '.' } },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Valor mínimo por serviço") },
                                prefix = { Text("R$ ") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                shape = FieldShape
                            )
                        }
                    }
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
    LumisTheme(modo = ModoTema.SISTEMA) {
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
