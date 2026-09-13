package com.example.lumis.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val email: String,

    val nomeUsuario: String,

    val senha: String,

    val tipoUsuario: String
)
