package com.example.lumis.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

// ---------------------------------------------------------------------------------------------
// FOTO DE PERFIL / BANNER — cópia interna (recomendado)
// A Uri da galeria (Photo Picker) pode perder a permissão após reboot; por isso copiamos
// o bytes para filesDir/perfil_<id>.jpg (foto) ou filesDir/banner_<id>.jpg (banner)
// e persistimos só o caminho absoluto no Room.
// Nenhuma permissão de armazenamento é necessária.
// ---------------------------------------------------------------------------------------------

object FotoPerfilHelper {

    private fun arquivoBase(context: Context, userId: Int, tipo: String): File =
        File(context.filesDir, "${tipo}_$userId.jpg")

    fun arquivoFoto(context: Context, userId: Int): File =
        arquivoBase(context, userId, "perfil")

    fun arquivoBanner(context: Context, userId: Int): File =
        arquivoBase(context, userId, "banner")

    fun fotoExiste(path: String): Boolean =
        path.isNotBlank() && File(path).exists()

    suspend fun salvarFotoInterna(context: Context, origem: Uri, userId: Int, tipo: String = "perfil"): String =
        withContext(Dispatchers.IO) {
            val destino = when (tipo) {
                "banner" -> arquivoBanner(context, userId)
                else -> arquivoFoto(context, userId)
            }
            try {
                context.contentResolver.openInputStream(origem)?.use { input ->
                    destino.outputStream().use { output ->
                        input.copyTo(output)
                    }
                } ?: throw IllegalStateException("Não consegui ler a imagem escolhida.")
            } catch (e: IllegalStateException) {
                throw e
            } catch (_: Exception) {
                throw IllegalStateException("Não consegui salvar a imagem. Tente outra.")
            }
            destino.absolutePath
        }

    fun removerFoto(context: Context, userId: Int, tipo: String = "perfil") {
        try {
            val arquivo = when (tipo) {
                "banner" -> arquivoBanner(context, userId)
                else -> arquivoFoto(context, userId)
            }
            arquivo.takeIf { it.exists() }?.delete()
        } catch (_: Exception) {
            // Remoção é best-effort; o path será limpo no banco de qualquer forma.
        }
    }
}
