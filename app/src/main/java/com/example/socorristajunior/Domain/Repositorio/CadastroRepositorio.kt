package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.model.UserEntity

class CadastroRepositorio {
    fun registerUser(user: UserEntity): Boolean {
        return user.email!!.isNotEmpty() && user.userToken!!.length >= 6
    }
}