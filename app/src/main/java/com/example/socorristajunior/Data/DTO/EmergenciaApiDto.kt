package com.example.socorristajunior.Data.DTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO para a tabela 'gravidade'
@Serializable
data class GravidadeApiDto(
    @SerialName("id")
    val id: Int,

    @SerialName("gravnome")
    val gravnome: String
)

// DTO para a tabela 'fontes'
@Serializable
data class FontesApiDto(
    @SerialName("id")
    val id: Int,

    @SerialName("fonnome")
    val fonnome: String,

    @SerialName("fondescricao")
    val fondescricao: String? = null,

    @SerialName("url")
    val url: String? = null
)

@Serializable // Anotação para serialização (parsing) do JSON
data class EmergenciaApiDto(

    @SerialName("id") // Mapeia o nome da coluna no Supabase
    val id: Int,

    @SerialName("emernome") // Mapeia o nome da coluna no Supabase
    val emernome: String,

    @SerialName("emerdesc") // Mapeia o nome da coluna no Supabase
    val emerdesc: String,

    @SerialName("emerimagem") // Mapeia o nome da coluna no Supabase
    val emerimagem: String? = null, // Varchar pode ser nulo

    @SerialName("gravidade")
    val gravidade: GravidadeApiDto? = null,

    @SerialName("fontes")
    val fontes: FontesApiDto? = null,

    @SerialName("passos")
    val passos: List<PassoApiDto> = emptyList(),

    @SerialName("created_at") // Mapeia o nome da coluna no Supabase
    val createdAt: String? = null // Timestamps são recebidos como String
)