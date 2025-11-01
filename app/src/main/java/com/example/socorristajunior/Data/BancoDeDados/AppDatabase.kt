package com.example.socorristajunior.Data.BancoDeDados

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.DAO.QuizDAO
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Data.model.Quiz
import com.example.socorristajunior.Data.model.UserEntity

@TypeConverters(QuizTypeConverters::class)
@Database(
    entities = [
        Emergencia::class,
        Passo::class,
        UserEntity::class,
        Quiz::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    // DAO para Usuário
    abstract fun userDAO(): UserDAO
    // DAO para Emergência
    abstract fun emergenciaDAO(): EmergenciaDAO
    // DAO para Passo
    abstract fun passoDAO(): PassoDAO
    // DAO para Quiz (que você já tinha adicionado)
    abstract fun quizDAO(): QuizDAO
}