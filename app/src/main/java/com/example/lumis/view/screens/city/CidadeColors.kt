package com.example.lumis.view.screens.city

import androidx.compose.ui.graphics.Color
import com.example.lumis.view.theme.LumisColors
import com.example.lumis.view.theme.TemaApp.corTema

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

    // Verde claro para trilha do arco (neutra: trilha clara → escura)
    val ProgressTrackGreen: Color get() = corTema(claro = Color(0xFFE7F5EC), escuro = Color(0xFF3A3A3C))

    // Fundo do sub-card cinza muito claro (neutra: fundo cinza-claro → escuro)
    val SubCardBackground: Color get() = corTema(claro = Color(0xFFF5F5F5), escuro = Color(0xFF2C2C2C))

    // Chip "Ver mais" verde
    val ChipVerMaisBackground = LumisColors.Primary
    val ChipVerMaisText = Color.White

    // Botão de filtro azul (Tertiary)
    val FilterButtonBackground = LumisColors.Tertiary
    val FilterButtonIcon = Color.White

    // Botões QuickActions
    val ActionEcopotonsBackground = LumisColors.Primary
    val ActionColetaBackground = Color(0xFFFFC107) // amarelo
    // Fundo verde claro do botão Dados (neutra: fundo claro → escuro)
    val ActionDadosBackground: Color get() = corTema(claro = Color(0xFFE7F5EC), escuro = Color(0xFF1F3A2E))
    val ActionDadosText = LumisColors.Primary // texto verde escuro para contraste
    val ActionIconWhite = Color.White
    // Texto escuro para ações (neutra: texto escuro → claro)
    val ActionTextDark: Color get() = corTema(claro = Color(0xFF1A1A1A), escuro = Color(0xFFF9F9FB))
}