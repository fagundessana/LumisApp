package com.example.lumis.data

import android.content.Context
import android.graphics.Bitmap
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

    // Imagem ORIGINAL escolhida para o banner (sem recorte). Fica guardada para que o usuário
    // possa reajustar o enquadramento depois sem precisar escolher a foto de novo.
    fun arquivoBannerOriginal(context: Context, userId: Int): File =
        File(context.filesDir, "banner_original_$userId.jpg")

    // Destino temporário da câmera: só vira "original" se a foto for confirmada.
    fun arquivoBannerTemp(context: Context, userId: Int): File =
        File(context.filesDir, "banner_tmp_$userId.jpg")

    // Banners recortados: banner_<id>.jpg (formato antigo) ou banner_<id>_<timestamp>.jpg.
    // Cada recorte ganha nome novo para o Coil não reutilizar a imagem antiga do cache.
    private fun arquivosBannerRecortado(context: Context, userId: Int): List<File> {
        val regex = Regex("^banner_${userId}(_\\d+)?\\.jpg$")
        return context.filesDir.listFiles { _, nome -> regex.matches(nome) }?.toList() ?: emptyList()
    }

    // Fonte do ajuste: a original, se existir; senão o banner já recortado (ajuste limitado).
    fun arquivoParaAjuste(context: Context, userId: Int, bannerPath: String): File? {
        val original = arquivoBannerOriginal(context, userId)
        return if (original.exists()) original else bannerPath.takeIf { fotoExiste(it) }?.let { File(it) }
    }

    suspend fun copiarOriginalBanner(context: Context, origem: Uri, userId: Int): File =
        withContext(Dispatchers.IO) {
            val destino = arquivoBannerOriginal(context, userId)
            try {
                context.contentResolver.openInputStream(origem)?.use { input ->
                    destino.outputStream().use { output -> input.copyTo(output) }
                } ?: throw IllegalStateException("Não consegui ler a imagem escolhida.")
            } catch (e: IllegalStateException) {
                throw e
            } catch (_: Exception) {
                throw IllegalStateException("Não consegui abrir a imagem. Tente outra.")
            }
            destino
        }

    suspend fun promoverBannerTempParaOriginal(context: Context, userId: Int): File =
        withContext(Dispatchers.IO) {
            val temp = arquivoBannerTemp(context, userId)
            val destino = arquivoBannerOriginal(context, userId)
            try {
                temp.copyTo(destino, overwrite = true)
                temp.delete()
            } catch (_: Exception) {
                throw IllegalStateException("Não consegui salvar a foto da câmera.")
            }
            destino
        }

    suspend fun salvarBannerRecortado(context: Context, bitmap: Bitmap, userId: Int): String =
        withContext(Dispatchers.IO) {
            val destino = File(context.filesDir, "banner_${userId}_${System.currentTimeMillis()}.jpg")
            try {
                destino.outputStream().use { saida ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, saida)
                }
            } catch (_: Exception) {
                throw IllegalStateException("Não consegui salvar o banner. Tente de novo.")
            }
            // Só apaga os recortes antigos depois que o novo foi gravado com sucesso.
            arquivosBannerRecortado(context, userId).filter { it != destino }.forEach { it.delete() }
            destino.absolutePath
        }

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
            if (tipo == "banner") {
                arquivosBannerRecortado(context, userId).forEach { it.delete() }
                arquivoBannerOriginal(context, userId).takeIf { it.exists() }?.delete()
                arquivoBannerTemp(context, userId).takeIf { it.exists() }?.delete()
            } else {
                arquivoFoto(context, userId).takeIf { it.exists() }?.delete()
            }
        } catch (_: Exception) {
            // Remoção é best-effort; o path será limpo no banco de qualquer forma.
        }
    }
}
