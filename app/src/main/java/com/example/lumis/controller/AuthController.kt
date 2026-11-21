package com.example.lumis.controller

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lumis.data.AppDatabase
import com.example.lumis.data.Agendamento
import com.example.lumis.data.User

// Número máximo de caracteres da biografia do perfil.
const val LIMITE_BIOGRAFIA = 300

enum class AuthRoute { LOGIN, ESCOLHA_PERFIL, CADASTRO }

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

    var rotaAuth by mutableStateOf(AuthRoute.LOGIN)
        private set

    var tipoCadastroInicial by mutableStateOf("Cliente")
        private set

    val mostrarCadastro: Boolean get() = rotaAuth == AuthRoute.CADASTRO
    val mostrarEscolhaPerfil: Boolean get() = rotaAuth == AuthRoute.ESCOLHA_PERFIL

    // Mensagem de sucesso exibida na tela de login (ex.: após cadastro).
    var mensagemLogin by mutableStateOf<String?>(null)
        private set

    fun abrirLogin() {
        rotaAuth = AuthRoute.LOGIN
    }

    suspend fun garantirDadosDemo() {
        val demonstrativos = listOf(
            User(
                email = "ana.demo@lumis.app",
                nomeUsuario = "Ana Beatriz Santos",
                senha = "Lumis@Ana1",
                tipoUsuario = "Profissional",
                localizacao = "São Paulo, SP",
                telefone = "(11) 99911-2401",
                biografia = "Especialista em limpeza residencial cuidadosa e organização de ambientes.",
                especialidades = "Limpeza padrão, organização e limpeza pesada",
                regiaoAtuacao = "São Paulo — Zona Sul e Centro",
                experiencia = "5 anos de experiência",
                valorMinimoCentavos = 12_000,
                isDemo = true
            ),
            User(
                email = "carlos.demo@lumis.app",
                nomeUsuario = "Carlos Henrique Lima",
                senha = "Lumis@Carlos1",
                tipoUsuario = "Profissional",
                localizacao = "São Paulo, SP",
                telefone = "(11) 99922-3512",
                biografia = "Atendimento ágil para escritórios, mudanças e limpezas de maior porte.",
                especialidades = "Limpeza pesada, pós-mudança e comercial",
                regiaoAtuacao = "São Paulo e ABC Paulista",
                experiencia = "7 anos de experiência",
                valorMinimoCentavos = 18_000,
                isDemo = true
            ),
            User(
                email = "mariana.demo@lumis.app",
                nomeUsuario = "Mariana Costa",
                senha = "Lumis@Mariana1",
                tipoUsuario = "Profissional",
                localizacao = "Curitiba, PR",
                telefone = "(41) 99880-4623",
                biografia = "Foco em hospedagens, preparação entre reservas e padrão de hotelaria.",
                especialidades = "Airbnb, temporada e limpeza padrão",
                regiaoAtuacao = "Curitiba e região metropolitana",
                experiencia = "4 anos de experiência",
                valorMinimoCentavos = 15_000,
                isDemo = true
            ),
            User(
                email = "cliente.demo@lumis.app",
                nomeUsuario = "Cliente Demonstração",
                senha = "Lumis@Cliente1",
                tipoUsuario = "Cliente",
                localizacao = "São Paulo, SP",
                biografia = "Conta preparada para demonstrar o fluxo completo do aplicativo.",
                isDemo = true
            )
        )

        demonstrativos.forEach { usuarioDemo ->
            if (database.userDao().buscarPorEmail(usuarioDemo.email) == null) {
                database.userDao().inserir(usuarioDemo)
            }
        }

        val clienteDemo = database.userDao().buscarPorEmail("cliente.demo@lumis.app") ?: return
        val avaliacoesDemo = mapOf(
            "ana.demo@lumis.app" to listOf(5, 5),
            "carlos.demo@lumis.app" to listOf(5, 4),
            "mariana.demo@lumis.app" to listOf(5, 5)
        )

        avaliacoesDemo.forEach { (email, avaliacoes) ->
            val profissional = database.userDao().buscarPorEmail(email) ?: return@forEach
            if (database.agendamentoDao().listarPorProfissional(profissional.id).isEmpty()) {
                avaliacoes.forEachIndexed { indice, estrelas ->
                    database.agendamentoDao().inserir(
                        Agendamento(
                            usuarioId = clienteDemo.id,
                            profissionalId = profissional.id,
                            servico = if (indice == 0) "Limpeza padrão" else "Limpeza pesada",
                            data = if (indice == 0) "12/10/2026" else "26/10/2026",
                            horario = if (indice == 0) "09:00" else "14:00",
                            quartos = 2,
                            banheiros = 1,
                            tamanhoImovel = "Médio",
                            endereco = "Endereço de demonstração",
                            formaPagamento = "Pix",
                            totalCentavos = profissional.valorMinimoCentavos,
                            status = "Concluído",
                            avaliacao = estrelas
                        )
                    )
                }
            }
        }
    }

    fun abrirCadastro() {
        rotaAuth = AuthRoute.ESCOLHA_PERFIL
    }

    fun cadastrarComo(tipoUsuario: String) {
        tipoCadastroInicial = tipoUsuario
        rotaAuth = AuthRoute.CADASTRO
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
        tipoUsuario: String,
        localizacao: String = ""
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
                tipoUsuario = tipoUsuario,
                localizacao = localizacao.trim()
            )
        )

        rotaAuth = AuthRoute.LOGIN
        mensagemLogin = "Cadastro realizado com sucesso! Faça login."
        return null
    }

    // Atualiza nome, localização, telefone, foto, banner e biografia do usuário logado.
    // Telefone e biografia são opcionais; telefone preenchido exige 10 ou 11 dígitos,
    // biografia tem no máximo 300 caracteres.
    // Retorna null em caso de sucesso, ou mensagem de erro.
    suspend fun atualizarPerfil(
        nomeUsuario: String,
        localizacao: String,
        telefone: String = "",
        fotoPerfilPath: String = "",
        bannerPerfilPath: String = "",
        biografia: String = "",
        especialidades: String = "",
        regiaoAtuacao: String = "",
        experiencia: String = "",
        valorMinimoCentavos: Long = 0
    ): String? {
        val atual = usuarioLogado ?: return "Nenhum usuário logado"
        val nomeLimpo = nomeUsuario.trim()
        val locLimpa = localizacao.trim()
        val telLimpo = telefone.trim()
        val fotoLimpa = fotoPerfilPath.trim()
        val bannerLimpo = bannerPerfilPath.trim()
        val bioLimpa = biografia.trim()

        if (nomeLimpo.isBlank()) return "Digite seu nome de usuário"

        if (telLimpo.isNotBlank()) {
            val digitos = telLimpo.filter { it.isDigit() }
            if (digitos.length !in 10..11) return "Digite um telefone válido com DDD"
        }

        if (bioLimpa.length > LIMITE_BIOGRAFIA) return "A biografia deve ter no máximo $LIMITE_BIOGRAFIA caracteres"

        val atualizado = atual.copy(
            nomeUsuario = nomeLimpo,
            localizacao = locLimpa,
            telefone = telLimpo,
            fotoPerfilPath = fotoLimpa,
            bannerPerfilPath = bannerLimpo,
            biografia = bioLimpa,
            especialidades = especialidades.trim(),
            regiaoAtuacao = regiaoAtuacao.trim(),
            experiencia = experiencia.trim(),
            valorMinimoCentavos = valorMinimoCentavos.coerceAtLeast(0)
        )
        database.userDao().atualizar(atualizado)
        usuarioLogado = database.userDao().buscarPorId(atual.id) ?: atualizado
        return null
    }

    // Atalho rápido usado pela Home/TopLocationBar para salvar só a região.
    suspend fun atualizarLocalizacao(localizacao: String): String? {
        val atual = usuarioLogado ?: return "Nenhum usuário logado"
        return atualizarPerfil(
            nomeUsuario = atual.nomeUsuario,
            localizacao = localizacao,
            telefone = atual.telefone,
            fotoPerfilPath = atual.fotoPerfilPath,
            bannerPerfilPath = atual.bannerPerfilPath,
            biografia = atual.biografia,
            especialidades = atual.especialidades,
            regiaoAtuacao = atual.regiaoAtuacao,
            experiencia = atual.experiencia,
            valorMinimoCentavos = atual.valorMinimoCentavos
        )
    }

    fun logout() {
        usuarioLogado = null
        mensagemLogin = null
        rotaAuth = AuthRoute.LOGIN
        prefs.edit().remove("ultimo_email").apply()
    }
}
