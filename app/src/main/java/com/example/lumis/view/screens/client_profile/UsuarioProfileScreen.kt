package com.example.lumis.view.screens.client_profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.R
import com.example.lumis.view.components.lembreteEmBreve
// Reaproveita a paleta de cores já definida na tela do funcionário,
// mantendo os dois perfis (funcionário / usuário) com a mesma identidade visual.
import com.example.lumis.view.screens.employee_profile.ProfileColors

// ---------------------------------------------------------------------------------------------
// ---------------------------------------------------------------------------------------------

private val CardShape = RoundedCornerShape(20.dp)
private val SmallCardShape = RoundedCornerShape(16.dp)
private val PillShape = RoundedCornerShape(50)

// Gradiente verde usado no header do perfil de usuário (equivalente ao azul do funcionário)
private val HeaderGreenTop = Color(0xFFA9E2C4)
private val HeaderGreenBottom = Color(0xFFE9F8F0)

// ---------------------------------------------------------------------------------------------
// MODELOS DE DADOS (MOCK)
// ---------------------------------------------------------------------------------------------

data class UsuarioProfile(
    val nome: String = "Maria de Oliveira",
    val localizacao: String = "São Paulo, Brasil",
    val fotoUrl: String? = null
)

enum class StatusAtividade(val label: String) {
    CONCLUIDO("Concluído"),
    CANCELADO("Cancelado")
}

data class AtividadeRecente(
    val titulo: String,
    val data: String,
    val status: StatusAtividade = StatusAtividade.CONCLUIDO
)

data class TarefaPendente(
    val titulo: String,
    val prazo: String,
    val icone: ImageVector
)

data class Atalho(
    val label: String,
    val icone: ImageVector
)

// Dados mockados equivalentes ao print de referência
val atalhosPadrao = listOf(
    Atalho("Meus dados", Icons.Outlined.Description),
    Atalho("Favoritos", Icons.Outlined.FavoriteBorder),
    Atalho("Pagamentos", Icons.Outlined.CreditCard),
    Atalho("Notificações", Icons.Outlined.NotificationsNone)
)

val atividadesRecentesPadrao = listOf(
    AtividadeRecente("Limpeza Residencial", "26/08/2026"),
    AtividadeRecente("Limpeza Residencial", "05/08/2026"),
    AtividadeRecente("Limpeza de estofados", "17/07/2026"),
    AtividadeRecente("Limpeza de carro", "08/06/2026")
)

val tarefasPendentesPadrao = listOf(
    TarefaPendente("Coleta de recicláveis", "2 Dias para completar", Icons.Outlined.Autorenew),
    TarefaPendente("Reporte de acúmulo de lixo", "2 Dias para completar", Icons.Outlined.DeleteOutline)
)

// ---------------------------------------------------------------------------------------------
// TELA PRINCIPAL
// ---------------------------------------------------------------------------------------------

@Composable
fun UsuarioProfileScreen(
    profile: UsuarioProfile = UsuarioProfile(),
    atalhos: List<Atalho> = atalhosPadrao,
    atividadesRecentes: List<AtividadeRecente> = atividadesRecentesPadrao,
    tarefasPendentes: List<TarefaPendente> = tarefasPendentesPadrao,
    onAtalhoClick: (Atalho) -> Unit = {},
    onAtividadeClick: (AtividadeRecente) -> Unit = {},
    onConcluirTarefa: (TarefaPendente) -> Unit = {},
    onEditarClick: () -> Unit = {},
    onMembroLumisClick: () -> Unit = {},
    onConfigClick: () -> Unit = {}
) {
    // Ações ainda não implementadas mostram o aviso "Em breve"
    val emBreve = lembreteEmBreve()

    Scaffold(
        containerColor = ProfileColors.CardBackground
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isWideScreen = maxWidth > 600.dp
            val contentMaxWidth = if (isWideScreen) 460.dp else maxWidth
            val horizontalPadding = if (isWideScreen) (maxWidth - contentMaxWidth) / 2 else 0.dp

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    UsuarioHeaderSection(
                        profile = profile,
                        horizontalPadding = horizontalPadding,
                        onEditarClick = { emBreve() },
                        onConfigClick = onConfigClick
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ProfileColors.CardBackground)
                            .padding(horizontal = horizontalPadding + 20.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))

                        MembroLumisCard(onClick = { emBreve() })

                        AtalhosRapidosSection(
                            atalhos = atalhos,
                            onAtalhoClick = { emBreve() }
                        )

                        AtividadesRecentesSection(
                            atividades = atividadesRecentes,
                            onAtividadeClick = { emBreve() }
                        )

                        TarefasPendentesSection(
                            tarefas = tarefasPendentes,
                            onConcluirTarefa = { emBreve() }
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// 1. HEADER (fundo verde + avatar sobreposto + nome/localização)
// ---------------------------------------------------------------------------------------------

@Composable
fun UsuarioHeaderSection(
    profile: UsuarioProfile,
    horizontalPadding: Dp,
    onEditarClick: () -> Unit,
    onConfigClick: () -> Unit
) {
    val emBreve = lembreteEmBreve()

    Box(modifier = Modifier.fillMaxWidth()) {
        // Fundo verde gradiente
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(HeaderGreenTop, HeaderGreenBottom)
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding + 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo Lumis
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ProfileColors.Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.lumis),
                        contentDescription = "Logo Lumis",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Botões: configurações + alterar foto de capa (lado a lado)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.7f))
                            .clickable { onConfigClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Configurações",
                            tint = ProfileColors.TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.7f))
                            .clickable { emBreve() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PhotoCamera,
                            contentDescription = "Alterar capa",
                            tint = ProfileColors.TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Container branco com cantos superiores arredondados
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 110.dp)
                .background(
                    color = ProfileColors.CardBackground,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = horizontalPadding + 20.dp, end = horizontalPadding + 20.dp, top = 18.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar circular sobrepondo a transição verde/branca
                Box(
                    modifier = Modifier
                        .offset(y = (-46).dp)
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(3.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(ProfileColors.Divider),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Foto do usuário",
                            tint = ProfileColors.Primary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Nome e localização (alinhados ao centro do avatar)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = (-30).dp)
                ) {
                    Text(
                        text = profile.nome,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = ProfileColors.TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = ProfileColors.TextSecondary,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = profile.localizacao,
                            fontSize = 12.sp,
                            color = ProfileColors.TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Botão editar
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .offset(y = (-30).dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onEditarClick() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Editar perfil",
                        tint = ProfileColors.Primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Editar",
                        fontSize = 10.sp,
                        color = ProfileColors.Primary
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// 2. CARD "MEMBRO LUMIS"
// ---------------------------------------------------------------------------------------------

@Composable
fun MembroLumisCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = CardShape,
        colors = CardDefaults.cardColors(containerColor = ProfileColors.PrimaryLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.WorkspacePremium,
                contentDescription = null,
                tint = ProfileColors.PrimaryDark,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Membro Lumis",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = ProfileColors.PrimaryDark
                )
                Text(
                    text = "Clique aqui e descubra seus benefícios",
                    fontSize = 12.sp,
                    color = ProfileColors.TextSecondary
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = ProfileColors.PrimaryDark,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ---------------------------------------------------------------------------------------------
// 3. ATALHOS RÁPIDOS
// ---------------------------------------------------------------------------------------------

@Composable
fun AtalhosRapidosSection(
    atalhos: List<Atalho>,
    onAtalhoClick: (Atalho) -> Unit
) {
    Column {
        SectionTitle(prefixo = "Atalhos", destaque = "rápidos")
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            atalhos.forEach { atalho ->
                AtalhoItem(
                    atalho = atalho,
                    modifier = Modifier.weight(1f),
                    onClick = { onAtalhoClick(atalho) }
                )
            }
        }
    }
}

@Composable
private fun AtalhoItem(
    atalho: Atalho,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(SmallCardShape)
            .background(ProfileColors.CardBackground)
            .border(BorderStroke(1.dp, ProfileColors.Divider), SmallCardShape)
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = atalho.icone,
            contentDescription = atalho.label,
            tint = ProfileColors.Primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = atalho.label,
            fontSize = 11.sp,
            color = ProfileColors.TextPrimary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ---------------------------------------------------------------------------------------------
// 4. ATIVIDADES RECENTES
// ---------------------------------------------------------------------------------------------

@Composable
fun AtividadesRecentesSection(
    atividades: List<AtividadeRecente>,
    onAtividadeClick: (AtividadeRecente) -> Unit
) {
    Column {
        SectionTitle(prefixo = "Atividades", destaque = "recentes")
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = CardShape,
            border = BorderStroke(1.dp, ProfileColors.Divider),
            colors = CardDefaults.cardColors(containerColor = ProfileColors.CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                atividades.forEachIndexed { index, atividade ->
                    AtividadeRow(
                        atividade = atividade,
                        onClick = { onAtividadeClick(atividade) }
                    )
                    if (index != atividades.lastIndex) {
                        HorizontalDivider(color = ProfileColors.Divider, thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun AtividadeRow(
    atividade: AtividadeRecente,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = null,
            tint = ProfileColors.ChipGreenIcon,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = atividade.titulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = ProfileColors.TextPrimary,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = atividade.data,
                fontSize = 12.sp,
                color = ProfileColors.TextSecondary
            )
            Text(
                text = atividade.status.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (atividade.status == StatusAtividade.CONCLUIDO)
                    ProfileColors.BadgeGreenText else ProfileColors.BadgeRedText
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = ProfileColors.TextSecondary,
            modifier = Modifier.size(18.dp)
        )
    }
}

// ---------------------------------------------------------------------------------------------
// 5. TAREFAS PENDENTES
// ---------------------------------------------------------------------------------------------

@Composable
fun TarefasPendentesSection(
    tarefas: List<TarefaPendente>,
    onConcluirTarefa: (TarefaPendente) -> Unit
) {
    Column {
        SectionTitle(prefixo = "Tarefas", destaque = "Pendentes")
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = CardShape,
            border = BorderStroke(1.dp, ProfileColors.Divider),
            colors = CardDefaults.cardColors(containerColor = ProfileColors.CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                tarefas.forEachIndexed { index, tarefa ->
                    TarefaRow(
                        tarefa = tarefa,
                        onConcluir = { onConcluirTarefa(tarefa) }
                    )
                    if (index != tarefas.lastIndex) {
                        HorizontalDivider(color = ProfileColors.Divider, thickness = 1.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun TarefaRow(
    tarefa: TarefaPendente,
    onConcluir: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = tarefa.icone,
            contentDescription = null,
            tint = ProfileColors.Primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tarefa.titulo,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = ProfileColors.TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = tarefa.prazo,
                fontSize = 11.sp,
                color = ProfileColors.TextSecondary
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onConcluir,
            shape = PillShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = ProfileColors.Primary,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(text = "Concluir", fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Outlined.PhotoCamera,
                contentDescription = "Anexar foto",
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

// ---------------------------------------------------------------------------------------------
// COMPONENTE AUXILIAR: TÍTULO DE SEÇÃO (ex.: "Atalhos rápidos")
// ---------------------------------------------------------------------------------------------

@Composable
private fun SectionTitle(prefixo: String, destaque: String) {
    Row {
        Text(
            text = "$prefixo ",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = ProfileColors.TextPrimary
        )
        Text(
            text = destaque,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = ProfileColors.Primary
        )
    }
}

// ---------------------------------------------------------------------------------------------
// PREVIEWS
// ---------------------------------------------------------------------------------------------

@Preview(name = "Celular - Pixel 4", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun UsuarioProfileScreenPreviewPhone() {
    MaterialTheme {
        UsuarioProfileScreen()
    }
}

@Preview(name = "Celular pequeno", showBackground = true, widthDp = 320, heightDp = 780)
@Composable
fun UsuarioProfileScreenPreviewSmallPhone() {
    MaterialTheme {
        UsuarioProfileScreen()
    }
}

@Preview(name = "Tablet", showBackground = true, device = Devices.PIXEL_TABLET)
@Composable
fun UsuarioProfileScreenPreviewTablet() {
    MaterialTheme {
        UsuarioProfileScreen()
    }
}
