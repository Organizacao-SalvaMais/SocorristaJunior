package com.example.socorristajunior.Domain.Repositorio

import com.example.socorristajunior.Data.DAO.QuizCategoryDAO
import com.example.socorristajunior.Data.DAO.QuizDAO
import jakarta.inject.Inject
import jakarta.inject.Singleton

// (MODIFICAÇÃO) Define o Repositório como um Singleton (só haverá uma instância dele)
@Singleton
class QuizRepo @Inject constructor(
    //  (MODIFICAÇÃO) Injeta o DAO de Categoria
    private val quizCategoryDAO: QuizCategoryDAO
) {

    // ️ (MODIFICAÇÃO) Função para buscar todas as categorias (para a lista de quizzes)
    fun getCategorias() = quizCategoryDAO.getCategorias()

    // ️ (MODIFICAÇÃO) Função para buscar um quiz completo (Categoria + Questões + Opções)
    fun getQuizCompleto(categoriaId: Int) =
        quizCategoryDAO.getCategoriaComQuestoes(categoriaId)
}