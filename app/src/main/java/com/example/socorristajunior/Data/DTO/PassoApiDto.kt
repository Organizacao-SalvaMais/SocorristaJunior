package com.example.socorristajunior.Data.DTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable // Anotação para serialização
data class PassoApiDto(

    @SerialName("pascodigo") // Mapeia o nome da coluna no Supabase
    val pascodigo: Int,

    @SerialName("pasnome") // Mapeia o nome da coluna no Supabase
    val pasnome: String,

    @SerialName("pasimagem") // Mapeia o nome da coluna no Supabase
    val pasimagem: String? = null, // Varchar pode ser nulo

    @SerialName("pasdescricao") // Mapeia o nome da coluna no Supabase
    val pasdescricao: String,

    @SerialName("pasordem") // Mapeia o nome da coluna no Supabase
    val pasordem: Int,

    @SerialName("fk_emercodigo") // Mapeia o nome da coluna no Supabase
    val fk_emercodigo: Int,

    @SerialName("created_at") // Mapeia o nome da coluna no Supabase
    val createdAt: String? = null // Opcional
)