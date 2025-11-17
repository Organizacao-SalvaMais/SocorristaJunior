package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.socorristajunior.Data.model.UserInteraction

@Dao
interface UserInteractionDAO {

    // Insere ou Atualiza (se já existir, substitui)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveInteraction(interaction: UserInteraction)

    // Busca a interação de uma emergência específica
    @Query("SELECT * FROM user_interaction WHERE userId = :userId AND emergencyId = :emerId")
    fun getInteractionFlow(userId: Int, emerId: Int): Flow<UserInteraction?>

    // Busca todas as favoritas
    @Query("SELECT * FROM user_interaction WHERE userId = :userId AND isFavorite = 1")
    fun getFavorites(userId: Int): Flow<List<UserInteraction>>

    @Query("SELECT * FROM user_interaction WHERE userId = :userId")
    fun getAllInteractionsFlow(userId: Int): Flow<List<UserInteraction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInteractions(interactions: List<UserInteraction>)

    @Query("DELETE FROM user_interaction")
    suspend fun clearAllInteractions()
}