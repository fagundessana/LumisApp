package com.example.lumis.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assinaturas_plano")
data class AssinaturaPlano(
    @PrimaryKey val usuarioId: Int,
    val planoId: String,
    val nomePlano: String,
    val valorMensalCentavos: Long,
    val ativaDesdeMillis: Long = System.currentTimeMillis()
)
