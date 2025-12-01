package com.example.socorristajunior.Data.remote

import com.example.socorristajunior.Data.model.Noticia
import retrofit2.http.GET

interface NoticiasApi {
    @GET("noticias") // Rota do seu FastAPI
    suspend fun getNoticias(): List<Noticia>
}