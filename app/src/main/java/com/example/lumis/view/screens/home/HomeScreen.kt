// Tela Home - a principal do app, igualzinho ao protótipo que o grupo recebeu.
// A gente dividiu ela em pedacinhos (componentes) pra cada um fazer uma parte.

package com.example.lumis.view.screens.home

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
import com.example.lumis.view.screens.home.components.CategorySection
import com.example.lumis.view.screens.home.components.PopularServicesGrid
import com.example.lumis.view.screens.home.components.PromoBanner
import com.example.lumis.view.screens.home.components.TopLocationBar
import com.example.lumis.view.screens.home.components.RecommendedSection
import com.example.lumis.view.screens.home.model.ProfessionalUi
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ModoTema
import androidx.compose.ui.res.stringResource
import com.example.lumis.R

// HomeScreen: junta todas as seções em uma lista que rola pra baixo.
// Usamos LazyColumn (em vez de Column normal) porque ela só desenha o que
// tá aparecendo na tela, então fica mais leve no celular.
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    // contentPadding vem do Scaffold (pra não tampar nada com a navbar).
    contentPadding: PaddingValues = PaddingValues(0.dp),
    localizacao: String = "",
    onLocationClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    // Esses onClick com {} vazio são de propósito: o grupo liga eles depois.
    onSeeAllCategories: () -> Unit = {},
    onSeeAllServices: () -> Unit = {},
    onDiscountClick: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onServiceClick: (String) -> Unit = {},
    profissionais: List<ProfessionalUi> = emptyList(),
    onFavoritoClick: (Int) -> Unit = {},
    onProfissionalClick: (Int) -> Unit = {}
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
            item {
                TopLocationBar(
                    address = localizacao.ifBlank { stringResource(R.string.define_region) },
                    subtitle = if (localizacao.isBlank()) {
                        stringResource(R.string.tap_to_add)
                    } else {
                        stringResource(R.string.current_location)
                    },
                    onLocationClick = onLocationClick,
                    onNotificationClick = onNotificationClick
                )
            }
            // 2. Banner verde da promoção "30% OFF"
            item { PromoBanner(onDiscountClick = onDiscountClick) }
            // 3. Bolinhas de categoria (Limpeza, Residencial, Comercial...)
            item {
                CategorySection(
                    onSeeAllClick = onSeeAllCategories,
                    onCategoryClick = onCategoryClick
                )
            }
            // 4. Grade com os 8 serviços populares
            item {
                PopularServicesGrid(
                    onSeeAllClick = onSeeAllServices,
                    onServiceClick = onServiceClick
                )
            }
            item {
                RecommendedSection(
                    profissionais = profissionais,
                    onFavoritoClick = onFavoritoClick,
                    onContratarClick = onProfissionalClick
                )
            }
        }
    }
}

// Preview da Home no tamanho de um celular comum (360x800).
@Preview(showBackground = true, backgroundColor = 0xFFF9F9FB, widthDp = 360, heightDp = 800)
@Composable
private fun HomeScreenPreview() {
    LumisTheme(modo = ModoTema.SISTEMA) {
        HomeScreen()
    }
}
