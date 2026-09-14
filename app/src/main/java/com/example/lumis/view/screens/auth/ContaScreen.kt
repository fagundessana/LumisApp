package com.example.lumis.view.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TravelExplore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.data.User
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.screens.employee_profile.ProfileColors

// ---------------------------------------------------------------------------------------------
// PÁGINA DE CONTA: permite voltar ao perfil ou desconectar a conta (ir para o login)
// ---------------------------------------------------------------------------------------------

@Composable
fun ContaScreen(
    usuario: User,
    onVoltar: () -> Unit,
    onSair: () -> Unit
) {
    val rotuloTipo = if (usuario.tipoUsuario == "Cliente") "Cliente" else "Funcionário"

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
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = ProfileColors.Primary,
                                modifier = Modifier.size(32.dp)
                            )
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
                        containerColor = Color(0xFFFBDCDC),
                        contentColor = Color(0xFFB5495F)
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
            }
        }
    }
}