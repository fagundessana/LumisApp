package com.example.lumis.ui.placeholder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.ui.theme.LumisColors
import com.example.lumis.ui.theme.Montserrat
import com.example.lumis.ui.theme.Questrial

/**
 * Telas provisórias (Cidade, Explorar, Agenda, Perfil).
 * O grupo deixou elas assim pra navbar funcionar desde já; quando alguém
 * fizer a tela de verdade, é só trocar o conteúdo aqui e apontar no
 * NavHost lá no MainActivity. Nada quebra.
 */
// Tela genérica: título grande + frase "em breve". As 4 telas usam ela.
@Composable
fun PlaceholderScreen(
    title: String,
    subtitle: String = "Em breve aqui entra a tela de $title.",
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LumisColors.Background)
            .padding(contentPadding)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontFamily = Montserrat,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = LumisColors.TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                fontFamily = Questrial,
                fontSize = 14.sp,
                color = LumisColors.SecondaryText
            )
        }
    }
}

@Composable
fun CidadeScreen(padding: PaddingValues) =
    PlaceholderScreen(title = "Cidade", contentPadding = padding)

@Composable
fun ExplorarScreen(padding: PaddingValues) =
    PlaceholderScreen(title = "Explorar", contentPadding = padding)

@Composable
fun AgendaScreen(padding: PaddingValues) =
    PlaceholderScreen(title = "Agenda", contentPadding = padding)

@Composable
fun PerfilScreen(padding: PaddingValues) =
    PlaceholderScreen(title = "Perfil", contentPadding = padding)
