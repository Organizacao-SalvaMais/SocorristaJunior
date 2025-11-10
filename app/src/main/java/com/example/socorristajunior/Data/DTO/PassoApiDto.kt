package com.example.socorristajunior.Data.DTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable // Anotação para serialização
data class PassoApiDto(

    @SerialName("id") // Mapeia o nome da coluna no Supabase
    val id: Int,

    @SerialName("pasnome") // Mapeia o nome da coluna no Supabase
    val pasnome: String,

    @SerialName("pasimagem") // Mapeia o nome da coluna no Supabase
    val pasimagem: String? = null, // Varchar pode ser nulo

    @SerialName("pasdescricao") // Mapeia o nome da coluna no Supabase
    val pasdescricao: String,

    @SerialName("pasordem") // Mapeia o nome da coluna no Supabase
    val pasordem: Int,

    @SerialName("emercodigo") // Mapeia o nome da coluna no Supabase
    val emercodigo: Int,

    @SerialName("created_at") // Mapeia o nome da coluna no Supabase
    val createdAt: String? = null // Opcional
)
