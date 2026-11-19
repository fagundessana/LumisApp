// Seção "Categorias": as bolinhas que filtram (Limpeza, Residencial...).
// A selecionada fica verde, as outras brancas. Feito com LazyRow pra
// poder arrastar pro lado quando tiver muita categoria.

package com.example.lumis.view.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.view.screens.home.model.HomePreviewData
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.Questrial

@Composable
fun CategorySection(
    modifier: Modifier = Modifier,
    onSeeAllClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {}
) {
    // Guarda qual categoria tá selecionada (começa na "limpeza").
    // O remember faz o valor continuar mesmo quando a tela redesenha.
    var selectedId by remember { mutableStateOf("limpeza") }

    Column(modifier = modifier) {
        SectionHeader(title = "Categorias", onActionClick = onSeeAllClick)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(HomePreviewData.categories, key = { it.id }) { category ->
                val selected = category.id == selectedId
                FilterChip(
                    selected = selected,
                    onClick = {
                        selectedId = category.id
                        onCategoryClick(category.id)
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LumisColors.Primary,
                        selectedLabelColor = LumisColors.OnButton,
                        selectedLeadingIconColor = LumisColors.OnButton,
                        containerColor = LumisColors.White,
                        labelColor = LumisColors.SecondaryText
                    ),
                    border = if (selected) null else BorderStroke(1.dp, LumisColors.Border),
                    leadingIcon = {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = {
                        Text(
                            text = category.label,
                            fontFamily = Questrial,
                            fontSize = 13.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
            item {
                FilterChip(
                    selected = false,
                    onClick = onSeeAllClick,
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = LumisColors.White,
                        labelColor = LumisColors.SecondaryText
                    ),
                    border = BorderStroke(1.dp, LumisColors.Border),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.MoreHoriz,
                            contentDescription = "Mais",
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    label = {}
                )
            }
        }
    }
}
