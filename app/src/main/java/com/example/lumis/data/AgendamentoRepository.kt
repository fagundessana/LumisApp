package com.example.lumis.data

class AgendamentoRepository(private val agendamentoDao: AgendamentoDao) {
    suspend fun salvar(agendamento: Agendamento): Long = agendamentoDao.inserir(agendamento)
    suspend fun listarPorUsuario(usuarioId: Int): List<Agendamento> = agendamentoDao.listarPorUsuario(usuarioId)
    suspend fun listarPorProfissional(profissionalId: Int): List<Agendamento> = agendamentoDao.listarPorProfissional(profissionalId)
    suspend fun atualizarStatus(id: Long, usuarioId: Int, status: String): Boolean =
        agendamentoDao.atualizarStatus(id, usuarioId, status) > 0
    suspend fun atualizarStatusComoProfissional(id: Long, profissionalId: Int, status: String): Boolean =
        agendamentoDao.atualizarStatusPorProfissional(id, profissionalId, status) > 0
    suspend fun reagendar(id: Long, usuarioId: Int, data: String, horario: String): Boolean {
        if (agendamentoDao.contarConflitos(usuarioId, data, horario, id) > 0) return false
        return agendamentoDao.reagendar(id, usuarioId, data, horario) > 0
    }
    suspend fun horarioEmUso(usuarioId: Int, data: String, horario: String): Boolean =
        agendamentoDao.contarConflitos(usuarioId, data, horario, -1L) > 0
    suspend fun reagendarComoProfissional(id: Long, profissionalId: Int, data: String, horario: String): Boolean =
        agendamentoDao.reagendarComoProfissional(id, profissionalId, data, horario) > 0
    suspend fun avaliar(id: Long, usuarioId: Int, estrelas: Int): Boolean {
        require(estrelas in 1..5)
        return agendamentoDao.avaliar(id, usuarioId, estrelas) > 0
    }
}
