package com.example.lumis.controller

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lumis.data.AppDatabase
import com.example.lumis.data.User

// ---------------------------------------------------------
// CONTROLLER DE AUTENTICAÇÃO (cadastro / login / logout)
// ---------------------------------------------------------

class AuthController(context: Context) {

    private val database = AppDatabase.getDatabase(context)

    private val prefs =
        context.getSharedPreferences("lumis_sessao", Context.MODE_PRIVATE)

    // Usuário logado. Quando null, o app fica na tela de login/cadastro.
    var usuarioLogado by mutableStateOf<User?>(null)
        private set

    // Alterna entre a tela de login e a de cadastro.
    var mostrarCadastro by mutableStateOf(false)
        private set

    // Mensagem de sucesso exibida na tela de login (ex.: após cadastro).
    var mensagemLogin by mutableStateOf<String?>(null)
        private set

    fun abrirLogin() {
        mostrarCadastro = false
    }

    fun abrirCadastro() {
        mostrarCadastro = true
    }

    // Restaura a sessão do último usuário logado (nas próximas aberturas do app).
    suspend fun restaurarSessao() {
        val email = prefs.getString("ultimo_email", null) ?: return
        val user = database.userDao().buscarPorEmail(email) ?: return
        usuarioLogado = user
    }

    // Retorna null em caso de sucesso, ou uma mensagem de erro.
    suspend fun login(email: String, senha: String): String? {
        val user = database.userDao().buscarPorEmail(email.trim())

        when {
            user == null -> return "Email não cadastrado"
            user.senha != senha -> return "Senha incorreta"
        }

        usuarioLogado = user
        mensagemLogin = null
        prefs.edit().putString("ultimo_email", user.email).apply()
        return null
    }

    // Retorna null em caso de sucesso, ou uma mensagem de erro.
    // Em caso de sucesso, volta para a tela de login com a mensagem de confirmação.
    suspend fun cadastrar(
        email: String,
        nomeUsuario: String,
        senha: String,
        confirmacaoSenha: String,
        tipoUsuario: String
    ): String? {
        val dao = database.userDao()
        val emailLimpo = email.trim()

        if (senha != confirmacaoSenha) return "As senhas não coincidem"

        if (dao.buscarPorEmail(emailLimpo) != null) {
            return "Este email já está cadastrado"
        }

        dao.inserir(
            User(
                email = emailLimpo,
                nomeUsuario = nomeUsuario.trim(),
                senha = senha,
                tipoUsuario = tipoUsuario
            )
        )

        mostrarCadastro = false
        mensagemLogin = "Cadastro realizado com sucesso! Faça login."
        return null
    }

    fun logout() {
        usuarioLogado = null
        mensagemLogin = null
        mostrarCadastro = false
        prefs.edit().remove("ultimo_email").apply()
    }
}