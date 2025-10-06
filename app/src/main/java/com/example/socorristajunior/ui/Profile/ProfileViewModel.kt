package com.example.socorristajunior.ui.Profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Modelo de dados de exemplo para o usuário
data class User(
    val id: Int = 1, // Exemplo de ID fixo
    val username: String,
    val email: String,
    val phone: String,
    val gender: String,
    val dateOfBirth: String
)

// Estado da UI para a tela de perfil
data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Simulação de carregamento de dados do usuário
            val dummyUser = User(
                username = "Usuário de Teste",
                email = "teste@gmail.com",
                phone = "(92) 99745-5208",
                gender = "Masculino",
                dateOfBirth = "11/07/2005"
            )
            _uiState.update { it.copy(user = dummyUser, isLoading = false) }
        }
    }

    fun saveProfile(
        username: String,
        email: String,
        phone: String,
        gender: String,
        dateOfBirth: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            // Simulação de salvamento de dados
            val updatedUser = _uiState.value.user?.copy(
                username = username,
                email = email,
                phone = phone,
                gender = gender,
                dateOfBirth = dateOfBirth
            )
            _uiState.update { it.copy(user = updatedUser, isSaving = false, errorMessage = null) }
        }
    }
}