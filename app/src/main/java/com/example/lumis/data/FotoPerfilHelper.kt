package com.example.lumis.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

// ---------------------------------------------------------------------------------------------
// FOTO DE PERFIL — cópia interna (recomendado)
// A Uri da galeria (Photo Picker) pode perder a permissão após reboot; por isso copiamos
// o bytes para filesDir/perfil_<id>.jpg e persistimos só o caminho absoluto no Room.
// Nenhuma permissão de armazenamento é necessária.
// ---------------------------------------------------------------------------------------------

object FotoPerfilHelper {

    fun arquivoFoto(context: Context, userId: Int): File =
        File(context.filesDir, "perfil_$userId.jpg")

    fun fotoExiste(path: String): Boolean =
        path.isNotBlank() && File(path).exists()

    suspend fun salvarFotoInterna(context: Context, origem: Uri, userId: Int): String =
        withContext(Dispatchers.IO) {
            val destino = arquivoFoto(context, userId)
            try {
                context.contentResolver.openInputStream(origem)?.use { input ->
                    destino.outputStream().use { output ->
                        input.copyTo(output)
                    }
                } ?: throw IllegalStateException("Não consegui ler a imagem escolhida.")
            } catch (e: IllegalStateException) {
                throw e
            } catch (_: Exception) {
                throw IllegalStateException("Não consegui salvar a foto. Tente outra imagem.")
            }
            destino.absolutePath
        }

    fun removerFoto(context: Context, userId: Int) {
        try {
            arquivoFoto(context, userId).takeIf { it.exists() }?.delete()
        } catch (_: Exception) {
            // Remoção é best-effort; o path será limpo no banco de qualquer forma.
        }
    }
}
