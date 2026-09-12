package com.example.lumis.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.R
import com.example.lumis.controller.ServicesController
import com.example.lumis.model.Servico
import com.example.lumis.view.theme.*

// ---------------------------------------------------------
// TELA PRINCIPAL (SERVIÇOS)
// ---------------------------------------------------------

@Composable
fun ServicesScreen() {
    val controller = remember { ServicesController() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LumisBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LumisBackground)
        ) {
            Topo()

            Abas()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE0E0E0))
                ) {
                    Box(
                        modifier = Modifier
                            .width(52.dp)
                            .fillMaxSize()
                            .background(LumisTeal)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "CATEGORIA",
                    color = LumisBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Qual tipo de limpeza você\nprecisa?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(controller.servicos) { servico ->
                        CardServico(
                            servico = servico,
                            selecionado = controller.servicoSelecionado == servico.nome,
                            onClick = {
                                controller.selecionarServico(servico)
                            }
                        )
                    }
                }
            }

            Button(
                onClick = { controller.continuar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LumisGreenVivid
                )
            ) {
                Text(
                    text = "Continuar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            MenuInferior()
        }
    }
}

// ---------------------------------------------------------
// TOPO DA TELA
// ---------------------------------------------------------

@Composable
private fun Topo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(LumisSurface)
            .statusBarsPadding()
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.lumis),
                contentDescription = "Logo Lumis",
                modifier = Modifier.size(32.dp)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEAF0F4)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notificações",
                    tint = LumisBlue,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEAF5EF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = LumisGreenVivid,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ---------------------------------------------------------
// ABAS
// ---------------------------------------------------------

@Composable
private fun Abas() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 10.dp)
            .height(29.dp)
            .clip(RoundedCornerShape(7.dp))
            .background(Color(0xFFE7E7E7))
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .clip(RoundedCornerShape(7.dp))
                .background(LumisGreenVivid),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Serviços",
                color = Color.White,
                fontSize = 11.sp
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Planos",
                color = LumisGreySecondary,
                fontSize = 11.sp
            )
        }
    }
}

// ---------------------------------------------------------
// CARD DE SERVIÇO
// ---------------------------------------------------------

@Composable
private fun CardServico(
    servico: Servico,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    val corBorda = if (selecionado) LumisGreenVivid else Color(0xFFD5D5D5)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(LumisSurface)
            .border(width = 1.dp, color = corBorda, shape = RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = servico.nome,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Box(
                modifier = Modifier
                    .size(17.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        color = if (selecionado) LumisGreenVivid else Color(0xFF999999),
                        shape = CircleShape
                    )
                    .background(if (selecionado) LumisGreenVivid else Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (selecionado) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selecionado",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = servico.descricao,
            fontSize = 9.sp,
            lineHeight = 12.sp,
            color = LumisGreySecondary
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = servico.preco,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = LumisBlue
        )
    }
}

// ---------------------------------------------------------
// MENU INFERIOR
// ---------------------------------------------------------

@Composable
private fun MenuInferior() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LumisSurface)
            .navigationBarsPadding()
            .height(57.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemMenu(icone = Icons.Default.Home, texto = "Home", selecionado = false)
        ItemMenu(icone = Icons.Default.Business, texto = "Cidade", selecionado = false)
        ItemMenu(icone = Icons.Default.Explore, texto = "Explorar", selecionado = true)
        ItemMenu(icone = Icons.Default.CalendarMonth, texto = "Agenda", selecionado = false)
        ItemMenu(icone = Icons.Default.Person, texto = "Perfil", selecionado = false)
    }
}

// ---------------------------------------------------------
// ITEM DO MENU
// ---------------------------------------------------------

@Composable
private fun ItemMenu(
    icone: ImageVector,
    texto: String,
    selecionado: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icone,
            contentDescription = texto,
            modifier = Modifier.size(20.dp),
            tint = if (selecionado) Color(0xFF777777) else Color(0xFF888888)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = texto,
            fontSize = 8.sp,
            color = if (selecionado) Color(0xFF555555) else Color(0xFF777777)
        )
    }
}