package com.example.socorristajunior.ui.screens.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.Usuario
import com.example.socorristajunior.Domain.Repositorio.UsuarioRepositorio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CadastroViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val usuarioRepositorio: UsuarioRepositorio
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    private val _resetPasswordState = MutableStateFlow<ResetState>(ResetState.Idle)
    val resetPasswordState: StateFlow<ResetState> = _resetPasswordState

    fun signUp(email: String, name: String, password: String) {
        _signUpState.value = SignUpState.Loading

        // Garante que a operação é cancelada se a ViewModel for destruída
        viewModelScope.launch {
            try {
                // 1. CHAMA FIREBASE AUTH
                val authResult = firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .await()

                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    val fireCode = firebaseUser.uid

                    // 2. CRIA OBJETO DE DOMÍNIO
                    val novoUsuario = Usuario(
                        id = null, // Deixando o Supabase gerar o PK
                        usunome = name,
                        usuemail = email,
                        firecodigo = fireCode // Chave de ligação
                    )

                    // 3. CHAMA SUPABASE REPOSITORY
                    val dataSuccess = usuarioRepositorio.insertUser(novoUsuario)

                    if (dataSuccess) {
                        _signUpState.value = SignUpState.Success
                    } else {
                        // Cenário de falha de persistência de dados no Supabase.
                        // O usuário existe no Firebase, mas não tem perfil no Supabase.
                        // Ação: Logar o erro e notificar o usuário (ou tentar uma reinserção).
                        _signUpState.value =
                            SignUpState.Error("Cadastro concluído, mas o perfil não foi salvo no Supabase. Tente novamente.")
                    }
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is FirebaseAuthUserCollisionException -> "O email fornecido já está em uso."
                    else -> "Erro no Firebase Auth: ${e.localizedMessage}"
                }
                _signUpState.value = SignUpState.Error(errorMessage)
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        _resetPasswordState.value = ResetState.Loading

        viewModelScope.launch {
            try {
                // 🚨 CHAMA O METODO DO FIREBASE AUTH:
                firebaseAuth.sendPasswordResetEmail(email).await()

                _resetPasswordState.value =
                    ResetState.Success("Link de redefinição enviado para $email.")

            } catch (e: Exception) {
                val errorMessage = when (e) {
                    // Trata o erro mais comum: usuário não encontrado.
                    is FirebaseAuthInvalidUserException -> "Nenhuma conta encontrada com este e-mail. Verifique o endereço."
                    else -> "Erro ao enviar link: ${e.localizedMessage ?: "Erro desconhecido"}"
                }
                _resetPasswordState.value = ResetState.Error(errorMessage)
            }
        }
    }
}

// Modelos de Estado para a UI
sealed class SignUpState {
    data object Idle : SignUpState()
    data object Loading : SignUpState()
    data object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}

sealed class ResetState {
    data object Idle : ResetState()
    data object Loading : ResetState()
    data class Success(val message: String) : ResetState()
    data class Error(val message: String) : ResetState()
}