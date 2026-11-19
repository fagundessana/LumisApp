package com.example.lumis.view.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Marca
val LumisGreen = Color(0xFF3AA66E)
val LumisGreenDark = Color(0xFF2E8F5C)
val LumisGreenLight = Color(0xFFE7F5EC)
val LumisTeal = Color(0xFF3DBEA9)
val LumisTealLight = Color(0xFFE6F7F4)
val LumisBlueAccent = Color(0xFF4A6CF7)
val LumisGreenVivid = Color(0xFF50BF87)
val LumisBlue = Color(0xFF508ABF)
val LumisGreySecondary = Color(0xFF666666)

// Neutros (usados pelo esquema claro em Theme.kt; nas telas prefira MaterialTheme.colorScheme)
val LumisBackground = Color(0xFFF7F8FA)
val LumisSurface = Color(0xFFFFFFFF)
val LumisBorder = Color(0xFFE7E9EC)
val LumisTextPrimary = Color(0xFF1A1D1F)
val LumisTextSecondary = Color(0xFF7C8388)
val LumisTextTertiary = Color(0xFFAEB4B8)

// Estados
val LumisSelectedBorder = LumisGreen
val LumisDisabledToggle = Color(0xFFD8DBDD)

// Escolhe o valor claro ou escuro conforme o tema atual do app.
// Pode ser usada em qualquer tela: lumisThemed(corClara, corEscura)
@Composable
fun lumisThemed(light: Color, dark: Color): Color =
    if (LocalDarkTheme.current) dark else light

// Paleta da Home (branch Douglas)
// Neutros e fundos mudam com o tema. Cores de marca, hovers, selos e o scrim são iguais nos dois temas.
object LumisColors {
    // ---- Mudam com o tema (valor claro = o original) ----
    val Background: Color
        @Composable get() = lumisThemed(Color(0xFFF9F9FB), Color(0xFF121212))
    val SecondaryText: Color
        @Composable get() = lumisThemed(Color(0xFF666666), Color(0xFFA0A0A6))
    val LightGray: Color
        @Composable get() = lumisThemed(Color(0xFFF5F5F5), Color(0xFF2A2A2D))
    // Atenção: no escuro vira cinza-escuro (serve de fundo de card).
    // Se algum texto/ícone SOBRE o verde usar LumisColors.White, troque por Color.White.
    val White: Color
        @Composable get() = lumisThemed(Color(0xFFFFFFFF), Color(0xFF1E1E1E))
    val TextPrimary: Color
        @Composable get() = lumisThemed(Color(0xFF1A1A1A), Color(0xFFEDEDED))
    val CardTint: Color
        @Composable get() = lumisThemed(Color(0xFFEAF7F0), Color(0xFF1F3A2E))
    val ExplorerCircle: Color
        @Composable get() = lumisThemed(Color(0xFFDDF0E6), Color(0xFF264536))
    val InactiveNav: Color
        @Composable get() = lumisThemed(Color(0xFF999999), Color(0xFF8A8A8E))
    val Border: Color
        @Composable get() = lumisThemed(Color(0xFFE8E8EC), Color(0xFF2E2E33))

    // ---- Iguais nos dois temas ----
    val Primary = Color(0xFF50BF87)
    val Secondary = Color(0xFF3DBEA9)
    val Tertiary = Color(0xFF508ABF)
    val PrimaryHover = Color(0xFF317252)
    val SecondaryHover = Color(0xFF2E8C7E)
    val TertiaryHover = Color(0xFF3168A2)
    val OnButton = Color(0xFFF9F9FB)
    val OnButtonHover = Color(0xFFD3D3D3)
    val BadgeYellow = Color(0xFFFFC107)
    val OnBadgeYellow = Color(0xFF3E2723)
    val StarYellow = Color(0xFFFFB800)
    val Scrim = Color(0x99000000)
}

// Neutros que acompanham o tema (os valores claros são as constantes originais acima).
// Nas telas, use estes no lugar de LumisBackground, LumisSurface, LumisTextPrimary etc.
object LumisThemed {
    val Background: Color
        @Composable get() = lumisThemed(LumisBackground, Color(0xFF121212))
    val Surface: Color
        @Composable get() = lumisThemed(LumisSurface, Color(0xFF1E1E1E))
    val Border: Color
        @Composable get() = lumisThemed(LumisBorder, Color(0xFF2E2E33))
    val TextPrimary: Color
        @Composable get() = lumisThemed(LumisTextPrimary, Color(0xFFEDEDED))
    val TextSecondary: Color
        @Composable get() = lumisThemed(LumisTextSecondary, Color(0xFFA0A0A6))
    val GreySecondary: Color
        @Composable get() = lumisThemed(LumisGreySecondary, Color(0xFFA0A0A6))
}