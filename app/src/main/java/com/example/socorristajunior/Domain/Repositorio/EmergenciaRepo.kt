package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.DAO.EmergenciaDAO
import com.example.socorristajunior.Data.model.Emergencia
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmergenciaRepo @Inject constructor(
    private val emergenciaDAO: EmergenciaDAO
) {
    fun getAllEmergencias(): Flow<List<Emergencia>> {
        return emergenciaDAO.getAllEmergencias()
    }

    suspend fun getEmergenciaById(id: Int): Emergencia? {
        return emergenciaDAO.getEmergenciaById(id)
    }

    fun searchEmergencias(query: String): Flow<List<Emergencia>> {
        return emergenciaDAO.searchEmergencias(query)
    }

    fun getEmergenciasPorGravidade(gravidade: String): Flow<List<Emergencia>> {
        return emergenciaDAO.getEmergenciasPorGravidade(gravidade)
    }

    fun getEmergenciasPorCategoria(categoria: String): Flow<List<Emergencia>> {
        return emergenciaDAO.getEmergenciasPorCategoria(categoria)
    }

    suspend fun insertEmergencia(emergencia: Emergencia) {
        emergenciaDAO.insertEmergencia(emergencia)
    }

    suspend fun insertAllEmergencias(emergencias: List<Emergencia>) {
        emergenciaDAO.insertAllEmergencias(emergencias)
    }

    suspend fun updateEmergencia(emergencia: Emergencia) {
        emergenciaDAO.updateEmergencia(emergencia)
    }

    suspend fun getTotalEmergencias(): Int {
        return emergenciaDAO.getTotalEmergencias()
    }    
}