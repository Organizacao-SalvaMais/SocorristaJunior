package com.example.socorristajunior.Data.BancoDeDados

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.DAO.OptionDAO
import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.DAO.QuestionDAO
import com.example.socorristajunior.Data.DAO.QuizCategoryDAO
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.model.Emergencia
import com.example.socorristajunior.Data.model.Option
import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Data.model.Question
import com.example.socorristajunior.Data.model.QuizCategory
import com.example.socorristajunior.Data.model.UserEntity

@TypeConverters(QuizTypeConverters::class)
@Database(
    entities = [
        Emergencia::class,
        Passo::class,
        UserEntity::class,
        QuizCategory::class,
        Question::class,
        Option::class
               ],
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
    // DAO da Categoria do Quiz
    abstract fun quizCategoryDAO(): QuizCategoryDAO
    // DAO das Questões
    abstract fun questionDAO(): QuestionDAO
    // DAO das Opções
    abstract fun optionDAO(): OptionDAO
}