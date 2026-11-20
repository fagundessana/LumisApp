package com.example.lumis.view.screens.city

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lumis.view.components.LumisTopBar
import com.example.lumis.view.screens.city.components.BinFillCard
import com.example.lumis.view.screens.city.components.BinMapCard
import com.example.lumis.view.screens.city.components.CircularFillIndicator
import com.example.lumis.view.screens.city.components.QuickActionsRow
import com.example.lumis.view.screens.city.components.StatusSummaryCard
import com.example.lumis.view.screens.city.model.CidadeUiModel
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisTheme
import com.example.lumis.view.theme.ModoTema

// Tela principal "Cidade Consciente"
// LazyColumn com: LumisTopBar fixo, StatusSummaryCard, BinFillCard, BinMapCard, QuickActionsRow
@Composable
fun CidadeConscienteScreen(
    modifier: Modifier = Modifier,
    model: CidadeUiModel = CidadeUiModel
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LumisColors.Background)
    ) {
        // LumisTopBar fixo no topo (fora da lista rolável)
        LumisTopBar()

        // Lista rolável com os componentes
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp), // altura aproximada da LumisTopBar
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Espaço para a TopBar não cobrir o primeiro item
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // 1. StatusSummaryCard
            item {
                StatusSummaryCard(statusItems = model.statusItems)
            }

            // 2. BinFillCard
            item {
                BinFillCard(
                    fraction = model.binFillFraction,
                    label = model.binFillLabel,
                    items = model.binFillItems
                )
            }

            // 3. BinMapCard
            item {
                BinMapCard(markers = model.mapMarkers)
            }

            // 4. QuickActionsRow
            item {
                QuickActionsRow()
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9FB, widthDp = 360, heightDp = 800)
@Composable
private fun CidadeConscienteScreenPreview() {
    LumisTheme(modo = ModoTema.SISTEMA) {
        CidadeConscienteScreen()
    }
}