package com.example.lumis.controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.lumis.model.Servico
import com.example.lumis.model.ServicoRepository

// ---------------------------------------------------------
// CONTROLLER DA TELA DE SERVIÇOS
// ---------------------------------------------------------

class ServicesController {

    val servicos: List<Servico> = ServicoRepository.todos

    var servicoSelecionado by mutableStateOf(
        ServicoRepository.todos[1].nome
    )
        private set

    fun selecionarServico(servico: Servico) {
        servicoSelecionado = servico.nome
    }

    fun continuar() {
        // Lógica de navegação/pagamento (próxima etapa)
    }
}