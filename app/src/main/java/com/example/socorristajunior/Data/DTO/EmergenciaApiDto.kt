package com.example.socorristajunior.Data.DTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// DTO para a tabela 'gravidade'
@Serializable
data class GravidadeApiDto(
    @SerialName("gravcodigo")
    val gravcodigo: Int,

    @SerialName("gravnome")
    val gravnome: String,

    @SerialName("gravicor")
    val gravicor: String? = null
)

// DTO para a tabela 'fontes'
@Serializable
data class FontesApiDto(
    @SerialName("foncodigo")
    val foncodigo: Int,

    @SerialName("fonnome")
    val fonnome: String,

    @SerialName("fondescricao")
    val fondescricao: String? = null,

    @SerialName("url")
    val url: String? = null
)

@Serializable // Anotação para serialização (parsing) do JSON
data class EmergenciaApiDto(

    @SerialName("emercodigo") // Mapeia o nome da coluna no Supabase
    val emercodigo: Int,

    @SerialName("emernome") // Mapeia o nome da coluna no Supabase
    val emernome: String,

    @SerialName("emerdesc") // Mapeia o nome da coluna no Supabase
    val emerdesc: String,

    @SerialName("emerimagem") // Mapeia o nome da coluna no Supabase
    val emerimagem: String? = null, // Varchar pode ser nulo

    @SerialName("fk_gravcodigo")
    val fk_gravcodigo: GravidadeApiDto? = null,

    @SerialName("fk_foncodigo")
    val fontes: FontesApiDto? = null,

    @SerialName("passos")
    val passos: List<PassoApiDto> = emptyList(), // Default para lista vazia

    @SerialName("created_at") // Mapeia o nome da coluna no Supabase
    val createdAt: String? = null // Timestamps são recebidos como String
)