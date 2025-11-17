package com.example.socorristajunior.Data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logged_user")
data class UserEntity(
    // Sempre deve haver apenas 1 linha nesta tabela, então usamos uma chave primária fixa.
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1,

    // O status de login: true se o usuário está logado
    val isLoggedIn: Boolean,

    // Detalhes do Usuário (Para exibição no Perfil)
    val username: String? = null,
    val email: String? = null,

    // Token de autenticação
    val userToken: String? = null,
    val photoUrl: String? = null
)