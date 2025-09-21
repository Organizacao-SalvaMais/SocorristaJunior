package com.example.socorristajunior.Domain.CasosDeUso

import com.example.socorristajunior.Data.model.Passo
import com.example.socorristajunior.Domain.Repositorio.PassoRepo
import kotlinx.coroutines.flow.Flow

// Camada de Domain usada para verificar regras de negócio(verificação, tratamento de erro, etc), não necessário atualmente, mas muito importante para o futuro
class GetPassos(
    private val repositorio: PassoRepo
) {
    // Invoke serve para chamar a classe como se fosse uma função
//    operator fun invoke(passoId: Int, emergenciaId: Int): Flow<Passo> {
//        return repositorio.getPassos(passoId, emergenciaId)
//    }
}