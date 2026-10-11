package com.example.lumis.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.lumis.data.User
import com.example.lumis.model.Servico
import com.example.lumis.view.components.AgendaStore
import com.example.lumis.view.components.AgendaTab
import com.example.lumis.view.components.AgendamentoRegistro
import com.example.lumis.view.components.BottomNavBar
import com.example.lumis.view.components.NavItem
import com.example.lumis.view.screens.auth.ContaScreen
import com.example.lumis.view.screens.client_profile.UsuarioProfile
import com.example.lumis.view.screens.client_profile.UsuarioProfileScreen
import com.example.lumis.view.screens.employee_profile.FuncionarioProfile
import com.example.lumis.view.screens.employee_profile.FuncionarioProfileScreen
import com.example.lumis.view.screens.home.EmBreveScreen
import com.example.lumis.view.screens.home.HomeScreen
import com.example.lumis.view.screens.services.AgendamentoScreen
import com.example.lumis.view.screens.services.ConfiguracaoImovelScreen
import com.example.lumis.view.screens.services.PlanosScreen
import com.example.lumis.view.screens.services.ResumoPedidoScreen
import com.example.lumis.view.screens.services.ServicosAdicionaisScreen
import com.example.lumis.view.screens.services.ServicesScreen
import com.example.lumis.view.theme.LumisBackground
import java.time.YearMonth

sealed class LumisTela {
    data class Principal(val aba: NavItem) : LumisTela()
    data object Planos : LumisTela()
    data class Agendamento(val servico: Servico) : LumisTela()
    data object Imovel : LumisTela()
    data object Adicionais : LumisTela()
    data object Resumo : LumisTela()
    data object Conta : LumisTela()
}

@Composable
fun MainScaffold(
    usuario: User,
    onLogout: () -> Unit
) {
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
                when (val tela = telaAtual) {
                    is LumisTela.Principal -> when (tela.aba) {
                        NavItem.HOME -> HomeScreen(
                            onSeeAllCategories = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                            onSeeAllServices = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                            onSeeAllRecommended = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                            onDiscountClick = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                            onCategoryClick = { abrirServicoPorLegenda(it) },
                            onServiceClick = { abrirServicoPorLegenda(it) },
                            onHireClick = { abrirServicoPorLegenda(it) }
                        )
                        NavItem.CIDADE -> EmBreveScreen()
                        NavItem.AGENDA -> AgendaTab()
                        NavItem.PERFIL -> if (usuario.tipoUsuario == "Cliente") {
                            UsuarioProfileScreen(
                                profile = UsuarioProfile(nome = usuario.nomeUsuario),
                                onConfigClick = { irPara(LumisTela.Conta) }
                            )
                        } else {
                            FuncionarioProfileScreen(
                                profile = FuncionarioProfile(nome = usuario.nomeUsuario),
                                onConfigClick = { irPara(LumisTela.Conta) }
                            )
                        }
                        NavItem.EXPLORAR -> ServicesScreen(
                            onAbrirPlanos = { irPara(LumisTela.Planos) },
                            onAbrirAgendamento = { servico ->
                                servicoEscolhido = servico
                                irPara(LumisTela.Agendamento(servico))
                            },
                            servicoInicial = servicoInicial
                        )
                    }
                    is LumisTela.Planos -> PlanosScreen(
                        onVoltarServicos = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) }
                    )
                    is LumisTela.Agendamento -> AgendamentoScreen(
                        servicoSelecionado = tela.servico.nome,
                        onBack = { irPara(LumisTela.Principal(NavItem.EXPLORAR)) },
                        onConfirm = { dia, hora ->
                            val mesAtual = YearMonth.now()
                            dataEscolhida = "$dia/${mesAtual.monthValue.toString().padStart(2, '0')}/${mesAtual.year}"
                            horarioEscolhido = hora
                            irPara(LumisTela.Imovel)
                        }
                    )
                    is LumisTela.Imovel -> ConfiguracaoImovelScreen(
                        servico = servicoEscolhido?.nome,
                        data = dataEscolhida,
                        horario = horarioEscolhido,
                        onBack = {
                            irPara(
                                servicoEscolhido?.let { LumisTela.Agendamento(it) }
                                    ?: LumisTela.Principal(NavItem.EXPLORAR)
                            )
                        },
                        onConfirm = { q, b, tamanho ->
                            quartosCount = q
                            banheirosCount = b
                            tamanhoImovel = tamanho
                            irPara(LumisTela.Adicionais)
                        }
                    )
                    is LumisTela.Adicionais -> ServicosAdicionaisScreen(
                        servico = servicoEscolhido?.nome,
                        datahora = if (dataEscolhida.isBlank()) null else "$dataEscolhida às $horarioEscolhido",
                        onBack = { irPara(LumisTela.Imovel) },
                        onConfirm = { subtotal, adicionais ->
                            extrasEscolhidos = adicionais
                            totalPedido = subtotal
                            irPara(LumisTela.Resumo)
                        }
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
                        onBack = { irPara(LumisTela.Adicionais) },
                        onConfirm = {
                            servicoEscolhido?.let { servico ->
                                AgendaStore.registros.add(
                                    AgendamentoRegistro(
                                        servico = servico.nome,
                                        data = dataEscolhida,
                                        horario = horarioEscolhido,
                                        extras = extrasEscolhidos,
                                        total = totalPedido
                                    )
                                )
                            }
                            irPara(LumisTela.Principal(NavItem.AGENDA))
                        }
                    )
                    is LumisTela.Conta -> ContaScreen(
                        usuario = usuario,
                        onVoltar = { irPara(LumisTela.Principal(NavItem.PERFIL)) },
                        onSair = onLogout
                    )
                }
            }

            BottomNavBar(
                selected = when (val tela = telaAtual) {
                    is LumisTela.Principal -> tela.aba
                    else -> NavItem.EXPLORAR
                },
                onSelect = { novaAba -> telaAtual = LumisTela.Principal(novaAba) }
            )
        }
    }
}