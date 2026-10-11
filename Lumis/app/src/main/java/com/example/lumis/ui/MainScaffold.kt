package com.example.lumis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.lumis.ui.screens.ServicesScreen
import com.example.lumis.ui.theme.LumisBackground

@Composable
fun MainScaffold() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LumisBackground)
    ) {
        // Conteúdo principal (Serviços/Planos já tem Tabs + bottom bar interno)
        ServicesScreen()
    }
}
