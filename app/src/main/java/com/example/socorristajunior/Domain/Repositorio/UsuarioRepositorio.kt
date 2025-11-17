package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.model.Usuario

interface UsuarioRepositorio {
    // Insere o perfil após o Firebase Auth
    suspend fun insertUser(usuario: Usuario): Boolean
    // Busca o perfil pelo UID
    suspend fun getUserByFirebaseId(fireCodigo: String): Usuario?

    // Deleta Conta
    suspend fun deleteUser(fireCodigo: String): Boolean
}