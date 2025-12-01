package com.example.socorristajunior.Data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidade para armazenar uma Questão.
 * Corresponde a cada objeto no array "questoes" do seu JSON.
 *
 * Define uma 'chave estrangeira' (foreign key) para ligar esta Questão
 * à sua Categoria de Quiz (QuizCategory).
 */
@Entity(
    tableName = "questao_table", // Define o nome da tabela
    // Define a relação: a coluna 'quizCategoriaId' desta tabela...
    foreignKeys = [
        ForeignKey(
            entity = QuizCategory::class,       // ...aponta para a entidade QuizCategory...
            parentColumns = ["id"],             // ...na coluna 'id' dela...
            childColumns = ["quizCategoriaId"], // ...usando nossa coluna 'quizCategoriaId'.
            onDelete = ForeignKey.CASCADE       // Se a categoria for deletada, deleta as questões.
        )
    ],
    // Cria um índice na chave estrangeira para otimizar as buscas
    indices = [Index(value = ["quizCategoriaId"])]
)
data class Question(
    // Usa o 'qtcodigo' do JSON como chave primária
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    // Armazena o 'qtenunciado'
    val enunciado: String,

    // Armazena a 'qtimagem' (pode ser nula)
    val imagemUrl: String?,

    // Armazena o ID da Categoria de Quiz à qual esta questão pertence
    val quizCategoriaId: Int
)