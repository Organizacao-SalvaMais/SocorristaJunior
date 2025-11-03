package com.example.socorristajunior.Domain.Repositorio

interface CadastroRepositorio {
    suspend fun cadastrarUsuario(
        nomeCompleto: String,
        email: String,
        telefone: String,
        genero: String,
        dataNascimento: String,
        senha: String
    )

    // Login com Supabase. A função deve ser suspend (assíncrona).
    suspend fun logar(email: String, senha: String): io.github.jan.supabase.auth.user.UserSession

    // Cadastro de usuário. Também é assíncrona.
    suspend fun cadastrarUsuario(email: String, senha: String)

    // Verifica o estado atual de login
    fun isUserLoggedIn(): Boolean

    // Desconecta o usuário
    suspend fun sair()
}