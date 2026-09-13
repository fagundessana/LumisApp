package com.example.lumis.ui.screens

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lumis.R
import com.example.lumis.data.AppDatabase
import com.example.lumis.data.User
import kotlinx.coroutines.launch

private val Fundo = Color(0xFFF9F9FB)
private val VerdeCliente = Color(0xFF50C58A)
private val VerdeProfissional = Color(0xFF3DB8B1)
private val VerdeTexto = Color(0xFF43BD80)
private val AzulLink = Color(0xFF4389C9)
private val TextoPrincipal = Color(0xFF222222)
private val TextoSecundario = Color(0xFF777777)

@Composable
fun CadastroScreen(
    database: AppDatabase
) {

    var email by remember { mutableStateOf("") }
    var nomeUsuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmacaoSenha by remember { mutableStateOf("") }

    var tipoUsuario by remember { mutableStateOf("Cliente") }

    var senhaVisivel by remember { mutableStateOf(false) }
    var confirmacaoVisivel by remember { mutableStateOf(false) }

    var mensagemErro by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
            .verticalScroll(rememberScrollState())
            .padding(
                start = 25.dp,
                end = 25.dp,
                top = 15.dp,
                bottom = 25.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // =========================================================
        // ILUSTRAÇÃO
        // =========================================================

        Image(
            painter = painterResource(
                id = R.drawable.ilustracao_cadastro
            ),
            contentDescription = "Ilustração de cadastro",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(215.dp)
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        // =========================================================
        // TÍTULO
        // =========================================================

        Text(
            text = buildAnnotatedString {
                append("Faça parte de nossa ")

                withStyle(
                    SpanStyle(
                        color = VerdeProfissional
                    )
                ) {
                    append("comunidade")
                }
            },
            fontSize = 14.sp,
            color = TextoPrincipal,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "Selecione o seu tipo de usuário",
            fontSize = 14.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        // =========================================================
        // BOTÃO CLIENTE
        // =========================================================

        Button(
            onClick = {
                tipoUsuario = "Cliente"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(9.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (tipoUsuario == "Cliente")
                    Color.White
                else
                    VerdeCliente,

                contentColor = if (tipoUsuario == "Cliente")
                    VerdeCliente
                else
                    Color.White
            ),
            border = if (tipoUsuario == "Cliente") {
                BorderStroke(
                    2.dp,
                    VerdeCliente
                )
            } else {
                null
            }
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.PersonOutline,
                    contentDescription = "Cliente",
                    modifier = Modifier.size(34.dp)
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Cliente",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Spacer(
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        // =========================================================
        // BOTÃO PROFISSIONAL
        // =========================================================

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {
                tipoUsuario = "Profissional"
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(9.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (tipoUsuario == "Profissional")
                    Color.White
                else
                    VerdeProfissional,

                contentColor = if (tipoUsuario == "Profissional")
                    VerdeProfissional
                else
                    Color.White
            ),
            border = if (tipoUsuario == "Profissional") {
                BorderStroke(
                    2.dp,
                    VerdeProfissional
                )
            } else {
                null
            }
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Engineering,
                    contentDescription = "Profissional",
                    modifier = Modifier.size(34.dp)
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Profissional",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Spacer(
                    modifier = Modifier.size(34.dp)
                )
            }
        }

        // =========================================================
        // EMAIL
        // =========================================================

        CampoCadastro(
            valor = email,
            aoMudar = {
                email = it
            },
            placeholder = "Digite seu email"
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =========================================================
        // NOME DE USUÁRIO
        // =========================================================

        CampoCadastro(
            valor = nomeUsuario,
            aoMudar = {
                nomeUsuario = it
            },
            placeholder = "Nome de usuário"
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =========================================================
        // SENHA
        // =========================================================

        CampoSenha(
            valor = senha,
            aoMudar = {
                senha = it
            },
            placeholder = "Senha",
            visivel = senhaVisivel,
            aoAlternarVisibilidade = {
                senhaVisivel = !senhaVisivel
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // =========================================================
        // CONFIRMAÇÃO
        // =========================================================

        CampoSenha(
            valor = confirmacaoSenha,
            aoMudar = {
                confirmacaoSenha = it
            },
            placeholder = "Confirmação de senha",
            visivel = confirmacaoVisivel,
            aoAlternarVisibilidade = {
                confirmacaoVisivel = !confirmacaoVisivel
            }
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =========================================================
        // ERRO
        // =========================================================

        if (mensagemErro.isNotEmpty()) {

            Text(
                text = mensagemErro,
                color = Color.Red,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }

        // =========================================================
        // TERMOS
        // =========================================================

        Text(
            text = buildAnnotatedString {

                append("Ao se registrar, você concorda com nossos\n")

                withStyle(
                    SpanStyle(
                        color = AzulLink
                    )
                ) {
                    append("Termos de uso")
                }

                append(" e a nossa ")

                withStyle(
                    SpanStyle(
                        color = AzulLink
                    )
                ) {
                    append("Política de Privacidade")
                }
            },
            fontSize = 11.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(23.dp)
        )

        // =========================================================
        // BOTÃO CADASTRAR
        // =========================================================

        Button(
            onClick = {

                mensagemErro = ""

                when {

                    email.isBlank() -> {
                        mensagemErro = "Digite seu email"
                    }

                    !Patterns.EMAIL_ADDRESS
                        .matcher(email)
                        .matches() -> {

                        mensagemErro = "Digite um email válido"
                    }

                    nomeUsuario.isBlank() -> {
                        mensagemErro = "Digite seu nome de usuário"
                    }

                    senha.isBlank() -> {
                        mensagemErro = "Digite uma senha"
                    }

                    confirmacaoSenha.isBlank() -> {
                        mensagemErro = "Confirme sua senha"
                    }

                    senha != confirmacaoSenha -> {
                        mensagemErro = "As senhas não coincidem"
                    }

                    else -> {

                        scope.launch {

                            val existente =
                                database
                                    .userDao()
                                    .buscarPorEmail(email)

                            if (existente != null) {

                                mensagemErro =
                                    "Este email já está cadastrado"

                            } else {

                                val user = User(
                                    email = email,
                                    nomeUsuario = nomeUsuario,
                                    senha = senha,
                                    tipoUsuario = tipoUsuario
                                )

                                database
                                    .userDao()
                                    .inserir(user)

                                mensagemErro =
                                    "Cadastro realizado com sucesso!"

                                email = ""
                                nomeUsuario = ""
                                senha = ""
                                confirmacaoSenha = ""
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Fundo,
                contentColor = VerdeTexto
            ),
            border = BorderStroke(
                width = 1.5.dp,
                color = VerdeTexto
            )
        ) {

            Text(
                text = "Cadastre-se",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(25.dp)
        )

        // =========================================================
        // LOGIN
        // =========================================================

        Text(
            text = buildAnnotatedString {

                append("Já possui uma conta? ")

                withStyle(
                    SpanStyle(
                        color = AzulLink
                    )
                ) {
                    append("Entre")
                }

                append(" agora!")
            },
            fontSize = 12.sp,
            color = TextoPrincipal,
            textAlign = TextAlign.Center,
            modifier = Modifier.clickable {
                // Futuramente abrir a tela de Login
            }
        )
    }
}


// =============================================================
// CAMPO DE TEXTO NORMAL
// =============================================================

@Composable
private fun CampoCadastro(
    valor: String,
    aoMudar: (String) -> Unit,
    placeholder: String
) {

    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 12.sp,
                color = Color.Gray
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(2.dp),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color(0xFF4F67A2),
            cursorColor = Color(0xFF4F67A2)
        )
    )
}


// =============================================================
// CAMPO DE SENHA
// =============================================================

@Composable
private fun CampoSenha(
    valor: String,
    aoMudar: (String) -> Unit,
    placeholder: String,
    visivel: Boolean,
    aoAlternarVisibilidade: () -> Unit
) {

    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 12.sp,
                color = Color.Gray
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(2.dp),
        visualTransformation =
            if (visivel)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
        trailingIcon = {

            IconButton(
                onClick = aoAlternarVisibilidade
            ) {

                Icon(
                    imageVector =
                        if (visivel)
                            Icons.Default.VisibilityOff
                        else
                            Icons.Default.Visibility,
                    contentDescription = "Mostrar senha",
                    tint = Color(0xFF666872)
                )
            }
        },
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color(0xFF4F67A2),
            cursorColor = Color(0xFF4F67A2)
        )
    )
}