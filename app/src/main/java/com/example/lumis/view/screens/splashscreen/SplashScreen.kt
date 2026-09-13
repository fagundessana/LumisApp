package com.example.lumis.view.screens.splashscreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lumis.R

val CorPrimaria = Color(0xFF50BF87)
val CorSecundaria = Color(0xFF3DBEA9)
val CorTerciaria = Color(0xFF508ABF)

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Container que agrupa os Arcos Giratórios + Logo Centralizada
            LoadingCircleWithLogo(
                modifier = Modifier.size(180.dp) // Tamanho total do indicador circular
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Lumis",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Serviços de limpeza",
                style = MaterialTheme.typography.bodyLarge,
                color = CorPrimaria
            )
        }
    }
}

@Composable
fun LoadingCircleWithLogo(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "animacao_loading")

    // Rotação contínua dos arcos
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing)
        ),
        label = "angulo"
    )

    // Animação de pulso/opacidade dos 3 pontos
    val dotAlpha1 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    val dotAlpha2 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    val dotAlpha3 by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    val gradientOuter = Brush.sweepGradient(
        colors = listOf(CorPrimaria, CorSecundaria, CorPrimaria)
    )
    val gradientInner = Brush.sweepGradient(
        colors = listOf(CorTerciaria, CorSecundaria, CorTerciaria)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Box para sobrepor a Logo no centro dos arcos
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            // Arcos giratórios desenhados no fundo
            Canvas(modifier = Modifier.fillMaxSize()) {
                rotate(rotation) {
                    // Arco externo
                    drawArc(
                        brush = gradientOuter,
                        startAngle = 0f,
                        sweepAngle = 230f,
                        useCenter = false,
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Arco interno
                    drawArc(
                        brush = gradientInner,
                        startAngle = 180f,
                        sweepAngle = 120f,
                        useCenter = false,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                        topLeft = Offset(10.dp.toPx(), 10.dp.toPx()),
                        size = Size(
                            size.width - 20.dp.toPx(),
                            size.height - 20.dp.toPx()
                        )
                    )
                }
            }

            // Logo centralizada dentro dos círculos
            Image(
                painter = painterResource(id = R.drawable.lumis),
                contentDescription = "Logo Lumis",
                modifier = Modifier.size(80.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Três pontos de carregamento na parte inferior do círculo
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(CorPrimaria.copy(alpha = dotAlpha1))
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(CorSecundaria.copy(alpha = dotAlpha2))
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(CorTerciaria.copy(alpha = dotAlpha3))
            )
        }
    }
}