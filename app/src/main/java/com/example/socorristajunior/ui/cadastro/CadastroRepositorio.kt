package com.example.socorristajunior.ui.cadastro
//CadastroRepositorio.kt
import com.example.socorristajunior.Data.model.User

class CadastroRepositorio {
    fun registerUser(user: User): Boolean {
        return user.email.isNotEmpty() && user.senha.length >= 6
    }
}
