package com.example.socorristajunior.Data.model

import com.google.gson.annotations.SerializedName
import java.util.Locale

data class Noticia(
    val id: Int,
    val titulo: String,
    @SerializedName("tema_principal")
    val temaPrincipal: String?,
    val resumo: String,
    val link: String,
    @SerializedName("created_at")
    val createdAt: String?
) {
    // Lógica para descobrir o ícone/tipo baseado no link
    fun getTipoConteudo(): TipoConteudo {
        val linkLower = link.lowercase(Locale.ROOT)
        return when {
            linkLower.endsWith(".pdf") -> TipoConteudo.PDF
            linkLower.contains("youtube.com") || linkLower.contains("youtu.be") -> TipoConteudo.VIDEO
            else -> TipoConteudo.ARTIGO
        }
    }
}

// Enum para facilitar o controle
enum class TipoConteudo(val label: String) {
    PDF("Documento PDF"),
    VIDEO("Vídeo"),
    ARTIGO("Artigo/Site")
}