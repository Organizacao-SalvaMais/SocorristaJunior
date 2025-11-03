package com.example.socorristajunior.Data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define que esta classe é uma "tabela" (entity) no banco de dados com o nome "quiz_table"
@Entity(tableName = "quiz_table")
data class Quiz(
    // Define a "chave primária" (primary key) da tabela.
    // 'autoGenerate = true' faz o Room criar um ID único para cada novo quiz.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // Armazena o texto da pergunta do quiz
    val pergunta: String,

    // Armazena a lista de opções de resposta (ex: ["A", "B", "C", "D"])
    // O Room não sabe salvar listas, então usaremos um TypeConverter para isso.
    val opcoes: List<String>,

    // Armazena o índice (0, 1, 2, ou 3) da resposta correta na lista 'opcoes'
    val respostaCorretaIndex: Int,

    // Armazena uma categoria para o quiz (ex: "Engasgo", "Queimadura")
    // Isso nos permite filtrar quizzes por tópico.
    val categoria: String
)
