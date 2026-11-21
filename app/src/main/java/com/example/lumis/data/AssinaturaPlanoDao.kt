package com.example.lumis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AssinaturaPlanoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(assinatura: AssinaturaPlano)

    @Query("SELECT * FROM assinaturas_plano WHERE usuarioId = :usuarioId LIMIT 1")
    suspend fun buscarPorUsuario(usuarioId: Int): AssinaturaPlano?
}
