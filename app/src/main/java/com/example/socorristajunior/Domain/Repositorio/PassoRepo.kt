package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.DAO.PassoDAO
import com.example.socorristajunior.Data.model.Passo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PassoRepo @Inject constructor(
    private val PassoDAO: PassoDAO
) {
    fun getPassos(emergenciaId: Int): Flow<Passo>{
        return PassoDAO.getPassos(emergenciaId)
    }
}