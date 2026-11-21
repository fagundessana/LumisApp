package com.example.lumis.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfissionalFavoritoDao {
    @Query("SELECT profissionalId FROM profissionais_favoritos WHERE clienteId = :clienteId")
    suspend fun listarIds(clienteId: Int): List<Int>

    @Insert
    suspend fun inserir(favorito: ProfissionalFavorito)

    @Delete
    suspend fun remover(favorito: ProfissionalFavorito)
}
