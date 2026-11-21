package com.example.lumis.data

import androidx.room.Entity

@Entity(
    tableName = "profissionais_favoritos",
    primaryKeys = ["clienteId", "profissionalId"]
)
data class ProfissionalFavorito(
    val clienteId: Int,
    val profissionalId: Int
)
