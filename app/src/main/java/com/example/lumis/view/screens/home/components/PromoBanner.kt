// Banner da promoção "30% OFF" - o cartão grande verde do topo da Home.
// Como funciona: foto no fundo + sombra preta por cima (pra ler o texto)
// + etiqueta amarela + título + botão. Feito com um Box empilhando tudo.

package com.example.lumis.view.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lumis.R
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.Montserrat
import com.example.lumis.view.theme.Questrial

/**
 * Banner promocional. A gente usa o Coil pra carregar a foto: se um dia
 * vier uma URL da internet, passa no imageUrl; se não, ele mostra a
 * imagem placeholder que tá salva no drawable. Não precisa mexer aqui.
 */
@Composable
fun PromoBanner(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    onDiscountClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(168.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        // Foto de fundo (Coil com imagem reserva se der erro).
        AsyncImage(
            model = imageUrl ?: R.drawable.placeholder_hero,
            contentDescription = "Promoção de limpeza",
            placeholder = painterResource(R.drawable.placeholder_hero),
            error = painterResource(R.drawable.placeholder_hero),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Sombra preta em degradê por cima da foto (senão o texto branco some).
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.13f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Etiqueta amarela "Oferta por tempo limitado!".
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = LumisColors.BadgeYellow
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bolt,
                        contentDescription = null,
                        tint = LumisColors.OnBadgeYellow,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Oferta por tempo limitado!",
                        fontFamily = Questrial,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumisColors.OnBadgeYellow
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Título em 3 cores (branco + verde + branco), por isso o buildAnnotatedString.
            val titleStyleWhite =
                SpanStyle(color = LumisColors.White, fontFamily = Montserrat, fontWeight = FontWeight.ExtraBold)
            val titleStyleGreen =
                SpanStyle(color = LumisColors.Primary, fontFamily = Montserrat, fontWeight = FontWeight.ExtraBold)
            Text(
                text = buildAnnotatedString {
                    withStyle(titleStyleWhite) {
                        append("Primeira limpeza\n")
                    }
                    withStyle(titleStyleGreen) {
                        append("30% OFF ")
                    }
                    withStyle(titleStyleWhite) {
                        append("para novos clientes")
                    }
                },
                fontSize = 20.sp,
                lineHeight = 26.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            val interaction = remember { MutableInteractionSource() }
            val pressed by interaction.collectIsPressedAsState()
            Button(
                onClick = onDiscountClick,
                interactionSource = interaction,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (pressed) LumisColors.PrimaryHover else LumisColors.Primary,
                    contentColor = if (pressed) LumisColors.OnButtonHover else LumisColors.OnButton
                ),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = "Garantir desconto",
                    fontFamily = Questrial,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
