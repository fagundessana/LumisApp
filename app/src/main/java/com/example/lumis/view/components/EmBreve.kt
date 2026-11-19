package com.example.lumis.view.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

// Retorna uma ação que mostra o aviso "Em breve".
// Use nos botões/atalhos cuja funcionalidade ainda não foi implementada.
@Composable
fun lembreteEmBreve(): () -> Unit {
    val context = LocalContext.current
    return remember(context) {
        {
            Toast.makeText(context, "Em breve", Toast.LENGTH_SHORT).show()
        }
    }
}