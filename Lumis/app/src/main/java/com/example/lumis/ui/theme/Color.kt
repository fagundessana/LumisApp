package com.example.lumis.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Lumis Brand Colors
val LumisGreen = Color(0xFF3FBF8F)
val LumisGreenLight = Color(0xFFDFF6EC)
val LumisTeal = Color(0xFF009688)
val LumisBackground = Color(0xFFF5F6FA)
val LumisSurface = Color(0xFFFFFFFF)
val LumisBorder = Color(0xFFEDEDF2)
val LumisSelectedBorder = Color(0xFF3FBF8F)
val LumisDisabledToggle = Color(0xFFE0E0E0)
val LumisTextPrimary = Color(0xFF1E1E1E)
val LumisTextSecondary = Color(0xFF8A8A8E)
val LumisTextTertiary = Color(0xFFB0B0B0)

// Cores do grupo - identidade visual do app Lumis.
// A ideia aqui: ninguém espalha código hexadecimal pelos arquivos de tela,
// todo mundo usa o LumisColors. Se o professor pedir pra mudar um tom de
// verde, a gente muda SÓ aqui e o app inteiro atualiza.
object LumisColors {
    // Fundo das telas (aquele branquinho do protótipo) e texto cinza secundário.
    val Background = Color(0xFFF9F9FB)
    val SecondaryText = Color(0xFF666666)

    // As 3 cores principais que o grupo escolheu pro app.
    val Primary = Color(0xFF50BF87) // verde dos botões
    val Secondary = Color(0xFF3DBEA9) // verde-água
    val Tertiary = Color(0xFF508ABF) // azul dos ícones

    // Cor que o botão fica quando a pessoa aperta (no celular não tem hover).
    val PrimaryHover = Color(0xFF317252)
    val SecondaryHover = Color(0xFF2E8C7E)
    val TertiaryHover = Color(0xFF3168A2)

    // Cor do texto dentro dos botões (normal e apertado).
    val OnButton = Color(0xFFF9F9FB)
    val OnButtonHover = Color(0xFFD3D3D3)

    // Cinza clarinho usado em alguns fundos.
    val LightGray = Color(0xFFF5F5F5)

    // Cores de apoio que a gente tirou direto do protótipo (badge, cards...).
    val White = Color(0xFFFFFFFF)
    val TextPrimary = Color(0xFF1A1A1A) // preto dos títulos
    val BadgeYellow = Color(0xFFFFC107) // amarelo da etiqueta de oferta
    val OnBadgeYellow = Color(0xFF3E2723) // marrom do texto em cima do amarelo
    val CardTint = Color(0xFFEAF7F0) // verdinho claro dos cards de serviço
    val ExplorerCircle = Color(0xFFDDF0E6) // bola do botão Explorar
    val StarYellow = Color(0xFFFFB800) // estrelinha da avaliação
    val InactiveNav = Color(0xFF999999) // ícone da navbar quando não tá selecionado
    val Border = Color(0xFFE8E8EC) // bordinha cinza dos cards
    val Scrim = Color(0x99000000) // sombra preta transparente em cima das fotos
}
