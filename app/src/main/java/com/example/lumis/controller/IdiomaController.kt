package com.example.lumis.controller

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat

enum class ModoIdioma(val tag: String) {
    SISTEMA(""),
    PORTUGUES("pt-BR"),
    INGLES("en")
}

class IdiomaController(context: Context) {
    private val prefs = context.getSharedPreferences("lumis_idioma", Context.MODE_PRIVATE)

    var modo by mutableStateOf(
        runCatching { ModoIdioma.valueOf(prefs.getString("modo", ModoIdioma.SISTEMA.name)!!) }
            .getOrDefault(ModoIdioma.SISTEMA)
    )
        private set

    init {
        aplicar(modo)
    }

    fun definir(novo: ModoIdioma) {
        modo = novo
        prefs.edit().putString("modo", novo.name).apply()
        aplicar(novo)
    }

    private fun aplicar(modo: ModoIdioma) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(modo.tag))
    }
}
