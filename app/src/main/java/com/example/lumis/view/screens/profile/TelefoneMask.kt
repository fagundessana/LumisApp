package com.example.lumis.view.screens.profile

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

// ---------------------------------------------------------------------------------------------
// MÁSCARA AUTOMÁTICA DE TELEFONE BRASILEIRO
// (00) 0000-0000 para fixo (10 dígitos) | (00) 00000-0000 para celular (11 dígitos)
// Aplica progressivamente enquanto digita; apaga sem travar o cursor no meio.
// ---------------------------------------------------------------------------------------------

fun somenteDigitosTelefone(valor: String): String =
    valor.filter { it.isDigit() }.take(11)

fun aplicarMascaraTelefone(valor: String): String {
    val d = somenteDigitosTelefone(valor)
    return when {
        d.isEmpty() -> ""
        d.length <= 2 -> "($d"
        d.length <= 6 -> "(${d.substring(0, 2)}) ${d.substring(2)}"
        d.length <= 10 -> "(${d.substring(0, 2)}) ${d.substring(2, 6)}-${d.substring(6)}"
        else -> "(${d.substring(0, 2)}) ${d.substring(2, 7)}-${d.substring(7)}"
    }
}

/** Vazio é válido (campo opcional); preenchido exige 10 ou 11 dígitos. */
fun telefoneValido(valor: String): Boolean {
    val d = valor.filter { it.isDigit() }
    return d.isEmpty() || d.length in 10..11
}

// ---------------------------------------------------------------------------------------------
// Versão com controle de cursor (para usar com TextFieldValue).
// Resolve dois defeitos da versão String pura:
//  1. Apagar um separador (")", espaço, "-") não altera os dígitos, então a máscara
//     reaplicada "devolvia" o caractere na hora — parecia que voltava caracteres.
//  2. A recomposição jogava o cursor sempre para o fim, quebrando edição no meio.
// Regras:
//  - Dígito digitado/apagado: reformata e reposiciona o cursor após o mesmo dígito.
//  - Separador digitado manualmente: ignorado.
//  - Backspace sobre separador: apaga o dígito vizinho (comportamento esperado).
// ---------------------------------------------------------------------------------------------

fun aplicarMascaraTelefoneComCursor(
    anterior: TextFieldValue,
    atual: TextFieldValue
): TextFieldValue {
    // Com seleção ativa (ex.: colar por cima, selecionar tudo e digitar): reformata e vai pro fim.
    if (atual.selection.start != atual.selection.end) {
        val digitos = atual.text.filter { it.isDigit() }.take(11)
        val formatado = aplicarMascaraTelefone(digitos)
        return atual.copy(text = formatado, selection = TextRange(formatado.length))
    }

    val digitosAntes = anterior.text.filter { it.isDigit() }
    var digitos = atual.text.filter { it.isDigit() }.take(11)
    val cursor = atual.selection.start.coerceIn(0, atual.text.length)

    if (digitos == digitosAntes) {
        // Só mexeu em caractere da máscara ("(", ")", espaço, "-").
        if (atual.text.length >= anterior.text.length) {
            return anterior // separador digitado manualmente: ignora
        }
        // Backspace sobre separador: apaga o dígito imediatamente anterior ao cursor.
        val digitosAteCursor = atual.text.take(cursor).count { it.isDigit() }
        val idxRemover = digitosAteCursor - 1
        if (idxRemover < 0 || idxRemover >= digitosAntes.length) {
            return anterior
        }
        digitos = digitosAntes.removeRange(idxRemover, idxRemover + 1).take(11)
        val formatado = aplicarMascaraTelefone(digitos)
        return TextFieldValue(formatado, TextRange(posicaoAposDigitos(formatado, digitosAteCursor - 1)))
    }

    // Caso normal (dígito digitado, apagado ou colado): mantém o cursor após o mesmo dígito.
    val digitosAteCursor = atual.text.take(cursor).count { it.isDigit() }.coerceAtMost(digitos.length)
    val formatado = aplicarMascaraTelefone(digitos)
    return TextFieldValue(formatado, TextRange(posicaoAposDigitos(formatado, digitosAteCursor)))
}

/** Índice no texto mascarado logo após os N primeiros dígitos (0 -> início). */
private fun posicaoAposDigitos(mascarado: String, nDigitos: Int): Int {
    if (nDigitos <= 0) return 0
    var vistos = 0
    mascarado.forEachIndexed { i, c ->
        if (c.isDigit()) {
            vistos++
            if (vistos == nDigitos) return i + 1
        }
    }
    return mascarado.length
}
