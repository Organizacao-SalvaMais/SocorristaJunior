package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.socorristajunior.Data.model.Emergencia

@Dao
interface EmergenciaDAO {

    // Buscar todas as emergências ordenadas
    @Query("SELECT * FROM emergencia ORDER BY emernome ASC")
    fun getAllEmergencias(): Flow<List<Emergencia>>

    // Buscar emergência por ID
    @Query("SELECT * FROM emergencia WHERE emercodigo = :id")
    suspend fun getEmergenciaById(id: Int): Emergencia?

    // Buscar emergência por nome (para busca)
    @Query("SELECT * FROM emergencia WHERE emernome LIKE '%' || :nome || '%'")
    fun searchEmergencias(nome: String): Flow<List<Emergencia>>

    // Buscar por gravidade
    @Query("SELECT * FROM emergencia WHERE emergravidade = :gravidade")
    fun getEmergenciasPorGravidade(gravidade: String): Flow<List<Emergencia>>

    // Buscar por categoria
    @Query("SELECT * FROM emergencia WHERE categoria = :categoria")
    fun getEmergenciasPorCategoria(categoria: String): Flow<List<Emergencia>>


    // Inserir emergência
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencia(emergencia: Emergencia)

    // Inserir lista de emergências
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEmergencias(emergencias: List<Emergencia>)

    // Atualizar emergência
    @Update
    suspend fun updateEmergencia(emergencia: Emergencia)

    // Deletar emergência
    @Delete
    suspend fun deleteEmergencia(emergencia: Emergencia)

    // Contar total de emergências
    @Query("SELECT COUNT(*) FROM emergencia")
    suspend fun getTotalEmergencias(): Int
}