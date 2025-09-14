package com.example.socorristajunior.Repositorio

import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.model.Emergencia
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmergenciaRepo @Inject constructor(
    private val EmergenciaDAO: EmergenciaDAO
) {
    fun getAllEmergencias(): Flow<List<Emergencia>>{
        return EmergenciaDAO.getAllEmergencias()
    }

    fun selectEmergencia(nome: String): Flow<Emergencia?>{
        return EmergenciaDAO.selectEmergencia(nome)
    }
}