package com.example.socorristajunior.Data.model

import com.google.gson.annotations.SerializedName

data class Noticia(
    val id: Int,
    val titulo: String,
    @SerializedName("tema_principal") // Garante que o JSON snake_case vire camelCase se precisar
    val temaPrincipal: String?,
    val resumo: String,
    val link: String,
    @SerializedName("created_at")
    val createdAt: String?
)