package com.example.lumis.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lumis.R // Certifique-se de que o R correto do seu pacote está importado

// Definição das cores personalizadas
val CorPrimaria = Color(0xFF50BF87)
val CorSecundaria = Color(0xFF3DBEA9)
val CorTerciaria = Color(0xFF508ABF)

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lumis),
                    contentDescription = "Logo Lumis",
                    modifier = Modifier.size(400.dp)
                )
            }

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

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(color = CorTerciaria)
        }
    }
}