// Tela Home - a principal do app, igualzinho ao protótipo que o grupo recebeu.
// A gente dividiu ela em pedacinhos (componentes) pra cada um fazer uma parte.

package com.example.lumis.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lumis.ui.home.components.CategorySection
import com.example.lumis.ui.home.components.PopularServicesGrid
import com.example.lumis.ui.home.components.PromoBanner
import com.example.lumis.ui.home.components.RecommendedSection
import com.example.lumis.ui.home.components.TopLocationBar
import com.example.lumis.ui.theme.LumisColors
import com.example.lumis.ui.theme.LumisTheme

// HomeScreen: junta todas as seções em uma lista que rola pra baixo.
// Usamos LazyColumn (em vez de Column normal) porque ela só desenha o que
// tá aparecendo na tela, então fica mais leve no celular.
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    // contentPadding vem do Scaffold (pra não tampar nada com a navbar).
    contentPadding: PaddingValues = PaddingValues(0.dp),
    // Esses onClick com {} vazio são de propósito: o grupo liga eles depois.
    onSeeAllCategories: () -> Unit = {},
    onSeeAllServices: () -> Unit = {},
    onSeeAllRecommended: () -> Unit = {},
    onDiscountClick: () -> Unit = {},
    onHireClick: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LumisColors.Background) // fundinho cinza-claro do protótipo
            .padding(contentPadding)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), // margem lateral igual dos dois lados
            contentPadding = PaddingValues(top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp) // espaço entre as seções
        ) {
            // 1. Barrinha de endereço + sino + perfil
            item { TopLocationBar() }
            // 2. Banner verde da promoção "30% OFF"
            item { PromoBanner(onDiscountClick = onDiscountClick) }
            // 3. Bolinhas de categoria (Limpeza, Residencial, Comercial...)
            item {
                CategorySection(
                    onSeeAllClick = onSeeAllCategories,
                    onCategoryClick = {}
                )
            }
            // 4. Grade com os 8 serviços populares
            item {
                PopularServicesGrid(
                    onSeeAllClick = onSeeAllServices,
                    onServiceClick = {}
                )
            }
            // 5. Cards de profissionais recomendados
            item {
                RecommendedSection(
                    onSeeAllClick = onSeeAllRecommended,
                    onHireClick = onHireClick
                )
            }
        }
    }
}

// Preview da Home no tamanho de um celular comum (360x800).
@Preview(showBackground = true, backgroundColor = 0xFFF9F9FB, widthDp = 360, heightDp = 800)
@Composable
private fun HomeScreenPreview() {
    LumisTheme {
        HomeScreen()
    }
}
