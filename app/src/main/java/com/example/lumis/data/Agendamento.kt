package com.example.lumis.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Agendamento salvo exclusivamente no banco Room do dispositivo.
 *
 * O valor monetário é armazenado em centavos para evitar imprecisão de Double.
 * O campo extras guarda os nomes selecionados separados por quebra de linha.
 */
@Entity(
    tableName = "agendamentos",
    indices = [
        Index(value = ["usuarioId"], name = "index_agendamentos_usuarioId"),
        Index(value = ["data", "horario"], name = "index_agendamentos_data_horario")
    ]
)
data class Agendamento(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: Int,
    val servico: String,
    val data: String,
    val horario: String,
    val quartos: Int,
    val banheiros: Int,
    val tamanhoImovel: String,
    @ColumnInfo(defaultValue = "''")
    val endereco: String = "",
    @ColumnInfo(defaultValue = "'A combinar no atendimento'")
    val formaPagamento: String = "A combinar no atendimento",
    val extras: String = "",
    val totalCentavos: Long,
    val status: String = "Agendado",
    val criadoEmMillis: Long = System.currentTimeMillis(),
    val avaliacao: Int? = null
)
