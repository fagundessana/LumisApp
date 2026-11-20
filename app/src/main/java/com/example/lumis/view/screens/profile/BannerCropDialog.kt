package com.example.lumis.view.screens.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.lumis.view.screens.employee_profile.ProfileColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

// ---------------------------------------------------------------------------------------------
// EDITOR DE BANNER — o usuário arrasta e dá zoom na imagem dentro de uma moldura 5:2.
// O que fica fora da moldura (área escurecida) é cortado. O resultado é um Bitmap 5:2 já
// recortado, que a tela salva via FotoPerfilHelper.salvarBannerRecortado.
// Proporção 5:2 escolhida porque os perfis exibem o banner com ContentScale.Crop em
// ~150-160dp de altura x largura da tela (≈ 2,4 a 2,75:1): assim o que o usuário enquadra
// é praticamente o que aparece no perfil.
// ---------------------------------------------------------------------------------------------

const val BANNER_ASPECTO = 2.5f
private const val BANNER_LARGURA_PX = 1500
private const val ZOOM_MAXIMO = 5f

// Geometria da moldura e escala mínima (imagem sempre cobre a moldura inteira).
private class GeometriaCrop(
    val moldura: Size,
    val escalaMinima: Float,
    val bmpLargura: Int,
    val bmpAltura: Int
) {
    fun limiteX(escala: Float): Float = max(0f, (bmpLargura * escala - moldura.width) / 2f)
    fun limiteY(escala: Float): Float = max(0f, (bmpAltura * escala - moldura.height) / 2f)
}

@Composable
fun BannerCropDialog(
    arquivo: File,
    onCancelar: () -> Unit,
    onConfirmar: (Bitmap) -> Unit
) {
    var bitmap by remember(arquivo) { mutableStateOf<Bitmap?>(null) }
    var falhou by remember(arquivo) { mutableStateOf(false) }

    LaunchedEffect(arquivo) {
        val carregado = carregarBitmap(arquivo)
        if (carregado == null) falhou = true else bitmap = carregado
    }

    Dialog(
        onDismissRequest = onCancelar,
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
            Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text(
                        text = "Ajustar banner",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Arraste para mover e use dois dedos ou o controle abaixo para dar zoom. " +
                            "A parte escurecida será cortada.",
                        fontSize = 13.sp,
                        color = Color(0xFFB0B0B8)
                    )
                }

                val bmp = bitmap
                if (bmp == null) {
                    Column(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (falhou) {
                            Text(
                                text = "Não consegui abrir essa imagem.",
                                color = Color.White,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            CircularProgressIndicator(color = ProfileColors.Primary)
                        }
                    }
                } else {
                    EditorDeRecorte(bmp = bmp, onCancelar = onCancelar, onConfirmar = onConfirmar)
                }

                if (bmp == null) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onCancelar) {
                            Text(text = "Fechar", color = ProfileColors.Primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun androidx.compose.foundation.layout.ColumnScope.EditorDeRecorte(
    bmp: Bitmap,
    onCancelar: () -> Unit,
    onConfirmar: (Bitmap) -> Unit
) {
    val imagem: ImageBitmap = remember(bmp) { bmp.asImageBitmap() }
    var tamanhoArea by remember { mutableStateOf(IntSize.Zero) }
    var zoom by remember(bmp) { mutableStateOf(1f) }
    var deslocamento by remember(bmp) { mutableStateOf(Offset.Zero) }
    val margem = with(LocalDensity.current) { 24.dp.toPx() }

    val geometria: GeometriaCrop? = remember(bmp, tamanhoArea) {
        if (tamanhoArea.width <= 0 || tamanhoArea.height <= 0) {
            null
        } else {
            val largura = min(
                tamanhoArea.width - 2 * margem,
                (tamanhoArea.height - 2 * margem) * BANNER_ASPECTO
            ).coerceAtLeast(1f)
            val moldura = Size(largura, largura / BANNER_ASPECTO)
            GeometriaCrop(
                moldura = moldura,
                escalaMinima = max(moldura.width / bmp.width, moldura.height / bmp.height),
                bmpLargura = bmp.width,
                bmpAltura = bmp.height
            )
        }
    }

    // Aplica zoom (em torno do centro da moldura) + deslocamento, sempre mantendo a imagem
    // cobrindo a moldura inteira.
    fun aplicar(novoZoom: Float, pan: Offset) {
        val g = geometria ?: return
        val z = novoZoom.coerceIn(1f, ZOOM_MAXIMO)
        val fator = z / zoom
        val escala = g.escalaMinima * z
        val x = (deslocamento.x * fator + pan.x).coerceIn(-g.limiteX(escala), g.limiteX(escala))
        val y = (deslocamento.y * fator + pan.y).coerceIn(-g.limiteY(escala), g.limiteY(escala))
        zoom = z
        deslocamento = Offset(x, y)
    }

    Canvas(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clipToBounds()
            .onSizeChanged { tamanhoArea = it }
            .pointerInput(geometria) {
                detectTransformGestures { _, pan, gestoZoom, _ ->
                    aplicar(zoom * gestoZoom, pan)
                }
            }
    ) {
        val g = geometria ?: return@Canvas
        val escala = g.escalaMinima * zoom
        val larguraImg = bmp.width * escala
        val alturaImg = bmp.height * escala
        val cx = size.width / 2f
        val cy = size.height / 2f

        drawImage(
            image = imagem,
            dstOffset = IntOffset(
                (cx + deslocamento.x - larguraImg / 2f).roundToInt(),
                (cy + deslocamento.y - alturaImg / 2f).roundToInt()
            ),
            dstSize = IntSize(larguraImg.roundToInt(), alturaImg.roundToInt()),
            filterQuality = FilterQuality.High
        )

        val esquerda = cx - g.moldura.width / 2f
        val topo = cy - g.moldura.height / 2f
        val escurecer = Path().apply {
            fillType = PathFillType.EvenOdd
            addRect(Rect(0f, 0f, size.width, size.height))
            addRect(Rect(esquerda, topo, esquerda + g.moldura.width, topo + g.moldura.height))
        }
        drawPath(escurecer, Color.Black.copy(alpha = 0.65f))
        drawRect(
            color = Color.White,
            topLeft = Offset(esquerda, topo),
            size = g.moldura,
            style = Stroke(width = 2.dp.toPx())
        )
    }

    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Slider(
            value = zoom,
            onValueChange = { aplicar(it, Offset.Zero) },
            valueRange = 1f..ZOOM_MAXIMO,
            colors = SliderDefaults.colors(
                thumbColor = ProfileColors.Primary,
                activeTrackColor = ProfileColors.Primary
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onCancelar, modifier = Modifier.weight(1f)) {
                Text(text = "Cancelar", color = ProfileColors.Primary, fontSize = 15.sp)
            }
            Button(
                onClick = {
                    val g = geometria
                    if (g != null) {
                        val escala = g.escalaMinima * zoom
                        onConfirmar(recortar(bmp, g.moldura, escala, deslocamento))
                    }
                },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProfileColors.Primary,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Aplicar", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// Converte a moldura (coordenadas da tela) para a região correspondente do bitmap original.
// O centro da moldura, em coordenadas da imagem, é: centro do bitmap - deslocamento/escala.
private fun recortar(bmp: Bitmap, moldura: Size, escala: Float, deslocamento: Offset): Bitmap {
    val origemLargura = moldura.width / escala
    val origemAltura = moldura.height / escala
    val centroX = bmp.width / 2f - deslocamento.x / escala
    val centroY = bmp.height / 2f - deslocamento.y / escala

    val x = (centroX - origemLargura / 2f).roundToInt().coerceIn(0, bmp.width - 1)
    val y = (centroY - origemAltura / 2f).roundToInt().coerceIn(0, bmp.height - 1)
    val w = origemLargura.roundToInt().coerceIn(1, bmp.width - x)
    val h = origemAltura.roundToInt().coerceIn(1, bmp.height - y)

    val cortado = Bitmap.createBitmap(bmp, x, y, w, h)
    return if (w > BANNER_LARGURA_PX) {
        Bitmap.createScaledBitmap(
            cortado,
            BANNER_LARGURA_PX,
            (BANNER_LARGURA_PX / BANNER_ASPECTO).roundToInt(),
            true
        )
    } else {
        cortado
    }
}

// Decodifica com limite de tamanho (evita estourar memória com fotos de 12MP+) e já aplica
// a rotação EXIF (fotos tiradas em pé pela câmera).
private suspend fun carregarBitmap(arquivo: File, maxLado: Int = 2400): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(arquivo)) { decoder, info, _ ->
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                    val maior = max(info.size.width, info.size.height)
                    if (maior > maxLado) {
                        val razao = maxLado.toFloat() / maior
                        decoder.setTargetSize(
                            (info.size.width * razao).roundToInt().coerceAtLeast(1),
                            (info.size.height * razao).roundToInt().coerceAtLeast(1)
                        )
                    }
                }
            } else {
                val limites = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(arquivo.absolutePath, limites)
                var amostra = 1
                while (max(limites.outWidth, limites.outHeight) / amostra > maxLado) amostra *= 2
                val opcoes = BitmapFactory.Options().apply { inSampleSize = amostra }
                val original = BitmapFactory.decodeFile(arquivo.absolutePath, opcoes)
                if (original == null) {
                    null
                } else {
                    val rotacao = when (
                        ExifInterface(arquivo.absolutePath)
                            .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    ) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                        else -> 0f
                    }
                    if (rotacao == 0f) {
                        original
                    } else {
                        val matriz = Matrix().apply { postRotate(rotacao) }
                        Bitmap.createBitmap(original, 0, 0, original.width, original.height, matriz, true)
                    }
                }
            }
        } catch (e: Exception) {
            null
        }
    }
