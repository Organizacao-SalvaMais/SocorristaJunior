package com.example.socorristajunior.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.DAO.UserDAO
import com.example.socorristajunior.Data.model.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class ProfileUiState(
    val user: UserEntity? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDAO
) : ViewModel() {

    // O UI State é derivado do Flow do Room (reativo)
    val uiState: StateFlow<ProfileUiState> = userDao.getLoggedUser()
        .map { userEntity ->
            ProfileUiState(
                user = userEntity,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState(isLoading = true)
        )

    fun signOut() {
        viewModelScope.launch {
            userDao.logout() // Chama a função de Logout
        }
    }

    // Lógica básica de salvamento (requer a UserEntity atualizada)
    fun saveProfile(
        username: String,
        email: String,
        phone: String,
        gender: String,
        dateOfBirth: String
    ) {
        viewModelScope.launch {
            val currentUser = uiState.value.user
            if (currentUser != null) {
                // Cria uma nova entidade com os dados atualizados
                val updatedUser = currentUser.copy(
                    username = username,
                    email = email,
                    phone = phone,
                    gender = gender,
                    dateOfBirth = dateOfBirth
                )
                userDao.saveLoginStatus(updatedUser)
            }
        }
    }
}