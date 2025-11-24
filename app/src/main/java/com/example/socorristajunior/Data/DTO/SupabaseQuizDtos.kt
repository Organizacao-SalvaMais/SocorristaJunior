package com.example.socorristajunior.Data.DTO

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Representa a tabela 'quizz' do Supabase.
 * A anotação @SerialName liga o nome da coluna no banco à variável Kotlin.
 */
@Serializable
data class QuizDto(
    @SerialName("quizzcodigo")
    val id: Int, // Mapeia a coluna 'quizzcodigo'

    @SerialName("quizznome")
    val nome: String, // Mapeia a coluna 'quizznome'

    @SerialName("quizzdescricao")
    val descricao: String? = null, // Pode ser nulo no banco

    // Esta lista NÃO existe na tabela 'quizz', mas o Supabase a preenche
    // automaticamente quando fazemos a query aninhada (select *, questaoquizz(*)).
    @SerialName("questaoquizz")
    val questoes: List<QuestaoDto>? = null
)

/**
 * Representa a tabela 'questaoquizz' do Supabase.
 */
@Serializable
data class QuestaoDto(
    @SerialName("quesquizzcodigo")
    val id: Int,

    @SerialName("textodaquest")
    val enunciado: String,

    // Como não vi coluna de imagem na tabela 'questaoquizz' do seu SQL,
    // deixei nulo ou padrão. Se tiver, adicione aqui.
    // val imagem: String? = null,

    @SerialName("fk_quizzcodigo")
    val quizId: Int,

    // O Supabase preenche esta lista na query aninhada (..., alternativa(*))
    @SerialName("alternativa")
    val alternativas: List<AlternativaDto>? = null
)

/**
 * Representa a tabela 'alternativa' do Supabase.
 */
@Serializable
data class AlternativaDto(
    @SerialName("altecodigo")
    val id: Int,

    @SerialName("altetexto")
    val texto: String,

    @SerialName("correto")
    val correta: Boolean,

    @SerialName("fk_questcodigo")
    val questaoId: Int
)