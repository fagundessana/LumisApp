package com.example.lumis.view

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.lumis.controller.AuthController
import com.example.lumis.data.Agendamento
import com.example.lumis.data.AgendamentoRepository
import com.example.lumis.data.AppDatabase
import com.example.lumis.data.User
import com.example.lumis.model.Servico
import com.example.lumis.view.components.AgendaTab
import com.example.lumis.view.components.AgendamentoRegistro
import com.example.lumis.view.components.BottomNavBar
import com.example.lumis.view.components.NavItem
import com.example.lumis.view.screens.auth.ContaScreen
import com.example.lumis.view.screens.city.CidadeConscienteScreen
import com.example.lumis.view.screens.client_profile.UsuarioProfile
import com.example.lumis.view.screens.client_profile.UsuarioProfileScreen
import com.example.lumis.view.screens.employee_profile.FuncionarioProfile
import com.example.lumis.view.screens.employee_profile.FuncionarioProfileScreen
import com.example.lumis.view.screens.notifications.NotificationsScreen
import com.example.lumis.view.screens.profile.EditarPerfilScreen
import com.example.lumis.view.screens.home.HomeScreen
import com.example.lumis.view.screens.services.AgendamentoScreen
import com.example.lumis.view.screens.services.ConfiguracaoImovelScreen
import com.example.lumis.view.screens.services.PlanosScreen
import com.example.lumis.view.screens.services.PagamentoScreen
import com.example.lumis.view.screens.services.ResumoPedidoScreen
import com.example.lumis.view.screens.services.ServicosAdicionaisScreen
import com.example.lumis.view.screens.services.ServicesScreen
import com.example.lumis.view.theme.LumisBackground
import com.example.lumis.view.theme.ModoTema
import kotlinx.coroutines.launch
import java.util.Calendar

sealed class LumisTela {
    data class Principal(val aba: NavItem) : LumisTela()
    data object Planos : LumisTela()
    data class Agendamento(val servico: Servico) : LumisTela()
    data object Imovel : LumisTela()
    data object Adicionais : LumisTela()
    data object Resumo : LumisTela()
    data object Pagamento : LumisTela()
    data object Conta : LumisTela()
    data object EditarPerfil : LumisTela()
    data object Notificacoes : LumisTela()
}

@Composable
fun MainScaffold(
    usuario: User,
    authController: AuthController,
    onLogout: () -> Unit,
    modoTema: ModoTema = ModoTema.SISTEMA,
    onModoTemaChange: (ModoTema) -> Unit = {}
) {
    val context = LocalContext.current
    val database = remember(context) { AppDatabase.getDatabase(context) }
    val agendamentoRepository = remember(database) {
        AgendamentoRepository(database.agendamentoDao())
    }
    val scope = rememberCoroutineScope()

    var telaAtual by remember { mutableStateOf<LumisTela>(LumisTela.Principal(NavItem.HOME)) }

    var servicoEscolhido by remember { mutableStateOf<Servico?>(null) }
    var servicoInicial by remember { mutableStateOf<String?>(null) }
    var dataEscolhida by remember { mutableStateOf("") }
    var horarioEscolhido by remember { mutableStateOf("") }
    var quartosCount by remember { mutableStateOf(0) }
    var banheirosCount by remember { mutableStateOf(0) }
    var tamanhoImovel by remember { mutableStateOf("") }
    var extrasEscolhidos by remember { mutableStateOf<List<String>>(emptyList()) }
    var totalPedido by remember { mutableStateOf(0.0) }
    var registrosAgenda by remember { mutableStateOf<List<AgendamentoRegistro>>(emptyList()) }
    var formaPagamento by remember { mutableStateOf("A combinar no atendimento") }
    var enderecoImovel by remember { mutableStateOf("") }

    LaunchedEffect(usuario.id, agendamentoRepository) {
        registrosAgenda = agendamentoRepository
            .listarPorUsuario(usuario.id)
            .map { salvo ->
                AgendamentoRegistro(
                    id = salvo.id,
                    servico = salvo.servico,
                    data = salvo.data,
                    horario = salvo.horario,
                    endereco = salvo.endereco,
                    extras = salvo.extras
                        .split('\n')
                        .filter { it.isNotBlank() },
                    total = salvo.totalCentavos / 100.0,
                    status = salvo.status,
                    avaliacao = salvo.avaliacao
                )
            }
    }

    fun irPara(nova: LumisTela) {
        telaAtual = nova
    }

    // Mapeia o atalho da Home (categoria/serviço/recomendado) para um serviço existente.
    // Se não houver correspondência, abre a aba de serviços normalmente.
    fun abrirServicoPorLegenda(legenda: String) {
        val label = legenda.lowercase()
        val mapa = listOf(
            "limpeza pesada" to "Limpeza Pesada",
            "pós-mudança" to "Limpeza Pós-Mudança",
            "pos-mudanca" to "Limpeza Pós-Mudança",
            "pós-reforma" to "Limpeza Pós-Mudança",
            "pos-reforma" to "Limpeza Pós-Mudança",
            "airbnb" to "Limpeza Airbnb / Temporada",
            "limpeza geral" to "Limpeza Padrão",
            "residencial" to "Limpeza Padrão",
            "casas e aptos" to "Limpeza Padrão",
            "comercial" to "Limpeza Pesada",
            "escritórios" to "Limpeza Pesada",
            "empresas" to "Limpeza Pesada",
            "limpeza padrão" to "Limpeza Padrão",
            "limpeza" to "Limpeza Padrão",
            "geral" to "Limpeza Padrão",
            "expansao" to "Limpeza Padrão",
            "posreforma" to "Limpeza Pós-Mudança"
        )
        servicoInicial = mapa
            .firstOrNull { label.contains(it.first) }
            ?.second
        irPara(LumisTela.Principal(NavItem.EXPLORAR))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LumisBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = telaAtual,
                    transitionSpec = {
                        (fadeIn(tween(220)) + scaleIn(tween(220), initialScale = 0.985f))
                            .togetherWith(fadeOut(tween(140)))
                    },
                    label = "screenTransition"
                ) { telaDestino ->
                when (val tela = telaDestino) {
                    is LumisTela.Principal -> when (tela.aba) {
                        NavItem.HOME -> HomeScreen(
                            localizacao = usuario.localizacao,
                            onLocationClick = { irPara(LumisTela.EditarPerfil) },
                            onNotificationClick = { irPara(LumisTela.Notificacoes) },
                            onSeeAllCategories = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                            onSeeAllServices = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                            onDiscountClick = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                            onCategoryClick = { abrirServicoPorLegenda(it) },
                            onServiceClick = { abrirServicoPorLegenda(it) }
                        )
                        NavItem.CIDADE -> CidadeConscienteScreen(onNotificationsClick = { irPara(LumisTela.Notificacoes) })
                        NavItem.AGENDA -> AgendaTab(
                            registros = registrosAgenda,
                            onAvaliar = { id, estrelas ->
                                scope.launch {
                                    if (agendamentoRepository.avaliar(id, usuario.id, estrelas)) {
                                        registrosAgenda = registrosAgenda.map { registro ->
                                            if (registro.id == id) registro.copy(avaliacao = estrelas) else registro
                                        }
                                    }
                                }
                            },
                            onCancelar = { id ->
                                scope.launch {
                                    if (agendamentoRepository.atualizarStatus(id, usuario.id, "Cancelado")) {
                                        registrosAgenda = registrosAgenda.map { if (it.id == id) it.copy(status = "Cancelado") else it }
                                    }
                                }
                            },
                            onConcluir = { id ->
                                scope.launch {
                                    if (agendamentoRepository.atualizarStatus(id, usuario.id, "Concluído")) {
                                        registrosAgenda = registrosAgenda.map { if (it.id == id) it.copy(status = "Concluído") else it }
                                    }
                                }
                            },
                            onReagendar = { id, data, horario ->
                                scope.launch {
                                    if (agendamentoRepository.reagendar(id, usuario.id, data, horario)) {
                                        registrosAgenda = registrosAgenda.map { if (it.id == id) it.copy(data = data, horario = horario) else it }
                                    } else {
                                        Toast.makeText(context, "Esse horário já está ocupado.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                        )
                        NavItem.PERFIL -> if (usuario.tipoUsuario == "Cliente") {
                            UsuarioProfileScreen(
                                profile = UsuarioProfile(
                                    nome = usuario.nomeUsuario,
                                    localizacao = usuario.localizacao.ifBlank { "Adicionar localização" },
                                    fotoUrl = usuario.fotoPerfilPath.ifBlank { null },
                                    biografia = usuario.biografia,
                                    bannerUrl = usuario.bannerPerfilPath.ifBlank { null }
                                ),
                                onEditarClick = { irPara(LumisTela.EditarPerfil) },
                                onConfigClick = { irPara(LumisTela.Conta) },
                                onMembroLumisClick = { irPara(LumisTela.Planos) },
                                onAtalhoClick = { atalho ->
                                    when (atalho.label) {
                                        "Meus dados" -> irPara(LumisTela.EditarPerfil)
                                        "Notificações" -> irPara(LumisTela.Notificacoes)
                                        "Serviços" -> irPara(LumisTela.Principal(NavItem.EXPLORAR))
                                        "Planos" -> irPara(LumisTela.Planos)
                                        else -> irPara(LumisTela.Principal(NavItem.HOME))
                                    }
                                },
                                onAtividadeClick = { irPara(LumisTela.Principal(NavItem.AGENDA)) },
                                onConcluirTarefa = { irPara(LumisTela.Principal(NavItem.CIDADE)) }
                            )
                        } else {
                            FuncionarioProfileScreen(
                                profile = FuncionarioProfile(
                                    nome = usuario.nomeUsuario,
                                    localizacao = usuario.localizacao.ifBlank { "Adicionar localização" },
                                    telefone = usuario.telefone.ifBlank { "Adicionar telefone" },
                                    fotoPath = usuario.fotoPerfilPath.ifBlank { null },
                                    bannerPath = usuario.bannerPerfilPath.ifBlank { null },
                                    biografia = usuario.biografia,
                                    especialidades = usuario.especialidades,
                                    regiaoAtuacao = usuario.regiaoAtuacao,
                                    experiencia = usuario.experiencia,
                                    valorMinimoCentavos = usuario.valorMinimoCentavos,
                                    mediaAvaliacoes = registrosAgenda.mapNotNull { it.avaliacao }.takeIf { it.isNotEmpty() }?.average(),
                                    quantidadeAvaliacoes = registrosAgenda.count { it.avaliacao != null }
                                ),
                                onEditarClick = { irPara(LumisTela.EditarPerfil) },
                                onConfigClick = { irPara(LumisTela.Conta) },
                                onCalendarioClick = { irPara(LumisTela.Principal(NavItem.AGENDA)) }
                            )
                        }
                        NavItem.EXPLORAR -> ServicesScreen(
                            onAbrirPlanos = { irPara(LumisTela.Planos) },
                            onAbrirAgendamento = { servico ->
                                servicoEscolhido = servico
                                irPara(LumisTela.Agendamento(servico))
                            },
                            servicoInicial = servicoInicial,
                            onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                        )
                    }
                    is LumisTela.Planos -> PlanosScreen(
                        onVoltarServicos = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                        onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                    )
                    is LumisTela.Agendamento -> AgendamentoScreen(
                        servicoSelecionado = tela.servico.nome,
                        onBack = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                        onConfirm = { dia, hora ->
                            val cal = Calendar.getInstance()
                            val mesAtual = cal.get(Calendar.MONTH) + 1
                            val anoAtual = cal.get(Calendar.YEAR)
                            dataEscolhida = "$dia/${mesAtual.toString().padStart(2, '0')}/$anoAtual"
                            horarioEscolhido = hora
                            irPara(LumisTela.Imovel)
                        },
                        onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                    )
is LumisTela.Imovel -> ConfiguracaoImovelScreen(
                        servico = servicoEscolhido?.nome,
                        data = dataEscolhida,
                        horario = horarioEscolhido,
                        endereco = enderecoImovel,
                        onBack = {
                            irPara(
                                servicoEscolhido?.let { LumisTela.Agendamento(it) }
                                    ?: LumisTela.Principal(NavItem.EXPLORAR)
                            )
                        },
                        onConfirm = { q, b, tamanho, endereco ->
                            quartosCount = q
                            banheirosCount = b
                            tamanhoImovel = tamanho
                            enderecoImovel = endereco
                            irPara(LumisTela.Adicionais)
                        },
                        onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                    )
                    is LumisTela.Adicionais -> ServicosAdicionaisScreen(
                        servico = servicoEscolhido?.nome,
                        datahora = if (dataEscolhida.isBlank()) null else "$dataEscolhida às $horarioEscolhido",
                        onBack = { irPara(LumisTela.Imovel) },
                        onConfirm = { subtotal, adicionais ->
                            extrasEscolhidos = adicionais
                            totalPedido = subtotal
                            irPara(LumisTela.Resumo)
                        },
                        onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                    )
                    is LumisTela.Resumo -> ResumoPedidoScreen(
                        servico = servicoEscolhido?.nome ?: "",
                        data = dataEscolhida,
                        horario = horarioEscolhido,
                        quartos = quartosCount,
                        banheiros = banheirosCount,
                        tamanho = tamanhoImovel,
                        extras = extrasEscolhidos,
                        total = totalPedido,
                        endereco = enderecoImovel,
                        formaPagamentoInicial = formaPagamento,
                        onFormaPagamentoChange = { formaPagamento = it },
                        onBack = { irPara(LumisTela.Adicionais) },
                        onConfirm = { formaSelecionada ->
                            formaPagamento = formaSelecionada
                            irPara(LumisTela.Pagamento)
                        },
                        onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                    )
                    is LumisTela.Pagamento -> PagamentoScreen(
                        formaPagamento = formaPagamento,
                        total = totalPedido,
                        onBack = { irPara(LumisTela.Resumo) },
                        onConcluir = {
                            val servico = servicoEscolhido
                            if (servico != null) {
                                val novoAgendamento = Agendamento(
                                    usuarioId = usuario.id,
                                    servico = servico.nome,
                                    data = dataEscolhida,
                                    horario = horarioEscolhido,
                                    quartos = quartosCount,
                                    banheiros = banheirosCount,
                                    tamanhoImovel = tamanhoImovel,
                                    extras = extrasEscolhidos.joinToString("\n"),
                                    endereco = enderecoImovel,
                                    formaPagamento = formaPagamento,
                                    totalCentavos = (totalPedido * 100.0).toLong()
                                )
                                scope.launch {
                                    try {
                                        agendamentoRepository.salvar(novoAgendamento)
                                        registrosAgenda = agendamentoRepository
                                            .listarPorUsuario(usuario.id)
                                            .map { salvo ->
                                                AgendamentoRegistro(
                                                    id = salvo.id,
                                                    servico = salvo.servico,
                                                    data = salvo.data,
                                                    horario = salvo.horario,
                                                    endereco = salvo.endereco,
                                                    extras = salvo.extras
                                                        .split('\n')
                                                        .filter { it.isNotBlank() },
                                                    total = salvo.totalCentavos / 100.0,
                                                    status = salvo.status,
                                                    avaliacao = salvo.avaliacao
                                                )
                                            }
                                        irPara(LumisTela.Principal(NavItem.AGENDA))
                                    } catch (erro: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Não foi possível salvar o agendamento. Tente novamente.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                        },
                        onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                    )
                    is LumisTela.Conta -> ContaScreen(
                        usuario = usuario,
                        onVoltar = { irPara(LumisTela.Principal(NavItem.PERFIL)) },
                        onEditarPerfil = { irPara(LumisTela.EditarPerfil) },
                        onSair = onLogout,
                        modoTema = modoTema,
                        onModoTemaChange = onModoTemaChange,
                        onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                    )
                    is LumisTela.EditarPerfil -> EditarPerfilScreen(
                        usuario = usuario,
                        authController = authController,
                        onVoltar = { irPara(LumisTela.Principal(NavItem.PERFIL)) },
                        onSalvo = { irPara(LumisTela.Principal(NavItem.PERFIL)) },
                        onNotificationsClick = { irPara(LumisTela.Notificacoes) }
                    )
                    is LumisTela.Notificacoes -> NotificationsScreen(
                        onBack = { irPara(LumisTela.Principal(NavItem.PERFIL)) }
                    )
                }
                }
            }

            BottomNavBar(
                selected = when (val tela = telaAtual) {
                    is LumisTela.Principal -> tela.aba
                    is LumisTela.Conta -> NavItem.PERFIL
                    is LumisTela.EditarPerfil -> NavItem.PERFIL
                    else -> NavItem.EXPLORAR
                },
                onSelect = { novaAba -> telaAtual = LumisTela.Principal(novaAba) }
            )
        }
    }
}
