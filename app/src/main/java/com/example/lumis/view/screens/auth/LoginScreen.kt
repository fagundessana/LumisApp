package com.example.lumis.view.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.R
import com.example.lumis.controller.AuthController
// Reaproveita a paleta de cores usada nas telas de perfil, para manter a mesma
// identidade visual em todo o app.
import com.example.lumis.view.screens.employee_profile.ProfileColors
import kotlinx.coroutines.launch

// ---------------------------------------------------------------------------------------------
// TOKENS LOCAIS
// ---------------------------------------------------------------------------------------------

private val LoginBlue = Color(0xFF3D7BF5)

// ---------------------------------------------------------------------------------------------
// TELA PRINCIPAL
// ---------------------------------------------------------------------------------------------

@Composable
fun LoginScreen(
    authController: AuthController
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = ProfileColors.CardBackground
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isWideScreen = maxWidth > 600.dp
            val contentMaxWidth = if (isWideScreen) 420.dp else maxWidth
            val horizontalPadding = if (isWideScreen) (maxWidth - contentMaxWidth) / 2 else 24.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Ilustração
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mulher_limpando),
                        contentDescription = "Mulher limpando",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // "Seja bem-vindo(a) de volta"
                Row {
                    Text(
                        text = "Seja ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = ProfileColors.TextPrimary
                    )
                    Text(
                        text = "bem-vindo(a)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ProfileColors.Primary
                    )
                    Text(
                        text = " de volta",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = ProfileColors.TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Campo de email
                LoginTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Digite seu email",
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de senha
                LoginTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    placeholder = "Senha",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                            Icon(
                                imageVector = if (senhaVisivel) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                                tint = ProfileColors.TextSecondary
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Mensagens de erro / sucesso
                val sucesso = authController.mensagemLogin
                when {
                    mensagemErro != null -> {
                        Text(
                            text = mensagemErro!!,
                            color = Color.Red,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    sucesso != null -> {
                        Text(
                            text = sucesso,
                            color = ProfileColors.Primary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Botão "Entrar"
                OutlinedButton(
                    onClick = {
                        mensagemErro = null
                        scope.launch {
                            val erro = authController.login(email, senha)
                            mensagemErro = erro
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = LoginBlue
                    ),
                    border = BorderStroke(2.dp, LoginBlue)
                ) {
                    Text(
                        text = "Entrar",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // "Não tem uma conta? Registre-se"
                Row {
                    Text(
                        text = "Não tem uma conta? ",
                        fontSize = 13.sp,
                        color = ProfileColors.TextSecondary
                    )
                    Text(
                        text = "Registre-se",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ProfileColors.Primary,
                        modifier = Modifier.clickable { authController.abrirCadastro() }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ---------------------------------------------------------------------------------------------
// COMPONENTE AUXILIAR: CAMPO DE TEXTO PADRONIZADO
// ---------------------------------------------------------------------------------------------

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = ProfileColors.TextSecondary,
                fontSize = 14.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ProfileColors.Background,
            unfocusedContainerColor = ProfileColors.Background,
            focusedBorderColor = ProfileColors.Primary,
            unfocusedBorderColor = ProfileColors.Divider,
            cursorColor = ProfileColors.Primary
        )
    )
}