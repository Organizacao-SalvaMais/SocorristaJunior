package com.example.socorristajunior.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.model.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


//const val LOGIN_ROUTE = "login_route"
//const val MAIN_SCREEN_ROUTE = "main_screen_route"

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDao: UserDAO
) : ViewModel() {

    // 1. O Estado é derivado do Room Flow
    val uiState: StateFlow<AuthUiState> = userDao.getLoggedUser()
        .map { userEntity ->
            AuthUiState(
                isLoggedIn = userEntity?.isLoggedIn ?: false,
                isLoading = false // Carregamento inicial do DB concluído
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthUiState(isLoading = true)
        )

    private val _errorFlow = MutableStateFlow<String?>(null)

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

    // Método para limpar o erro
    fun clearError() {
        _errorFlow.value = null
    }
}