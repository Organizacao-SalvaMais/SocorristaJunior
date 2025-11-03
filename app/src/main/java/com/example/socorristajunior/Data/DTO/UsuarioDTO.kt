package com.example.socorristajunior.Data.DTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDTO(
    // 💡 Este é o ID do usuário Supabase. É crucial!
    @SerialName("user_id") // Deve corresponder ao nome da coluna
    val userId: String,

    @SerialName("usunome")
    val usunome: String,

    // Assumindo que você terá uma coluna 'telefone'
    @SerialName("telefone")
    val telefone: String,

    // Assumindo que você terá uma coluna 'genero'
    @SerialName("genero")
    val genero: String,

    // Assumindo que você terá uma coluna 'data_nascimento'
    @SerialName("data_nascimento")
    val dataNascimento: String
)