package com.example.lumis.controller

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lumis.view.theme.ModoTema

/**
 * Controller para gerenciar preferência de tema do usuário.
 * Persiste em SharedPreferences "lumis_tema" com chave "modo".
 */
class TemaController(context: Context) {

    private val prefs = context.getSharedPreferences("lumis_tema", Context.MODE_PRIVATE)

    // Modo atual salvo (padrão: SISTEMA)
    var modo: ModoTema by mutableStateOf(
        ModoTema.valueOf(prefs.getString("modo", "SISTEMA")?.uppercase() ?: "SISTEMA")
    )
        private set

    /**
     * Define novo modo de tema e persiste.
     */
    fun definir(novo: ModoTema) {
        modo = novo
        prefs.edit().putString("modo", novo.name.lowercase()).apply()
    }
}