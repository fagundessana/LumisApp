package com.example.lumis.view.theme

import androidx.compose.ui.graphics.Color
import com.example.lumis.view.theme.TemaApp.corTema

// Marca
val LumisGreen = Color(0xFF3AA66E)
val LumisGreenDark = Color(0xFF2E8F5C)
val LumisGreenLight: Color get() = corTema(claro = Color(0xFFE7F5EC), escuro = Color(0xFF1F3A2E))
val LumisTeal = Color(0xFF3DBEA9)
val LumisTealLight: Color get() = corTema(claro = Color(0xFFE6F7F4), escuro = Color(0xFF1C3A35))
val LumisBlueAccent = Color(0xFF4A6CF7)
val LumisGreenVivid = Color(0xFF50BF87)
val LumisBlue = Color(0xFF508ABF)
val LumisGreySecondary: Color get() = corTema(claro = Color(0xFF666666), escuro = Color(0xFF9A9A9A))

// Neutros
val LumisBackground: Color get() = corTema(claro = Color(0xFFF7F8FA), escuro = Color(0xFF1E1E1E))
val LumisSurface: Color get() = corTema(claro = Color(0xFFFFFFFF), escuro = Color(0xFF262626))
val LumisBorder: Color get() = corTema(claro = Color(0xFFE7E9EC), escuro = Color(0xFF3A3A3C))
val LumisTextPrimary: Color get() = corTema(claro = Color(0xFF1A1D1F), escuro = Color(0xFFF9F9FB))
val LumisTextSecondary: Color get() = corTema(claro = Color(0xFF7C8388), escuro = Color(0xFFA0A6AB))
val LumisTextTertiary: Color get() = corTema(claro = Color(0xFFAEB4B8), escuro = Color(0xFF6E7479))

// Estados
val LumisSelectedBorder = LumisGreen
val LumisDisabledToggle: Color get() = corTema(claro = Color(0xFFD8DBDD), escuro = Color(0xFF3F4346))

// Paleta da Home (branch Douglas)
object LumisColors {
    val Background: Color get() = corTema(claro = Color(0xFFF9F9FB), escuro = Color(0xFF1E1E1E))
    val SecondaryText: Color get() = corTema(claro = Color(0xFF666666), escuro = Color(0xFFB0B0B0))
    val Primary = Color(0xFF50BF87)
    val Secondary = Color(0xFF3DBEA9)
    val Tertiary = Color(0xFF508ABF)
    val PrimaryHover = Color(0xFF317252)
    val SecondaryHover = Color(0xFF2E8C7E)
    val TertiaryHover = Color(0xFF3168A2)
    val OnButton = Color(0xFFF9F9FB)
    val OnButtonHover = Color(0xFFD3D3D3)
    val LightGray: Color get() = corTema(claro = Color(0xFFF5F5F5), escuro = Color(0xFF2C2C2C))
    val White = Color(0xFFFFFFFF)
    val TextPrimary: Color get() = corTema(claro = Color(0xFF1A1A1A), escuro = Color(0xFFF9F9FB))
    val BadgeYellow = Color(0xFFFFC107)
    val OnBadgeYellow = Color(0xFF3E2723)
    val CardTint: Color get() = corTema(claro = Color(0xFFEAF7F0), escuro = Color(0xFF1F3A2E))
    val ExplorerCircle: Color get() = corTema(claro = Color(0xFFDDF0E6), escuro = Color(0xFF24463A))
    val StarYellow = Color(0xFFFFB800)
    val InactiveNav = Color(0xFF999999)
    val Border: Color get() = corTema(claro = Color(0xFFE8E8EC), escuro = Color(0xFF3A3A3C))
    val Scrim = Color(0x99000000)
    val Surface: Color get() = corTema(claro = Color(0xFFFFFFFF), escuro = Color(0xFF262626))
}