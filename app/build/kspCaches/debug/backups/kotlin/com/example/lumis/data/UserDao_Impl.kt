package com.example.lumis.`data`

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserDao_Impl(
  __db: RoomDatabase,
) : UserDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUser: EntityInsertAdapter<User>
  init {
    this.__db = __db
    this.__insertAdapterOfUser = object : EntityInsertAdapter<User>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `users` (`id`,`email`,`nomeUsuario`,`senha`,`tipoUsuario`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: User) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.email)
        statement.bindText(3, entity.nomeUsuario)
        statement.bindText(4, entity.senha)
        statement.bindText(5, entity.tipoUsuario)
      }
    }
  }

  public override suspend fun inserir(user: User): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfUser.insert(_connection, user)
  }

  public override suspend fun buscarPorEmail(email: String): User? {
    val _sql: String = "SELECT * FROM users WHERE email = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, email)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfNomeUsuario: Int = getColumnIndexOrThrow(_stmt, "nomeUsuario")
        val _columnIndexOfSenha: Int = getColumnIndexOrThrow(_stmt, "senha")
        val _columnIndexOfTipoUsuario: Int = getColumnIndexOrThrow(_stmt, "tipoUsuario")
        val _result: User?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpNomeUsuario: String
          _tmpNomeUsuario = _stmt.getText(_columnIndexOfNomeUsuario)
          val _tmpSenha: String
          _tmpSenha = _stmt.getText(_columnIndexOfSenha)
          val _tmpTipoUsuario: String
          _tmpTipoUsuario = _stmt.getText(_columnIndexOfTipoUsuario)
          _result = User(_tmpId,_tmpEmail,_tmpNomeUsuario,_tmpSenha,_tmpTipoUsuario)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun buscarPorNome(nome: String): User? {
    val _sql: String = "SELECT * FROM users WHERE nomeUsuario = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, nome)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfNomeUsuario: Int = getColumnIndexOrThrow(_stmt, "nomeUsuario")
        val _columnIndexOfSenha: Int = getColumnIndexOrThrow(_stmt, "senha")
        val _columnIndexOfTipoUsuario: Int = getColumnIndexOrThrow(_stmt, "tipoUsuario")
        val _result: User?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpNomeUsuario: String
          _tmpNomeUsuario = _stmt.getText(_columnIndexOfNomeUsuario)
          val _tmpSenha: String
          _tmpSenha = _stmt.getText(_columnIndexOfSenha)
          val _tmpTipoUsuario: String
          _tmpTipoUsuario = _stmt.getText(_columnIndexOfTipoUsuario)
          _result = User(_tmpId,_tmpEmail,_tmpNomeUsuario,_tmpSenha,_tmpTipoUsuario)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
