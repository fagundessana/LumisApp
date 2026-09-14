// Tema do grupo: aqui a gente "liga" as cores no Material3.
// Toda tela que usar LumisTheme ganha as cores e fontes do app de graça.

package com.example.lumis.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Esquema claro: diz pro Material3 qual cor usar em cada lugar
// (primary = botões, background = fundo, surface = cards...).
private val LumisLightScheme = lightColorScheme(
    primary = LumisColors.Primary,
    onPrimary = LumisColors.OnButton, // texto em cima do botão verde
    secondary = LumisColors.Secondary,
    onSecondary = LumisColors.OnButton,
    tertiary = LumisColors.Tertiary,
    onTertiary = LumisColors.OnButton,
    background = LumisColors.Background,
    onBackground = LumisColors.TextPrimary,
    surface = LumisColors.White, // cor dos cards
    onSurface = LumisColors.TextPrimary,
    surfaceVariant = LumisColors.LightGray,
    onSurfaceVariant = LumisColors.SecondaryText,
    outline = LumisColors.Border // cor das bordas
)

// É só embrulhar a tela com LumisTheme { ... } que tudo funciona.
@Composable
fun LumisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LumisLightScheme,
        typography = LumisTypography, // fontes (tá no Type.kt)
        content = content
    )
}
