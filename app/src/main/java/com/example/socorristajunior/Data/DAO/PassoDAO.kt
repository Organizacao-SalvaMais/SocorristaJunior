package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.socorristajunior.Data.model.Passo

@Dao
interface PassoDAO {
    // Buscar passos de uma emergência em ordem
    @Query("SELECT * FROM passo WHERE fk_emercodigo = :emergenciaId ORDER BY pasordem ASC")
    fun getPassosDaEmergencia(emergenciaId: Int): Flow<List<Passo>>

    // Buscar um passo específico
    @Query("SELECT * FROM passo WHERE pascodigo = :passoId")
    suspend fun getPassoById(passoId: Int): Passo?

    // Buscar primeiro passo de uma emergência
    @Query("SELECT * FROM passo WHERE fk_emercodigo = :emergenciaId ORDER BY pasordem ASC LIMIT 1")
    suspend fun getPrimeiroPasso(emergenciaId: Int): Passo?

    // Inserir passos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPasso(passo: Passo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPassos(passos: List<Passo>)

    // Atualizar ordem dos passos
    @Query("UPDATE passo SET pasordem = :novaOrdem WHERE pascodigo = :passoId")
    suspend fun atualizarOrdemPasso(passoId: Int, novaOrdem: Int)

    // Deletar todos os passos de uma emergência
    @Query("DELETE FROM passo WHERE fk_emercodigo = :emergenciaId")
    suspend fun deletePassosDaEmergencia(emergenciaId: Int)

    // Contar passos de uma emergência
    @Query("SELECT COUNT(*) FROM passo WHERE fk_emercodigo = :emergenciaId")
    suspend fun contarPassosDaEmergencia(emergenciaId: Int): Int
}