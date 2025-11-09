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
import com.example.socorristajunior.Data.model.Usuario
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
    private val auth: FirebaseAuth,
    private val usuarioRepositorio: UsuarioRepositorio
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
                // 1. Pega o idToken da conta Google
                val idToken = account.idToken
                if (idToken == null) {
                    _errorFlow.value = "Erro: Token do Google está nulo."
                    return@launch
                }

                // 2. Cria a credencial do Firebase
                val credential = GoogleAuthProvider.getCredential(idToken, null)

                // 3. FAZ O LOGIN NO FIREBASE (aqui está a chave)
                // .await() espera a tarefa do Firebase terminar
                val authResult = auth.signInWithCredential(credential).await()

                // 4. Pega o usuário que o Firebase acabou de criar/logar
                val firebaseUser = authResult.user

                // 5. Se o Firebase retornou um usuário com sucesso...
                if (firebaseUser != null) {

                    // INÍCIO DA LÓGICA DE CADASTRO NO SUPABASE

                    // 6. VERIFICA SE O USUÁRIO É NOVO
                    // O Firebase nos diz se essa credencial resultou em um novo cadastro
                    val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

                    if (isNewUser) {
                        // 7. SE FOR NOVO: Salva o perfil no Supabase
                        // (Esta é a lógica do seu CadastroViewModel)
                        val novoUsuario = Usuario(
                            id = null, // Deixa o Supabase gerar o PK
                            usunome = firebaseUser.displayName ?: "Usuário Google",
                            usuemail = firebaseUser.email!!,
                            firecodigo = firebaseUser.uid // Chave de ligação
                        )

                        // 8. CHAMA SUPABASE REPOSITORY
                        val dataSuccess = usuarioRepositorio.insertUser(novoUsuario)

                        if (!dataSuccess) {
                            // ERRO CRÍTICO: O usuário foi criado no Firebase,
                            // mas NÃO foi salvo no Supabase.
                            _errorFlow.value = "Falha ao criar seu perfil no banco de dados."

                            // Opcional, mas recomendado: desfaz o cadastro no Firebase
                            // para que o usuário possa tentar de novo
                            firebaseUser.delete().await()

                            return@launch // Aborta o login
                        }
                    }
                    // Se o usuário não for novo (isNewUser == false),
                    // ele simplesmente pula este bloco e vai para o login local.

                    // FIM DA LÓGICA DE CADASTRO NO SUPABASE


                    // 9. CRIA A ENTIDADE LOCAL (ROOM)
                    // Isso acontece para usuários novos (que acabaram de ser salvos no Supabase)
                    // e para usuários antigos (que já estavam no Supabase).
                    val user = UserEntity(
                        isLoggedIn = true,
                        username = firebaseUser.displayName ?: "Usuário Google",
                        email = firebaseUser.email!!,
                        userToken = firebaseUser.uid
                    )

                    // 10. Salva o usuário no nosso banco local (Room)
                    userDao.saveLoginStatus(user)

                } else {
                    // Se o Firebase não retornou um usuário
                    _errorFlow.value = "Falha ao obter usuário do Firebase."
                }

            } catch (e: Exception) {
                // ... (seu 'catch' continua igual)
                if (e is CancellationException) throw e
                _errorFlow.value = "Falha no login com Firebase: ${e.message}"
            }
        }
    }

    // Metodo para limpar o erro
    fun clearError() {
        _errorFlow.value = null
    }
}