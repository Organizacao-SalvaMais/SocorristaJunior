package com.example.socorristajunior.ui.screens.cadastro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socorristajunior.Data.model.UserEntity
import com.example.socorristajunior.Domain.Repositorio.CadastroRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CadastroViewModel @Inject constructor( // 2. ADICIONE "@Inject constructor"
    private val repository: CadastroRepositorio // 3. Injete o repositório aqui
) : ViewModel() {

    private val _registerStatus = MutableLiveData<String>()
    val registerStatus: LiveData<String> = _registerStatus

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun register(
        nomeCompleto: String,
        email: String,
        telefone: String,
        genero: String,
        dataNascimento: String,
        senha: String,
        confirmarSenha: String
    ) {
        if (nomeCompleto.isBlank() || email.isBlank() ||
            telefone.isBlank() || senha.isBlank()
        ) {
            _registerStatus.value = "Preencha todos os campos obrigatórios."
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registerStatus.value = "E-mail inválido."
            return
        }

        if (!telefone.matches(Regex("\\(\\d{2}\\) \\d{5}-\\d{4}"))) {
            _registerStatus.value = "Telefone inválido."
            return
        }

        if (senha.length < 6) {
            _registerStatus.value = "A senha deve ter no mínimo 6 caracteres."
            return
        }

        if (senha != confirmarSenha) {
            _registerStatus.value = "As senhas não coincidem."
            return
        }

        _loading.value = true

        // USE viewModelScope:
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                // delay(2000) // Você pode remover o delay agora
                val user = UserEntity(
                    isLoggedIn = true,
                    username = nomeCompleto,
                    email = email,
                    phone = telefone,
                    gender = genero,
                    dateOfBirth = dataNascimento
                )
                repository.registerUser(user)
            }

            _loading.value = false
            _registerStatus.value =
                if (success) "Cadastro realizado com sucesso!" else "Erro ao cadastrar."
        }
    }
}