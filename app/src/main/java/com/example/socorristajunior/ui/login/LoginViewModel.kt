package com.example.socorristajunior.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


// 1. ESTADO DA UI (Para a tela de login/navegação)
data class AuthUiState(
    val isAuthenticated: Boolean = false, // True se o Firebase tem uma sessão ativa
    val isLoading: Boolean = true, // True enquanto verifica o estado inicial ou faz login
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth = Firebase.auth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Inicializa a verificação do estado de autenticação (persistência)
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            // O Firebase verifica automaticamente se há um usuário salvo (persistência)
            _uiState.update {
                it.copy(
                    isAuthenticated = auth.currentUser != null,
                    isLoading = false // Carregamento inicial concluído
                )
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Tenta fazer o login. O Firebase mantém a sessão salva por padrão.
                auth.signInWithEmailAndPassword(email, password).await()

                // Se o login for bem-sucedido, atualiza o estado
                _uiState.update { it.copy(isAuthenticated = true, isLoading = false) }

            } catch (e: Exception) {
                // Em caso de erro (ex: credenciais inválidas)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Erro desconhecido ao fazer login."
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // 1. Chama a função de logout do Firebase.
            auth.signOut()

            // 2. Atualiza o estado da UI para desautenticado e conclui o carregamento.
            _uiState.update {
                it.copy(
                    isAuthenticated = false,
                    isLoading = false
                )
            }
        }
    }
}