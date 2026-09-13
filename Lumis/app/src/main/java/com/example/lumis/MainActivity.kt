package com.example.lumis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.lumis.ui.theme.LumisTheme
import kotlinx.coroutines.delay
import com.example.lumis.ui.screens.SplashScreen
import com.example.lumis.data.AppDatabase
import com.example.lumis.ui.screens.CadastroScreen

private const val SPLASH_DURATION_MS = 1800L

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            LumisTheme {

                val database = remember {
                    AppDatabase.getDatabase(this)
                }

                var mostrarSplash by remember {
                    mutableStateOf(true)
                }

                LaunchedEffect(Unit) {

                    delay(SPLASH_DURATION_MS)

                    mostrarSplash = false
                }

                Crossfade(
                    targetState = mostrarSplash,
                    label = "splash"
                ) { splashVisivel ->

                    if (splashVisivel) {

                        SplashScreen()

                    } else {

                        CadastroScreen(
                            database = database
                        )
                    }
                }
            }
        }
    }
}