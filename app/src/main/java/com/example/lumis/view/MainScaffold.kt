package com.example.lumis.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.lumis.view.screens.ServicesScreen
import com.example.lumis.view.theme.LumisBackground

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
