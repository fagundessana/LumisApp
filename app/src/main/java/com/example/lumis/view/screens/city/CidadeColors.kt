package com.example.lumis.view.screens.city

import androidx.compose.ui.graphics.Color
import com.example.lumis.view.theme.LumisColors

// Cores de status específicas da tela Cidade Consciente
// Não altera o Color.kt global; usa LumisColors.Primary como base verde
object CidadeColors {
    // Verde para "Disponíveis" - usa o Primary do tema
    val StatusDisponivel = LumisColors.Primary // 0xFF50BF87

    // Amarelo para "Atenção"
    val StatusAtencao = Color(0xFFFFC107)

    // Laranja para "Prioritárias" e arco de progresso
    val StatusPrioritaria = Color(0xFFF59E42)

    // Vermelho para "Urgentes"
    val StatusUrgente = Color(0xFFE5484D)

    // Laranja do arco de progresso (sweep)
    val ProgressArcOrange = Color(0xFFF28C38)

    // Verde claro para trilha do arco
    val ProgressTrackGreen = Color(0xFFE7F5EC) // LumisGreenLight

    // Fundo do sub-card cinza muito claro
    val SubCardBackground = Color(0xFFF5F5F5) // LumisColors.LightGray

    // Chip "Ver mais" verde
    val ChipVerMaisBackground = LumisColors.Primary
    val ChipVerMaisText = Color.White

    // Botão de filtro azul (Tertiary)
    val FilterButtonBackground = LumisColors.Tertiary
    val FilterButtonIcon = Color.White

    // Botões QuickActions
    val ActionEcopotonsBackground = LumisColors.Primary
    val ActionColetaBackground = Color(0xFFFFC107) // amarelo
    val ActionDadosBackground = Color(0xFFE7F5EC) // verde bem claro (LumisGreenLight)
    val ActionDadosText = LumisColors.Primary // texto verde escuro para contraste
    val ActionIconWhite = Color.White
    val ActionTextDark = Color(0xFF1A1A1A) // LumisColors.TextPrimary
}