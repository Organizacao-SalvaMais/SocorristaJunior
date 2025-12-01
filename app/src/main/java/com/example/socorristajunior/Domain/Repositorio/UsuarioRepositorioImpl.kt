package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.model.Usuario
import com.example.socorristajunior.Data.DTO.UsuarioDTO
import com.google.firebase.auth.FirebaseAuth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


class UsuarioRepositorioImpl @Inject constructor(
    // Injeção do cliente Postgrest configurado no SupabaseModule
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val userDao: UserDAO,
    private val firebaseAuth: FirebaseAuth
) : UsuarioRepositorio {

    private val BUCKET_NAME = "profile_photos"
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
                    .decodeList<UsuarioDTO>()

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

    override suspend fun uploadProfilePhoto(uid: String, imageBytes: ByteArray): String? {
        val path = "profile/$uid.jpg"

        return withContext(Dispatchers.IO) {
            try {
                storage.from(BUCKET_NAME).upload(
                    path = path,
                    data = imageBytes
                ) {
                    upsert = true  // Configurações vão dentro do bloco lambda
                }

                // 2. OBTÉM A URL PÚBLICA
                val publicUrl = storage.from(BUCKET_NAME).publicUrl(path)

                // 3. ATUALIZA O REGISTRO DO USUÁRIO NO POSTGREST
                postgrest.from(TABLE_NAME).update(
                    mapOf("profile_photo_url" to publicUrl)
                ) {
                    filter { eq("firecodigo", uid) }
                }

                // 4. RETORNA A URL
                publicUrl

            } catch (e: Exception) {
                println("ERRO UPLOAD SUPABASE: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun deleteUser(fireCodigo: String): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                // Deleta a linha na tabela 'usuario' onde 'firecodigo' coincide
                postgrest.from(TABLE_NAME).delete {
                    filter { eq("firecodigo", fireCodigo) }
                }
                true // Sucesso
            }
        } catch (e: Exception) {
            println("ERRO SUPABASE: Falha ao deletar perfil: ${e.message}")
            false
        }
    }

    override suspend fun getOrCreateSupabaseUser(firebaseUid: String, email: String, nome: String): Int? {
        return try {
            withContext(Dispatchers.IO) {
                // 1. Tenta buscar usando o DTO existente
                val result = postgrest.from(TABLE_NAME)
                    .select {
                        filter {
                            eq("firecodigo", firebaseUid)
                        }
                    }.decodeSingleOrNull<UsuarioDTO>()

                if (result != null) {
                    // Se achou, retorna o ID. Se vier nulo (improvável no banco), retorna null
                    return@withContext result.usucodigo
                } else {
                    // 2. Se não existe, insere um novo
                    val usuarioDto = UsuarioDTO(
                        usuNome = nome,
                        usuEmail = email,
                        fireCodigo = firebaseUid
                    )

                    val insertedUser = postgrest.from(TABLE_NAME)
                        .insert(usuarioDto) {
                            select() // Pede o retorno do objeto criado
                        }.decodeSingle<UsuarioDTO>()

                    return@withContext insertedUser.usucodigo
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Erro ao buscar/criar ID do Supabase")
            null
        }
    }

    override suspend fun logout() {
        // 1. Desloga do Firebase
        firebaseAuth.signOut()

        // 2. Limpa o usuário do banco local (Room)
        // Isso fará o Flow no ViewModel emitir 'null' e sumir com os corações
        userDao.clearUser()
    }
}