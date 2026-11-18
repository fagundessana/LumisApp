package com.example.lumis.view.screens.city.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.lumis.view.screens.city.CidadeColors

// Resumo de status da cidade (4 colunas)
data class StatusItem(
    val count: Int,
    val label: String,
    val icon: ImageVector,
    val color: androidx.compose.ui.graphics.Color
)

// Item de lixeira na lista do BinFillCard
data class BinFillItem(
    val name: String,
    val fillPercent: Int
)

// Marcador no mapa
data class MapMarker(
    val type: MarkerType,
    val xFraction: Float, // 0.0 a 1.0 (esquerda para direita)
    val yFraction: Float  // 0.0 a 1.0 (topo para base)
)

enum class MarkerType(
    val icon: ImageVector,
    val color: androidx.compose.ui.graphics.Color
) {
    DISPONIVEL(Icons.Filled.CheckCircle, CidadeColors.StatusDisponivel),
    ATENCAO(Icons.Filled.Warning, CidadeColors.StatusAtencao),
    PRIORITARIA(Icons.Filled.Delete, CidadeColors.StatusPrioritaria),
    URGENTE(Icons.Filled.Warning, CidadeColors.StatusUrgente),
    LOCALIZACAO(Icons.Filled.MyLocation, CidadeColors.StatusDisponivel)
}

// Modelo principal da tela - dados de exemplo fixos
object CidadeUiModel {
    val statusItems = listOf(
        StatusItem(128, "Disponíveis", Icons.Filled.CheckCircle, CidadeColors.StatusDisponivel),
        StatusItem(34, "Atenção", Icons.Filled.Warning, CidadeColors.StatusAtencao),
        StatusItem(11, "Prioritárias", Icons.Filled.Delete, CidadeColors.StatusPrioritaria),
        StatusItem(5, "Urgentes", Icons.Filled.Warning, CidadeColors.StatusUrgente)
    )

    val binFillFraction = 0.5f
    val binFillLabel = "50 % cheio"

    val binFillItems = listOf(
        BinFillItem("Rua XV", 39),
        BinFillItem("Praça verde", 11)
    )

    val mapMarkers = listOf(
        MapMarker(MarkerType.DISPONIVEL, 0.15f, 0.25f),
        MapMarker(MarkerType.DISPONIVEL, 0.85f, 0.30f),
        MapMarker(MarkerType.ATENCAO, 0.35f, 0.55f),
        MapMarker(MarkerType.PRIORITARIA, 0.65f, 0.45f),
        MapMarker(MarkerType.URGENTE, 0.50f, 0.75f),
        MapMarker(MarkerType.LOCALIZACAO, 0.90f, 0.90f)
    )
}