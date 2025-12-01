package com.example.socorristajunior.Data.Services

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseTokenProvider @Inject constructor(
    private val auth: FirebaseAuth // Injeta o objeto do FirebaseModule
) {
    suspend fun getJwtToken(): String? {
        return try {
            // Pega o usuário Firebase atual
            val user = auth.currentUser

            // Requer o token, forçando uma atualização se necessário (false = não força refresh)
            val firebaseToken = user?.getIdToken(false)?.await()

            // Retorna a string do token, que o Supabase usará para autenticação
            return firebaseToken?.token

        } catch (e: Exception) {
            println("ERRO: Falha ao obter JWT do Firebase: ${e.message}")
            // Retorna null para sinalizar a falha
            return null
        }
    }
}