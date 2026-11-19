package com.example.lumis.view.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lumis.data.FotoPerfilHelper
import com.example.lumis.data.User
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.screens.employee_profile.ProfileColors
import com.example.lumis.view.theme.ThemeMode
import com.example.lumis.view.theme.ThemeRepository
import kotlinx.coroutines.launch
import java.io.File

// ---------------------------------------------------------------------------------------------
// PÁGINA DE CONTA: permite voltar ao perfil, escolher o tema ou desconectar a conta (ir para o login)
// ---------------------------------------------------------------------------------------------

@Composable
fun ContaScreen(
    usuario: User,
    onVoltar: () -> Unit,
    onEditarPerfil: () -> Unit = {},
    onSair: () -> Unit
) {
    val rotuloTipo = if (usuario.tipoUsuario == "Cliente") "Cliente" else "Funcionário"

    // Preferência de tema (claro / escuro / padrão do sistema)
    val context = LocalContext.current.applicationContext
    val themeRepo = remember { ThemeRepository(context) }
    val themeMode by themeRepo.themeMode.collectAsState(initial = ThemeMode.SYSTEM)
    val scope = rememberCoroutineScope()

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
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Minha conta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ProfileColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Card com dados do usuário logado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = ProfileColors.CardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(ProfileColors.Divider),
                            contentAlignment = Alignment.Center
                        ) {
                            val fotoFile = usuario.fotoPerfilPath
                                .takeIf { FotoPerfilHelper.fotoExiste(it) }
                                ?.let { File(it) }
                            if (fotoFile != null) {
                                AsyncImage(
                                    model = fotoFile,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(64.dp)
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

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = usuario.nomeUsuario,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = ProfileColors.TextPrimary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.MailOutline,
                                    contentDescription = null,
                                    tint = ProfileColors.TextSecondary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = usuario.email,
                                    fontSize = 12.sp,
                                    color = ProfileColors.TextSecondary
                                )
                            }
                            if (usuario.telefone.isNotBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.Call,
                                        contentDescription = null,
                                        tint = ProfileColors.TextSecondary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = usuario.telefone,
                                        fontSize = 12.sp,
                                        color = ProfileColors.TextSecondary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(ProfileColors.PrimaryLight)
                                    .padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Outlined.TravelExplore,
                                        contentDescription = null,
                                        tint = ProfileColors.PrimaryDark,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = rotuloTipo,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = ProfileColors.PrimaryDark
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card da região onde mora
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
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
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(ProfileColors.PrimaryLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = ProfileColors.PrimaryDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sua região",
                                fontSize = 12.sp,
                                color = ProfileColors.TextSecondary
                            )
                            Text(
                                text = usuario.localizacao.ifBlank { "Não definida — toque em editar" },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = ProfileColors.TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Card de aparência (tema claro / escuro / padrão do sistema)
                ThemeSelectorCard(
                    current = themeMode,
                    onSelect = { scope.launch { themeRepo.setThemeMode(it) } }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Botão editar perfil (mesmo padrão pill verde dos perfis)
                Button(
                    onClick = onEditarPerfil,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProfileColors.Primary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Editar perfil",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Ao desconectar, você precisará fazer login novamente\npara acessar o seu perfil.",
                    fontSize = 12.sp,
                    color = ProfileColors.TextSecondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botão de desconectar
                Button(
                    onClick = onSair,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProfileColors.DangerBackground,
                        contentColor = ProfileColors.DangerContent
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Desconectar conta",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// CARD DE APARÊNCIA: escolha entre padrão do sistema, claro e escuro
// ---------------------------------------------------------------------------------------------

@Composable
private fun ThemeSelectorCard(
    current: ThemeMode,
    onSelect: (ThemeMode) -> Unit
) {
    val options = listOf(
        ThemeMode.SYSTEM to "Padrão do sistema",
        ThemeMode.LIGHT to "Claro",
        ThemeMode.DARK to "Escuro",
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ProfileColors.CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .selectableGroup()
        ) {
            Text(
                text = "Aparência",
                fontSize = 12.sp,
                color = ProfileColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))

            options.forEach { (mode, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = current == mode,
                            onClick = { onSelect(mode) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = current == mode,
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = ProfileColors.Primary,
                            unselectedColor = ProfileColors.TextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ProfileColors.TextPrimary
                    )
                }
            }
        }
    }
}