package com.example.lumis.model

data class Servico(
    val nome: String,
    val descricao: String,
    val preco: String
)

// ---------------------------------------------------------
// FONTE DE DADOS (MOCK)
// ---------------------------------------------------------

object ServicoRepository {
    val todos = listOf(
        Servico(
            nome = "Limpeza Padrão",
            descricao = "Limpeza completa de rotina para sua casa com\n" +
                "aspiração, remoção de pó e higienização geral.",
            preco = "R$ 120,00"
        ),
        Servico(
            nome = "Limpeza Pesada",
            descricao = "Limpeza profunda com foco em gorduras difíceis,\n" +
                "rejuntes, remoção de sujeiras acumuladas.",
            preco = "R$ 220,00"
        ),
        Servico(
            nome = "Limpeza Pós-Mudança",
            descricao = "Ideal para preparar o imóvel antes de entrar ou deixar\n" +
                "impecável para a entrega de chaves.",
            preco = "R$ 350,00"
        ),
        Servico(
            nome = "Limpeza Airbnb / Temporada",
            descricao = "Preparação expressa e rigorosa entre reservas de\n" +
                "hóspedes com arrumação padrão hotelaria.",
            preco = "R$ 180,00"
        )
    )
}