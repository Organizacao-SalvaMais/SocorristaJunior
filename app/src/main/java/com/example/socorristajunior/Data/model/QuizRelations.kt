package com.example.socorristajunior.Data.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * POJO (Plain Old Kotlin Object) para unir Questões e Opções.
 * Usado pelo Room para construir o objeto complexo.
 */
data class QuestionWithOptions(
    // Traz o objeto Question principal
    @Embedded
    val questao: Question,

    // Traz a lista de Opções filhas
    @Relation(
        parentColumn = "id", // 'id' da Question
        entityColumn = "questaoId" // 'questaoId' da Option
    )
    val opcoes: List<Option>
)

/**
 * POJO para unir a Categoria com sua lista de Questões (que já incluem as Opções).
 */
data class QuizCategoryWithQuestions(
    // Traz o objeto QuizCategory principal
    @Embedded
    val categoria: QuizCategory,

    // Traz a lista de Questões (com suas Opções) filhas
    @Relation(
        entity = Question::class, // A entidade "filha" é a Question
        parentColumn = "id", // 'id' da QuizCategory
        entityColumn = "quizCategoriaId" // 'quizCategoriaId' da Question
    )
    val questoesComOpcoes: List<QuestionWithOptions>
)