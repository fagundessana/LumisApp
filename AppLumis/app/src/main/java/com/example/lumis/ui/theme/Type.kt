package com.example.lumis.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.lumis.R

/**
 * Fontes do grupo (tipografia Lumis).
 *
 * O que o trabalho pedia:
 *  h1 -> Montserrat 32sp (títulos grandões)
 *  h2/h3 -> Merriweather Sans 24sp (títulos médios)
 *  textos normais -> Questrial 16sp (parágrafos)
 *
 * O que a gente fez: nos títulos das seções da Home usamos 17-18sp
 * em vez de 24sp, porque 24sp ficava gigante no celular pequeno e
 * diferente do protótipo. Os 24/32sp continuam existindo pra telas
 * com destaque maior (hero etc.).
 */

// As 3 famílias de fonte que baixamos pra pasta res/font.
// Cada Font() liga um arquivo .ttf a um peso (Bold, ExtraBold...).
val Montserrat = FontFamily(
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_extrabold, FontWeight.ExtraBold)
)
val MerriweatherSans = FontFamily(
    Font(R.font.merriweather_sans_bold, FontWeight.Bold)
)
val Questrial = FontFamily(
    Font(R.font.questrial_regular, FontWeight.Normal)
)

val LumisTypography = Typography(
    // h1 — hero / títulos grandes
    displayLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 38.sp
    ),
    // h2/h3 — especificação original 24sp (usar em telas internas, não nos headers da Home)
    headlineLarge = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = MerriweatherSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    // Header de seção da Home — otimizado para celular / fidelidade à imagem
    titleLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    // Parágrafos e textos comuns
    bodyLarge = TextStyle(
        fontFamily = Questrial,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Questrial,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Questrial,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Questrial,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Questrial,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )
)
