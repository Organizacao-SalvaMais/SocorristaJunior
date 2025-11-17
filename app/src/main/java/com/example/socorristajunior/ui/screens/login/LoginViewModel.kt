package com.example.socorristajunior.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.model.UserEntity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.socorristajunior.Domain.Repositorio.UsuarioRepositorio
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDao: UserDAO,
    private val auth: FirebaseAuth,
    private val usuarioRepositorio: UsuarioRepositorio
) : ViewModel() {

    // Flow para controlar loading manual (durante login)
    private val _manualLoading = MutableStateFlow(false)

    // Flow para erros
    private val _errorFlow = MutableStateFlow<String?>(null)

    // Flow para o status do DB (login)
    private val _dbLoginStatus = userDao.getLoggedUser()
        .map { userEntity ->
            Pair(userEntity?.isLoggedIn ?: false, false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Pair(false, true)
        )

    // Estado UI derivado
    val uiState: StateFlow<AuthUiState> = combine(
        _dbLoginStatus,
        _errorFlow,
        _manualLoading
    ) { dbStatus, error, manualLoading ->
        AuthUiState(
            isLoggedIn = dbStatus.first,
            // Loading é true se estiver carregando do DB OU se estiver fazendo login manual
            isLoading = dbStatus.second || manualLoading,
            errorMessage = error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthUiState(isLoading = true)
    )

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                // Ativa loading e limpa erros
                _manualLoading.value = true
                _errorFlow.value = null

                // 1. FASE FIREBASE: Autenticar
                val authResult = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    val fireCode = firebaseUser.uid

                    // 2. FASE SUPABASE: Buscar o perfil
                    val supabaseId = usuarioRepositorio.getOrCreateSupabaseUser(
                        firebaseUid = fireCode,
                        email = firebaseUser.email ?: "",
                        nome = firebaseUser.displayName ?: "Usuario"
                    )

                    if (supabaseId != null) {
                        // SUCESSO COMPLETO
                        val user = UserEntity(
                            isLoggedIn = true,
                            username = firebaseUser.displayName ?: "Usuario",
                            email = firebaseUser.email,
                            userToken = fireCode,
                            supabaseUserId = supabaseId
                        )
                        userDao.saveLoginStatus(user)
                    } else {
                        // FALHA: Credencial OK, mas perfil Supabase faltando
                        auth.signOut()
                        _errorFlow.value = "Perfil não encontrado no banco de dados."
                    }
                } else {
                    _errorFlow.value = "Falha ao obter usuário do Firebase."
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _errorFlow.value = "Credenciais inválidas ou erro de rede: ${e.message}"
            } finally {
                // Sempre desativa loading ao finalizar
                _manualLoading.value = false
            }
        }
    }

    fun handleGoogleSignIn(account: GoogleSignInAccount) {
        viewModelScope.launch {
            try {
                _manualLoading.value = true
                _errorFlow.value = null

                // 1. Pega o idToken da conta Google
                val idToken = account.idToken
                if (idToken == null) {
                    _errorFlow.value = "Erro: Token do Google está nulo."
                    return@launch
                }

                // 2. Cria a credencial do Firebase
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                // 3. FAZ O LOGIN NO FIREBASE
                val authResult = auth.signInWithCredential(credential).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    val supabaseId = usuarioRepositorio.getOrCreateSupabaseUser(
                        firebaseUid = firebaseUser.uid,
                        email = firebaseUser.email ?: "",
                        nome = firebaseUser.displayName ?: "Usuário Google"
                    )

                    if (supabaseId != null) {
                        val user = UserEntity(
                            isLoggedIn = true,
                            username = firebaseUser.displayName ?: "Usuário Google",
                            email = firebaseUser.email!!,
                            userToken = firebaseUser.uid,
                            supabaseUserId = supabaseId // Salvando o ID do Supabase
                        )
                        userDao.saveLoginStatus(user)
                    } else {
                        auth.signOut()
                        _errorFlow.value = "Falha ao sincronizar usuário Google com o banco."
                    }

                } else {
                    _errorFlow.value = "Falha ao obter usuário do Firebase."
                }

            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _errorFlow.value = "Falha no login com Google: ${e.message}"
            } finally {
                _manualLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorFlow.value = null
    }
}