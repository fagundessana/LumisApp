package com.example.lumis.view.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Build
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

private val LumisLightColorScheme = lightColorScheme(
    primary = LumisGreenVivid,
    onPrimary = Color(0xFF102018),
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
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF242424),
    onBackground = Color(0xFFF7F8FA),
    onSurface = Color(0xFFF7F8FA),
    outline = Color(0xFF4A4A4A),
    error = Color(0xFFFF6B6B)
)

private val LumisTypography = Typography(
    displaySmall = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, lineHeight = 38.sp),
    headlineMedium = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 32.sp),
    headlineSmall = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    titleLarge = TextStyle(fontFamily = MerriweatherSans, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontFamily = MerriweatherSans, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 21.sp),
    bodyLarge = TextStyle(fontFamily = Questrial, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 23.sp),
    bodyMedium = TextStyle(fontFamily = Questrial, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = Questrial, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 17.sp),
    labelSmall = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 0.7.sp),
    labelLarge = TextStyle(fontFamily = Montserrat, fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
        val corFundo = (if (isDark) LumisDarkColorScheme else LumisLightColorScheme).background.toArgb()

        window.statusBarColor = corFundo
        window.navigationBarColor = corFundo
        // Fundo da janela acompanha o tema (evita faixa clara atrás das barras)
        window.setBackgroundDrawable(ColorDrawable(corFundo))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
            window.isStatusBarContrastEnforced = false
        }
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
