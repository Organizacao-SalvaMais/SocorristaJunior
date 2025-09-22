package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.socorristajunior.Data.model.Passo

@Dao
interface PassoDAO {
    // Ideia: Cada vez que o botão próximo passo for apertado, envia o um sinal, e o back filtra conforme a ordem
    //Listar passo um por um
    @Query("SELECT pas.pascodigo," +
            "pas.pasnome," +
            "pas.pasdescricao," +
            "pas.pasordem," +
            "pas.pasemercodigo," +
            "pas.pasimagem FROM passo pas WHERE pas.pasordem=:id AND pas.pasemercodigo=:emergenciaId limit 1")
    fun getPassos(id: Int, emergenciaId: Int): Flow<Passo>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg passo: Passo)
}