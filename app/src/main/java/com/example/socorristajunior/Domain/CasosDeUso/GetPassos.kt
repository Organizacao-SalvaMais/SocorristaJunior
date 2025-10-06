package com.example.socorristajunior.Domain.CasosDeUso

import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPassos @Inject constructor(
    private val passoRepo: PassoRepo
) {
    // A função agora espera apenas o ID da emergência
    // e retorna um Flow de uma lista de Passos.
    operator fun invoke(emergenciaId: Int): Flow<List<Passo>> {
        // A chamada ao repositório agora está correta.
        return passoRepo.getPassos(emergenciaId)
    }
}