package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.socorristajunior.Data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    // Retorna um Flow para que o ViewModel possa observar mudanças automaticamente
    @Query("SELECT * FROM logged_user WHERE id = 1")
    fun getLoggedUser(): Flow<UserEntity?>

    // Salva ou atualiza o estado de login. REPLACE é crucial para garantir que sempre haja apenas 1 linha.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLoginStatus(user: UserEntity)

    // Simula o logout (Define o status como false, mantendo a mesma linha de ID=1)
    @Query("UPDATE logged_user SET isLoggedIn = 0, userToken = null, username = null WHERE id = 1")
    suspend fun logout()
}
