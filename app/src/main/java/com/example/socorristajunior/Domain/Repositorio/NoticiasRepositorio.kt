package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.remote.NoticiasApi
import com.example.socorristajunior.Data.model.Noticia
import javax.inject.Inject

class NoticiasRepositorio @Inject constructor(
    private val api: NoticiasApi
) {
    suspend fun buscarNoticias(): List<Noticia> {
        return api.getNoticias()
    }
}