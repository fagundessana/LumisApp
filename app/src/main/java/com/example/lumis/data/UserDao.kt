package com.example.lumis.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun inserir(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): User?

    @Query("SELECT * FROM users WHERE nomeUsuario = :nome LIMIT 1")
    suspend fun buscarPorNome(nome: String): User?
}