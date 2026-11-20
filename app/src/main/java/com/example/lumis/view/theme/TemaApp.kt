package com.example.lumis.view.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * Modo de tema disponível no app.
 */
enum class ModoTema {
    CLARO, ESCURO, SISTEMA
}

/**
 * Estado global do tema (se está escuro no momento).
 * Atualizado pelo [LumisTheme] via SideEffect.
 */
object TemaApp {
    var escuro by mutableStateOf(false)

    /**
     * Retorna a cor apropriada para o tema atual.
     */
    fun corTema(claro: Color, escuro: Color): Color = if (TemaApp.escuro) escuro else claro
}