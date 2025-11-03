package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.socorristajunior.Data.model.Question

// DAO para as Questões
@Dao
interface QuestionDAO {

    // Insere uma lista de questões. Se já existir, substitui.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestoes(questoes: List<Question>)
}