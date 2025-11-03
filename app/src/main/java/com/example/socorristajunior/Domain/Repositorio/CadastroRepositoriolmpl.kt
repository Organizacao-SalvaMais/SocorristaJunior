package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.DTO.UsuarioDTO
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject


class CadastroRepositorioImpl @Inject constructor(
    private val auth: Auth, //Injetando do SupabaseModule
    private val postgrest: Postgrest
): CadastroRepositorio {

    override suspend fun cadastrarUsuario(
        nomeCompleto: String,
        email: String,
        telefone: String,
        genero: String,
        dataNascimento: String,
        senha: String
    ) {
        // 1. CHAMA O SUPABASE AUTH (Cria o registro em auth.users)
        val session = auth.signUpWith(Email) {
            this.email = email
            this.password = senha
        }

        // 2. OBTÉM O ID DO NOVO USUÁRIO
        // O Supabase retorna a sessão após o sign-up. Pegamos o ID.
        val userId = auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("ID do usuário não pôde ser obtido após cadastro. Sessão não ativa.")

        // 3. SALVA OS METADADOS USANDO POSTGREST
        val novoUsuario = UsuarioDTO(
            userId = userId,
            usunome = nomeCompleto,
            telefone = telefone,
            genero = genero,
            dataNascimento = dataNascimento
        )

        // Insere o perfil na sua tabela 'usuario'
        postgrest["usuario"].insert(novoUsuario)

        // Se este insert falhar, você deve usar o auth.signOut() para limpar a sessão
        // ou criar um Trigger no Supabase para lidar com a inserção de perfil.
    }

    // Implememtação do Login (Mantido)
    override suspend fun logar(email: String, senha: String): UserSession {
        auth.signInWith(Email) {
            this.email = email
            this.password = senha
        }
        val session = auth.currentSessionOrNull()
        return requireNotNull(session) {
            "Erro desconhecido: Sessão não pôde ser recuperada após o login. Verifique as credenciais."
        }
    }

    override suspend fun cadastrarUsuario(email: String, senha: String) {
        TODO("Not yet implemented")
    }

    // Implementação da Verificação de Login
    override fun isUserLoggedIn(): Boolean {
        // O cliente Supabase armazena a sessão localmente.
        return auth.currentSessionOrNull() != null
    }

    // Implementação
    override suspend fun sair() {
        auth.signOut()
    }
}