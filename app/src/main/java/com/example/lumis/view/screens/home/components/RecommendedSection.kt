package com.example.lumis.view.screens.home.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.lumis.R
import com.example.lumis.view.screens.home.model.ProfessionalUi
import com.example.lumis.view.theme.LumisColors

@Composable
fun RecommendedSection(
    profissionais: List<ProfessionalUi>,
    onFavoritoClick: (Int) -> Unit,
    onContratarClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (profissionais.isEmpty()) return

    Column(modifier) {
        SectionHeader(title = stringResource(R.string.recommended_professionals), showAction = false)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
            Icon(Icons.Outlined.Verified, null, tint = LumisColors.Primary, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(5.dp))
            Text(stringResource(R.string.demo_profiles_from_database), color = LumisColors.SecondaryText)
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(end = 4.dp)) {
            items(profissionais, key = { it.id }) { profissional ->
                ProfessionalCard(profissional, { onFavoritoClick(profissional.id) }, { onContratarClick(profissional.id) })
            }
        }
    }
}

@Composable
private fun ProfessionalCard(profissional: ProfessionalUi, onFavorito: () -> Unit, onContratar: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = LumisColors.Surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, LumisColors.Border),
        modifier = Modifier.width(216.dp)
    ) {
        Column {
            Box(
                Modifier.fillMaxWidth().height(104.dp)
                    .background(androidx.compose.ui.graphics.Brush.linearGradient(listOf(LumisColors.PrimaryHover, LumisColors.Primary)))
            ) {
                Box(
                    Modifier.align(Alignment.Center).size(62.dp).clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Person, null, tint = Color.White, modifier = Modifier.size(36.dp))
                }
                IconButton(
                    onClick = onFavorito,
                    modifier = Modifier.align(Alignment.TopEnd).padding(6.dp).clip(CircleShape).background(LumisColors.Surface.copy(alpha = 0.92f))
                ) {
                    Icon(
                        if (profissional.favorito) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        stringResource(R.string.favorite),
                        tint = if (profissional.favorito) Color(0xFFE45869) else LumisColors.SecondaryText
                    )
                }
            }
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text(profissional.nome, color = LumisColors.TextPrimary, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(profissional.especialidades, color = LumisColors.SecondaryText, maxLines = 2, minLines = 2, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocationOn, null, tint = LumisColors.Tertiary, modifier = Modifier.size(15.dp))
                    Text(profissional.localizacao, color = LumisColors.SecondaryText, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, null, tint = LumisColors.StarYellow, modifier = Modifier.size(17.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        profissional.mediaAvaliacoes?.let { "%.1f (${profissional.quantidadeAvaliacoes})".format(it) }
                            ?: stringResource(R.string.new_professional),
                        color = LumisColors.SecondaryText
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (profissional.valorMinimoCentavos > 0) stringResource(R.string.from_price, profissional.valorMinimoCentavos / 100.0)
                        else stringResource(R.string.price_to_agree),
                        modifier = Modifier.weight(1f),
                        color = LumisColors.TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Button(onClick = onContratar, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)) {
                        Text(stringResource(R.string.hire))
                    }
                }
            }
        }
    }
}
