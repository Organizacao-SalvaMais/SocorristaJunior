package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.DAO.QuizDAO
import jakarta.inject.Inject
import jakarta.inject.Singleton

// (MODIFICAÇÃO) Define o Repositório como um Singleton (só haverá uma instância dele)
@Singleton
// ️ (MODIFICAÇÃO) Habilita a injeção de dependência (Hilt)
class QuizRepo @Inject constructor(
    //️ (MODIFICAÇÃO) O Hilt irá injetar a instância do QuizDAO aqui
    private val quizDAO: QuizDAO
) {

    //️ (MODIFICAÇÃO) Expõe a função do DAO para o ViewModel
    // O QuizViewModel usará esta função para obter a lista de quizzes
    fun getQuizzes() = quizDAO.getAllQuizzes()

    //️ (MODIFICAÇÃO) Expõe a função do DAO para buscar por categoria
    fun getQuizzesByCategory(categoria: String) = quizDAO.getQuizzesByCategory(categoria)

    //️ (MODIFICAÇÃO) Expõe a função do DAO para buscar por ID
    fun getQuizById(id: Int) = quizDAO.getQuizById(id)
}