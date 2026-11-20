package com.example.lumis.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Acrescenta a tabela de agendamentos sem alterar nem recriar a tabela users.
 */
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `agendamentos` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `usuarioId` INTEGER NOT NULL,
                `servico` TEXT NOT NULL,
                `data` TEXT NOT NULL,
                `horario` TEXT NOT NULL,
                `quartos` INTEGER NOT NULL,
                `banheiros` INTEGER NOT NULL,
                `tamanhoImovel` TEXT NOT NULL,
                `extras` TEXT NOT NULL,
                `totalCentavos` INTEGER NOT NULL,
                `status` TEXT NOT NULL,
                `criadoEmMillis` INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_agendamentos_usuarioId` ON `agendamentos` (`usuarioId`)"
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_agendamentos_data_horario` ON `agendamentos` (`data`, `horario`)"
        )
    }
}


/**
 * Migration 5 -> 6: adiciona endereço e forma de pagamento, preservando agendamentos existentes.
 */
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE agendamentos ADD COLUMN endereco TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE agendamentos ADD COLUMN formaPagamento TEXT NOT NULL DEFAULT 'A combinar no atendimento'")
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE users ADD COLUMN localizacao TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE users ADD COLUMN telefone TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE users ADD COLUMN fotoPerfilPath TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE users ADD COLUMN biografia TEXT NOT NULL DEFAULT ''")
    }
}

/**
 * Migration 6 -> 7: adiciona o banner do perfil sem apagar usuários ou agendamentos.
 */
val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE users ADD COLUMN bannerPerfilPath TEXT NOT NULL DEFAULT ''")
    }
}

@Database(
    entities = [User::class, Agendamento::class],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun agendamentoDao(): AgendamentoDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lumis_database"
                ).addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4,
                    MIGRATION_4_5,
                    MIGRATION_5_6,
                    MIGRATION_6_7
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}
