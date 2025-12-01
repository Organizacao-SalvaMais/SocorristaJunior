package com.example.socorristajunior.Data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDTO(
    @SerialName("usucodigo")
    val usucodigo: Int? = null,

    @SerialName("usunome")
    val usuNome: String,

    @SerialName("usuemail")
    val usuEmail: String,

    // Campo Crucial: O UID fornecido pelo Firebase, usado para vincular Auth ao Perfil.
    @SerialName("firecodigo")
    val fireCodigo: String,

    // Campo criado_at (Ignorado na Inserção, pois o banco deve preencher)
    @SerialName("created_at")
    val createdAt: String? = null,

    // O campo 'ususenha' é preenchido pelo Firebase Auth. O Supabase não deve armazenar senhas em texto puro.
    @SerialName("ususenha")
    val usuSenha: String? = null
)