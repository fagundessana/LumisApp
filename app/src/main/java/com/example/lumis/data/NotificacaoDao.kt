package com.example.lumis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificacaoDao {
    @Insert
    suspend fun inserir(notificacao: Notificacao): Long

    @Query("SELECT * FROM notificacoes WHERE usuarioId = :usuarioId ORDER BY criadoEmMillis DESC")
    suspend fun listarPorUsuario(usuarioId: Int): List<Notificacao>

    @Query("UPDATE notificacoes SET lida = 1 WHERE id = :id AND usuarioId = :usuarioId")
    suspend fun marcarComoLida(id: Long, usuarioId: Int)

    @Query("UPDATE notificacoes SET lida = 1 WHERE usuarioId = :usuarioId")
    suspend fun marcarTodasComoLidas(usuarioId: Int)
}
