package com.example.socorristajunior.Data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_categoria_table") // Define o nome da tabela
data class QuizCategory(
    // Usa o 'qzcodigo' do JSON como chave primária.
    // 'autoGenerate = false' pois o ID vem do JSON.
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    // Armazena o 'qznome'
    val nome: String,

    // Armazena o 'qzdesc'
    val descricao: String
)