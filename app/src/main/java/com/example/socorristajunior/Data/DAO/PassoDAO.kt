package com.example.socorristajunior.Data.DAO

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.socorristajunior.Data.model.Passo

@Dao
interface PassoDAO {
    // Ideia: Cada vez que o botão próximo passo for apertado, envia o um sinal, e o back filtra conforme a ordem
    //Listar passo um por um
    @Query("SELECT * FROM passo pas WHERE pas.pasemercodigo=:emergenciaId ORDER BY pas.pasordem ASC")
    fun getPassos(emergenciaId: Int): Flow<Passo>
}