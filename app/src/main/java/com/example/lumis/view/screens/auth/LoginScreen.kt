package com.example.lumis.view.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.lumis.R
import com.example.lumis.controller.AuthController
import com.example.lumis.view.components.LumisPageTitle
import com.example.lumis.view.components.LumisPrimaryButton
import com.example.lumis.view.components.LumisTag
import com.example.lumis.view.components.LumisTextField
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisGreenLight
import com.example.lumis.view.theme.LumisSurface
import com.example.lumis.view.theme.LumisTextSecondary
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(authController: AuthController) {
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf<String?>(null) }
    var carregando by remember { mutableStateOf(false) }
    var conteudoVisivel by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { conteudoVisivel = true }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(LumisGreenLight.copy(alpha = 0.72f), LumisBackground, LumisBackground)
                )
            )
    ) {
        val horizontalPadding = if (maxWidth > 600.dp) 32.dp else 24.dp

        AnimatedVisibility(
            visible = conteudoVisivel,
            enter = fadeIn(tween(420)) + slideInVertically(tween(420)) { it / 12 }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 460.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    LumisTag(text = "Acesse sua conta")
                    LumisPageTitle(
                        title = "Bem-vindo à Lumis",
                        subtitle = "Entre para contratar serviços, acompanhar sua agenda e cuidar do seu espaço."
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(178.dp),
                        shape = RoundedCornerShape(24.dp),
                        color = LumisGreenLight,
                        tonalElevation = 0.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(R.drawable.mulher_limpando),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 8.dp)
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        LumisTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                mensagemErro = null
                            },
                            label = "E-mail",
                            placeholder = "voce@exemplo.com",
                            leadingIcon = Icons.Outlined.Email,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )
                        LumisTextField(
                            value = senha,
                            onValueChange = {
                                senha = it
                                mensagemErro = null
                            },
                            label = "Senha",
                            leadingIcon = Icons.Outlined.Lock,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = if (senhaVisivel) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            trailingIcon = {
                                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                                    Icon(
                                        imageVector = if (senhaVisivel) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                        contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha"
                                    )
                                }
                            },
                            isError = mensagemErro != null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    AnimatedVisibility(visible = mensagemErro != null || authController.mensagemLogin != null) {
                        val erro = mensagemErro
                        Text(
                            text = erro ?: authController.mensagemLogin.orEmpty(),
                            color = if (erro != null) MaterialTheme.colorScheme.error else LumisGreen,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    LumisPrimaryButton(
                        text = "Entrar",
                        loading = carregando,
                        enabled = email.isNotBlank() && senha.isNotBlank(),
                        onClick = {
                            scope.launch {
                                carregando = true
                                mensagemErro = authController.login(email, senha)
                                carregando = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ainda não tem conta? ",
                            color = LumisTextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Criar conta",
                            color = LumisGreen,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { authController.abrirCadastro() }
                                .padding(4.dp)
                        )
                    }
                    Spacer(Modifier.size(8.dp))
                }
            }
        }
    }
}
