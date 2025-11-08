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

import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException


//const val LOGIN_ROUTE = "login_route"
//const val MAIN_SCREEN_ROUTE = "main_screen_route"

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDao: UserDAO,
    private val auth: FirebaseAuth
) : ViewModel() {

    // 1. Flow privado para os erros
    private val _errorFlow = MutableStateFlow<String?>(null)

    // 2. Flow privado para o status do DB (login)
    private val _dbLoginStatus = userDao.getLoggedUser()
        .map { userEntity ->
            // Par(isLoggedIn, isLoading)
            Pair(userEntity?.isLoggedIn ?: false, false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Pair(false, true) // Valor inicial: (logado=false, carregando=true)
        )

    // 1. O Estado é derivado do Room Flow
    val uiState: StateFlow<AuthUiState> = combine(
        _dbLoginStatus,
        _errorFlow
    ) { dbStatus, error ->
        AuthUiState(
            isLoggedIn = dbStatus.first,  // Vem do _dbLoginStatus
            isLoading = dbStatus.second, // Vem do _dbLoginStatus
            errorMessage = error         // Vem do _errorFlow
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AuthUiState(isLoading = true) // Estado inicial antes de tudo
    )

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            // Simulação de login
            if (email == "teste@gmail.com" && password == "123456") {
                val user = UserEntity(
                    isLoggedIn = true,
                    username = "Teste Junior",
                    email = email,
                    userToken = "TOKEN-SIMULADO-123"
                )
                userDao.saveLoginStatus(user) // Salva no Room, ativando o login
            } else {
                _errorFlow.value = "Email ou senha inválidos."
            }
        }
    }

    fun handleGoogleSignIn(account: GoogleSignInAccount) {
        // Inicia uma coroutine no escopo do ViewModel
        viewModelScope.launch {
            try {
                // 1. Pega o idToken da conta Google (precisamos disso para o Firebase)
                val idToken = account.idToken

                // Validação: Se o token for nulo, o login não pode continuar
                if (idToken == null) {
                    _errorFlow.value = "Erro: Token do Google está nulo."
                    return@launch // Aborta a coroutine
                }

                // 2. Cria a credencial do Firebase usando o token do Google
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                // 3. FAZ O LOGIN NO FIREBASE (a parte que faltava)
                // .await() espera a tarefa do Firebase terminar (requer a dependência 'kotlinx-coroutines-play-services')
                val authResult = auth.signInWithCredential(credential).await()

                // 4. Pega o usuário que o Firebase acabou de criar/logar
                val firebaseUser = authResult.user

                // 5. Se o Firebase retornou um usuário com sucesso...
                if (firebaseUser != null) {
                    // Cria a nossa entidade UserEntity local
                    val user = UserEntity(
                        isLoggedIn = true,
                        // Usa o nome e email do usuário do Firebase
                        username = firebaseUser.displayName ?: "Usuário Google",
                        email = firebaseUser.email!!,
                        // MUITO IMPORTANTE: Usa o 'uid' do Firebase como nosso token/ID principal
                        userToken = firebaseUser.uid
                    )

                    // 6. AGORA SIM, salvamos o usuário no nosso banco local (Room)
                    userDao.saveLoginStatus(user)

                } else {
                    // Se o Firebase não retornou um usuário
                    _errorFlow.value = "Falha ao obter usuário do Firebase."
                }

            } catch (e: Exception) {
                // Captura qualquer erro que possa acontecer (ex: rede, usuário cancelou)

                // Se for um cancelamento da coroutine, relança o erro
                if (e is CancellationException) throw e

                // Mostra a mensagem de erro para o usuário
                _errorFlow.value = "Falha no login com Firebase: ${e.message}"
            }
        }
    }

    // Metodo para limpar o erro
    fun clearError() {
        _errorFlow.value = null
    }
}