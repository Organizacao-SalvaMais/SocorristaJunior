package com.example.socorristajunior.Data.DAO

import androidx.room.*
import com.example.socorristajunior.Data.model.QuizCategory
import com.example.socorristajunior.Data.model.QuizCategoryWithQuestions // Vamos criar este
import kotlinx.coroutines.flow.Flow

// DAO para a Categoria do Quiz
@Dao
interface QuizCategoryDAO {

    // Insere uma lista de categorias. Se já existir, substitui.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategorias(categorias: List<QuizCategory>)

    // Busca todas as categorias de quiz
    @Query("SELECT * FROM quiz_categoria_table")
    fun getCategorias(): Flow<List<QuizCategory>>

    // ⭐️ Query Relacional: Busca uma Categoria e,
    // automaticamente, busca todas as suas Questões (e Opções)
    @Transaction // Garante que a operação complexa seja atômica
    @Query("SELECT * FROM quiz_categoria_table WHERE id = :categoriaId")
    fun getCategoriaComQuestoes(categoriaId: Int): Flow<QuizCategoryWithQuestions>
}