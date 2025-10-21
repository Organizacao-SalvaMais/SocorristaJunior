package com.example.socorristajunior.ui.cadastro
//CadastroViewModel.kt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.socorristajunior.Data.model.User
import kotlinx.coroutines.*

class RegisterViewModel : ViewModel() {

    private val repository = CadastroRepositorio()

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
            telefone.isBlank() || senha.isBlank()) {
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

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            val user = User(nomeCompleto, email, telefone, genero, dataNascimento, senha)
            val success = repository.registerUser(user)
            withContext(Dispatchers.Main) {
                _loading.value = false
                _registerStatus.value =
                    if (success) "Cadastro realizado com sucesso!" else "Erro ao cadastrar."
            }
        }
    }
}
