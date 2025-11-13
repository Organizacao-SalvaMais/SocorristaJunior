package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.model.Usuario
import com.example.socorristajunior.Data.DTO.UsuarioDTO
import io.github.jan.supabase.postgrest.Postgrest
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

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
                    usuSenha = null
                )

                // Executa a inserção no Supabase (tabela 'usuario')
                postgrest.from(TABLE_NAME).insert(usuarioDto)
                Timber.i("Usuário inserido no Supabase: ${usuario.firecodigo}")
                true
            }
        } catch (e: Exception) {
            Timber.e(e, "Erro ao inserir usuário no Supabase")
            false
        }
    }

    override suspend fun getUserByFirebaseId(fireCodigo: String): Usuario? {
        return withContext(Dispatchers.IO) {
            try {

                val result = postgrest.from(TABLE_NAME)
                    .select {
                        filter { eq("firecodigo", fireCodigo) }
                    }
                    .decodeList<UsuarioDTO>() // ← Decodifica como lista

                // Pega o primeiro elemento da lista (se existir)
                val dto = result.firstOrNull()

                // Mapeia para o domínio
                dto?.let {
                    Usuario(
                        usucodigo = it.usucodigo,
                        usunome = it.usuNome,
                        usuemail = it.usuEmail,
                        firecodigo = it.fireCodigo
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Erro ao buscar perfil no Supabase")
                null
            }
        }
    }
}