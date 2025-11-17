package com.example.socorristajunior.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.model.UserEntity
import com.example.socorristajunior.Domain.Repositorio.UsuarioRepositorio
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class ProfileUiState(
    val user: UserEntity? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false // Estado para o indicador de exclusão/salvamento
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDAO,
    private val auth: FirebaseAuth,
    private val usuarioRepositorio: UsuarioRepositorio
) : ViewModel() {

    // Estado de login e dados do perfil
    private val _userEntityFlow: StateFlow<UserEntity?> = userDao.getLoggedUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Estado de salvamento/loading para ações
    private val _isSavingFlow = MutableStateFlow(false)

    // Combina as duas fontes
    val uiState: StateFlow<ProfileUiState> = combine(
        _userEntityFlow,
        _isSavingFlow
    ) { userEntity, isSaving ->
        // O isLoading é true se o userEntity ainda não foi carregado (inicia nulo)
        val isLoading = userEntity == null && _userEntityFlow.value == null

        ProfileUiState(
            user = userEntity,
            isLoading = isLoading,
            isSaving = isSaving // Usa o estado mutável
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState(isLoading = true)
    )

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    fun openDeleteDialog() {
        _showDeleteDialog.value = true
    }

    fun closeDeleteDialog() {
        _showDeleteDialog.value = false
    }

    fun signOut() {
        viewModelScope.launch {
            auth.signOut() // Desloga do Firebase
            userDao.logout() // Limpa o estado local
        }
    }

    // Lógica de salvamento
    fun saveProfile(username: String, email: String) {
        // Define o estado de salvamento ANTES de iniciar a coroutine
        _isSavingFlow.value = true

        viewModelScope.launch {
            try {
                // A lógica de salvamento aqui é simplificada, pois não envolve Supabase
                // para edição de perfil. Apenas atualiza o Room.
                val currentUser = _userEntityFlow.value
                if (currentUser != null) {
                    val updatedUser = currentUser.copy(username = username, email = email)
                    userDao.saveLoginStatus(updatedUser)
                }
            } catch (e: Exception) {
                println("ERRO AO SALVAR PERFIL: ${e.message}")
            } finally {
                // 💡 Sempre define como false ao terminar
                _isSavingFlow.value = false
            }
        }
    }

    fun deleteAccount() {
        val currentUser = auth.currentUser

        // Verificações de segurança
        if (currentUser == null || _userEntityFlow.value == null) return

        viewModelScope.launch {
            // Define o estado de loading de exclusão
            _isSavingFlow.value = true

            try {
                val fireCode = currentUser.uid

                // 1. SUPABASE: Deleta os dados do perfil
                val supabaseSuccess = usuarioRepositorio.deleteUser(fireCode)

                if (!supabaseSuccess) {
                    throw Exception("Falha ao deletar perfil no Supabase.")
                }

                // 2. FIREBASE: Deleta a credencial de segurança
                currentUser.delete().await()

                // 3. ROOM/DAO: Limpa o estado local (dispara navegação na UI)
                userDao.logout()

            } catch (e: Exception) {
                println("ERRO AO DELETAR CONTA: ${e.message}")

            } finally {
                // 💡 Limpa o indicador de loading após o processo
                _isSavingFlow.value = false
            }
        }
    }
    fun reauthenticateAndDelete(password: String) {
        val currentUser = auth.currentUser
        val userProfile = uiState.value.user

        // Fechamos o diálogo imediatamente
        closeDeleteDialog()

        if (currentUser == null || userProfile == null) return

        viewModelScope.launch {
            _isSavingFlow.value = true // Indica que uma operação crítica está em andamento

            try {
                // 1. REAUTENTICAÇÃO: Cria a credencial
                val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)

                // 2. Tenta fazer login com a credencial (reautenticação)
                currentUser.reauthenticate(credential).await()

                // Se a reautenticação for bem-sucedida, prosseguimos para a exclusão

                val fireCode = currentUser.uid

                // 3. SUPABASE: Deleta os dados do perfil
                val supabaseSuccess = usuarioRepositorio.deleteUser(fireCode)

                if (!supabaseSuccess) {
                    // Lidar com falha de rollback ou erro de consistência de dados
                    throw Exception("Falha ao deletar perfil no Supabase.")
                }

                // 4. FIREBASE: Deleta a credencial
                currentUser.delete().await()

                // 5. ROOM/DAO: Limpa o estado local
                userDao.logout()

            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is FirebaseAuthInvalidCredentialsException -> "Senha incorreta. A exclusão foi cancelada."
                    is FirebaseAuthRecentLoginRequiredException -> "Sua sessão expirou. Faça logout e login novamente para confirmar."
                    else -> "Erro ao deletar conta: ${e.message}"
                }
                // 💡 Reporte o erro para um Snackbar ou Flow de Erro (o seu atual println fará isso)
                println("ERRO AO DELETAR CONTA: $errorMessage")

            } finally {
                _isSavingFlow.value = false
            }
        }
    }
}