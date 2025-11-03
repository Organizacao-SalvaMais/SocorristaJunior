package com.example.socorristajunior.Data.model

// Classes DTO (Data Transfer Object) usadas APENAS para parsear o JSON
// Os nomes das variáveis devem bater exatamente com o JSON

// Nível 1: O objeto principal da resposta
data class QuizApiResponse(
    val status: String,
    val message: String,
    val data: List<QuizCategoriaDto>
)

// Nível 2: O objeto da Categoria
data class QuizCategoriaDto(
    val qzcodigo: Int,
    val qznome: String,
    val qzdesc: String,
    val questoes: List<QuestaoDto>
)

// Nível 3: O objeto da Questão
data class QuestaoDto(
    val qtcodigo: Int,
    val qtenunciado: String,
    val qtimagem: String?,
    val opcoes: List<OpcaoDto>
)

// Nível 4: O objeto da Opção
data class OpcaoDto(
    val opcodigo: Int,
    val opresposta: String,
    val opcorreta: Boolean
)