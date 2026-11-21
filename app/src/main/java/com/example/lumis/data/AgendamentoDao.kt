package com.example.lumis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AgendamentoDao {

    @Insert
    suspend fun inserir(agendamento: Agendamento): Long

    @Query("SELECT * FROM agendamentos WHERE usuarioId = :usuarioId ORDER BY id DESC")
    suspend fun listarPorUsuario(usuarioId: Int): List<Agendamento>

    @Query("SELECT * FROM agendamentos WHERE profissionalId = :profissionalId ORDER BY id DESC")
    suspend fun listarPorProfissional(profissionalId: Int): List<Agendamento>

    @Query("SELECT AVG(avaliacao) FROM agendamentos WHERE profissionalId = :profissionalId AND avaliacao IS NOT NULL")
    suspend fun mediaAvaliacoes(profissionalId: Int): Double?

    @Query("SELECT COUNT(avaliacao) FROM agendamentos WHERE profissionalId = :profissionalId AND avaliacao IS NOT NULL")
    suspend fun quantidadeAvaliacoes(profissionalId: Int): Int

    @Query("UPDATE agendamentos SET status = :status WHERE id = :id AND usuarioId = :usuarioId")
    suspend fun atualizarStatus(id: Long, usuarioId: Int, status: String): Int

    @Query("UPDATE agendamentos SET status = :status WHERE id = :id AND profissionalId = :profissionalId")
    suspend fun atualizarStatusPorProfissional(id: Long, profissionalId: Int, status: String): Int

    @Query("UPDATE agendamentos SET data = :data, horario = :horario WHERE id = :id AND usuarioId = :usuarioId AND status != 'Cancelado'")
    suspend fun reagendar(id: Long, usuarioId: Int, data: String, horario: String): Int

    @Query("UPDATE agendamentos SET data = :data, horario = :horario WHERE id = :id AND profissionalId = :profissionalId AND status != 'Cancelado'")
    suspend fun reagendarComoProfissional(id: Long, profissionalId: Int, data: String, horario: String): Int

    @Query("SELECT COUNT(*) FROM agendamentos WHERE usuarioId = :usuarioId AND data = :data AND horario = :horario AND status != 'Cancelado' AND id != :ignorarId")
    suspend fun contarConflitos(usuarioId: Int, data: String, horario: String, ignorarId: Long): Int

    @Query("UPDATE agendamentos SET avaliacao = :estrelas WHERE id = :id AND usuarioId = :usuarioId")
    suspend fun avaliar(id: Long, usuarioId: Int, estrelas: Int): Int
}
