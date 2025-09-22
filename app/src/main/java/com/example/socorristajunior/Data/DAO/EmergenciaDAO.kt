package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.socorristajunior.Data.model.Emergencia

@Dao
interface EmergenciaDAO {
    // Listar emergencia
    @Query("SELECT " +
            "em.emercodigo," +
            "em.emernome," +
            "em.emerdesc," +
            "em.emerimagem," +
            "em.emergravidade FROM emergencia em ORDER BY em.emernome ASC")
    fun getAllEmergencias(): Flow<List<Emergencia>> // Flow garante que notificação seja enviada sempre que os dados do banco de dados mudarem

    // Encontrar emergencia pelo nome
    @Query("SELECT " +
            "em.emercodigo," +
            "em.emernome," +
            "em.emerdesc," +
            "em.emerimagem," +
            "em.emergravidade FROM emergencia em WHERE em.emernome=:nome")
    fun selectEmergencia(nome: String): Flow<Emergencia?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg emergencia: Emergencia)
}