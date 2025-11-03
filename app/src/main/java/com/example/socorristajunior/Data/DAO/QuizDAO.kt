package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.socorristajunior.Data.model.Quiz
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDAO {

    // ⭐️ (MODIFICAÇÃO) Define uma query (consulta) SQL para buscar todos os quizzes
    // O 'Flow' permite que a UI (ViewModel) observe mudanças no banco em tempo real
    @Query("SELECT * FROM quiz_table")
    fun getAllQuizzes(): Flow<List<Quiz>>

    // ⭐️ (MODIFICAÇÃO) Define uma query SQL para buscar quizzes por uma categoria específica
    @Query("SELECT * FROM quiz_table WHERE categoria = :categoria")
    fun getQuizzesByCategory(categoria: String): Flow<List<Quiz>>

    // ⭐️ (MODIFICAÇÃO) Define uma query SQL para buscar um quiz específico pelo seu ID
    @Query("SELECT * FROM quiz_table WHERE id = :id")
    fun getQuizById(id: Int): Flow<Quiz>
}