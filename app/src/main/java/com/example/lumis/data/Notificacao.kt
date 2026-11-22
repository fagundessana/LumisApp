package com.example.lumis.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notificacoes",
    indices = [Index(value = ["usuarioId"], name = "index_notificacoes_usuarioId")]
)
data class Notificacao(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: Int,
    val tipo: String,
    val titulo: String,
    val descricao: String,
    val criadoEmMillis: Long = System.currentTimeMillis(),
    val lida: Boolean = false
)
