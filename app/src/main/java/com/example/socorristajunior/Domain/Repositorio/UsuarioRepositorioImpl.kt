package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.model.Usuario
import com.example.socorristajunior.Data.DTO.UsuarioDTO
import io.github.jan.supabase.postgrest.Postgrest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioRepositorioImpl @Inject constructor(
    // Injeção do cliente Postgrest configurado no SupabaseModule
    private val postgrest: Postgrest
) : UsuarioRepositorio {

    private val TABLE_NAME = "usuario"

    override suspend fun insertUser(usuario: Usuario): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                // Mapeamento para o DTO antes da inserção
                val usuarioDto = UsuarioDTO(
                    usuNome = usuario.usunome,
                    usuEmail = usuario.usuemail,
                    fireCodigo = usuario.firecodigo, // UID do Firebase
                    usuSenha = ""
                )

                // Executa a inserção no Supabase (tabela 'usuario')
                postgrest.from(TABLE_NAME).insert(usuarioDto)
                true
            }
        } catch (e: Exception) {
            println("ERRO SUPABASE CRÍTICO: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    override suspend fun getUserByFirebaseId(fireCodigo: String): Usuario? {
        // Implementação para busca (que seria usada no Login)
        return null
    }
}