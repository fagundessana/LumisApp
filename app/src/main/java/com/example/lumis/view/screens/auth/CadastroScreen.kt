package com.example.lumis.view.screens.auth

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.lumis.controller.AuthController
import com.example.lumis.view.components.LumisPageTitle
import com.example.lumis.view.components.LumisPrimaryButton
import com.example.lumis.view.components.LumisTag
import com.example.lumis.view.components.LumisTextField
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.LumisGreen
import com.example.lumis.view.theme.LumisGreenLight
import com.example.lumis.view.theme.LumisTextSecondary
import kotlinx.coroutines.launch

@Composable
fun CadastroScreen(authController: AuthController) {
    var email by rememberSaveable { mutableStateOf("") }
    var nomeUsuario by rememberSaveable { mutableStateOf("") }
    var localizacao by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmacaoSenha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmacaoVisivel by rememberSaveable { mutableStateOf(false) }
    var mensagemErro by rememberSaveable { mutableStateOf<String?>(null) }
    var carregando by rememberSaveable { mutableStateOf(false) }
    val tipoUsuario = authController.tipoCadastroInicial
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(LumisGreenLight.copy(alpha = 0.68f), LumisBackground, LumisBackground)
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        IconButton(onClick = authController::abrirCadastro) {
            Icon(Icons.Outlined.ArrowBack, contentDescription = "Voltar")
        }

        LumisTag(text = "PERFIL ${tipoUsuario.uppercase()}")
        LumisPageTitle(
            title = if (tipoUsuario == "Cliente") "Crie sua conta" else "Crie seu perfil profissional",
            subtitle = if (tipoUsuario == "Cliente") {
                "Encontre profissionais verificados e acompanhe seus serviços em um só lugar."
            } else {
                "Apresente seu trabalho, organize sua agenda e encontre novas oportunidades."
            }
        )

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            LumisTextField(
                value = nomeUsuario,
                onValueChange = {
                    nomeUsuario = it
                    mensagemErro = null
                },
                label = if (tipoUsuario == "Cliente") "Nome" else "Nome profissional",
                leadingIcon = Icons.Outlined.PersonOutline,
                modifier = Modifier.fillMaxWidth()
            )
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
                value = localizacao,
                onValueChange = { localizacao = it },
                label = if (tipoUsuario == "Cliente") "Cidade ou região (opcional)" else "Região de atuação (opcional)",
                leadingIcon = Icons.Outlined.LocationOn,
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
                visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            if (senhaVisivel) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            LumisTextField(
                value = confirmacaoSenha,
                onValueChange = {
                    confirmacaoSenha = it
                    mensagemErro = null
                },
                label = "Confirmar senha",
                leadingIcon = Icons.Outlined.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (confirmacaoVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmacaoVisivel = !confirmacaoVisivel }) {
                        Icon(
                            if (confirmacaoVisivel) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (confirmacaoVisivel) "Ocultar senha" else "Mostrar senha"
                        )
                    }
                },
                isError = mensagemErro != null,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AnimatedVisibility(mensagemErro != null) {
            Text(
                text = mensagemErro.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "Ao continuar, você concorda com os Termos de Uso e a Política de Privacidade da Lumis.",
            color = LumisTextSecondary,
            style = MaterialTheme.typography.bodySmall
        )

        LumisPrimaryButton(
            text = "Criar conta",
            loading = carregando,
            enabled = email.isNotBlank() && nomeUsuario.isNotBlank() && senha.isNotBlank() && confirmacaoSenha.isNotBlank(),
            onClick = {
                mensagemErro = when {
                    !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Digite um e-mail válido"
                    senha.length < 6 -> "A senha deve ter pelo menos 6 caracteres"
                    senha != confirmacaoSenha -> "As senhas não coincidem"
                    else -> null
                }
                if (mensagemErro == null) {
                    scope.launch {
                        carregando = true
                        mensagemErro = authController.cadastrar(
                            email = email,
                            nomeUsuario = nomeUsuario,
                            senha = senha,
                            confirmacaoSenha = confirmacaoSenha,
                            tipoUsuario = tipoUsuario,
                            localizacao = localizacao
                        )
                        carregando = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Já tem uma conta? ", color = LumisTextSecondary, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Entrar",
                color = LumisGreen,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .clickable(onClick = authController::abrirLogin)
                    .padding(4.dp)
            )
        }
        Spacer(Modifier.size(12.dp))
    }
}
