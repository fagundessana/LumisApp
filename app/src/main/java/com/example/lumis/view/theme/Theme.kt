package com.example.lumis.view.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.view.Window
import android.view.WindowInsetsController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

private val LumisLightColorScheme = lightColorScheme(
    primary = LumisGreen,
    onPrimary = LumisSurface,
    secondary = LumisTeal,
    background = LumisBackground,
    surface = LumisSurface,
    onBackground = LumisTextPrimary,
    onSurface = LumisTextPrimary,
    outline = LumisBorder,
    error = Color(0xFFE05353)
)

private val LumisDarkColorScheme = darkColorScheme(
    primary = LumisGreenVivid,
    onPrimary = Color(0xFF1E1E1E),
    secondary = LumisTeal,
    background = Color(0xFF1E1E1E),
    surface = Color(0xFF262626),
    onBackground = Color(0xFFF7F8FA),
    onSurface = Color(0xFFF7F8FA),
    outline = Color(0xFF4A4A4A),
    error = Color(0xFFFF6B6B)
)

private val LumisTypography = Typography(
    headlineSmall = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 26.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 22.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 20.sp),
    bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 20.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 11.sp, letterSpacing = 0.6.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
)

private val LumisShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun LumisTheme(
    modo: ModoTema,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    // Calcula se deve usar tema escuro
    val isDark: Boolean = when (modo) {
        ModoTema.CLARO -> false
        ModoTema.ESCURO -> true
        ModoTema.SISTEMA -> {
            val activity = context.findActivity()
            // Verifica modo escuro do sistema via Configuration (API 1+)
            val nightMode = activity?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
            nightMode == Configuration.UI_MODE_NIGHT_YES
        }
    }

    // Atualiza estado global do tema
    SideEffect {
        TemaApp.escuro = isDark
    }

    // Ajusta barras do sistema (status + navigation)
    SideEffect {
        val activity = context.findActivity() ?: return@SideEffect
        val window = activity.window
        val insetsController = WindowCompat.getInsetsController(window, view)
        val colorScheme = if (isDark) LumisDarkColorScheme else LumisLightColorScheme

        // Cores das barras - converte Color para Int ARGB
        val backgroundColor = colorScheme.background
        window.statusBarColor = backgroundColor.toArgb()
        window.navigationBarColor = backgroundColor.toArgb()

        // Aparência dos ícones (claro/escuro)
        insetsController.isAppearanceLightStatusBars = !isDark
        insetsController.isAppearanceLightNavigationBars = !isDark
    }

    MaterialTheme(
        colorScheme = if (isDark) LumisDarkColorScheme else LumisLightColorScheme,
        typography = LumisTypography,
        shapes = LumisShapes,
        content = content
    )
}

private fun Context.findActivity(): Activity? {
    var currentContext: Context = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) return currentContext
        currentContext = currentContext.baseContext
    }
    return null
}

private fun Color.toArgb(): Int = this.value.toInt()