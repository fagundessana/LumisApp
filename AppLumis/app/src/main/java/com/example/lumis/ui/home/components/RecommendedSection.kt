// Seção "Recomendado pra você": os cards de profissionais com foto,
// nota, tempo, preço e botão Contratar. Rola pro lado (LazyRow).

package com.example.lumis.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lumis.ui.home.model.HomePreviewData
import com.example.lumis.ui.home.model.Professional
import com.example.lumis.ui.theme.LumisColors
import com.example.lumis.ui.theme.Montserrat
import com.example.lumis.ui.theme.Questrial

@Composable
fun RecommendedSection(
    modifier: Modifier = Modifier,
    onSeeAllClick: () -> Unit = {},
    onHireClick: (String) -> Unit = {}
) {
    Column(modifier = modifier) {
        SectionHeader(title = "Recomendado para você", onActionClick = onSeeAllClick)
        // Linhazinha "Profissionais verificados" com o selo verde.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Verified,
                contentDescription = null,
                tint = LumisColors.Primary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Profissionais verificados",
                fontFamily = Questrial,
                fontSize = 11.sp,
                color = LumisColors.SecondaryText
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 4.dp)
        ) {
            items(HomePreviewData.recommended, key = { it.id }) { pro ->
                ProfessionalCard(pro = pro, onHireClick = { onHireClick(pro.id) })
            }
        }
    }
}

@Composable
private fun ProfessionalCard(
    pro: Professional,
    onHireClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = LumisColors.White,
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = modifier.width(172.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                AsyncImage(
                    model = pro.imageUrl ?: pro.placeholderRes,
                    contentDescription = pro.title,
                    placeholder = painterResource(pro.placeholderRes),
                    error = painterResource(pro.placeholderRes),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                // Badge OFF
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LumisColors.Primary,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = pro.discountBadge,
                        fontFamily = Questrial,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = LumisColors.OnButton,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                // Favorito / salvar
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(LumisColors.White.copy(alpha = 0.92f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (pro.isFavorite) Icons.Filled.Favorite else Icons.Filled.BookmarkBorder,
                        contentDescription = "Salvar",
                        tint = if (pro.isFavorite) LumisColors.Tertiary else LumisColors.SecondaryText,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = pro.title,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = LumisColors.TextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = LumisColors.StarYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${pro.rating} (${pro.ratingCount})",
                        fontFamily = Questrial,
                        fontSize = 11.sp,
                        color = LumisColors.SecondaryText
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null,
                        tint = LumisColors.Tertiary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = pro.duration,
                        fontFamily = Questrial,
                        fontSize = 11.sp,
                        color = LumisColors.SecondaryText
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pro.price,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        color = LumisColors.TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    val interaction = remember { MutableInteractionSource() }
                    val pressed by interaction.collectIsPressedAsState()
                    Button(
                        onClick = onHireClick,
                        interactionSource = interaction,
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (pressed) LumisColors.PrimaryHover else LumisColors.Primary,
                            contentColor = if (pressed) LumisColors.OnButtonHover else LumisColors.OnButton
                        ),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text(
                            text = "Contratar",
                            fontFamily = Questrial,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
