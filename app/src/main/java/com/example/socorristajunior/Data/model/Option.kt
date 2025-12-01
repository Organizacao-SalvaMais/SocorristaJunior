package com.example.socorristajunior.Data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidade para armazenar uma Opção de Resposta.
 * Corresponde a cada objeto no array "opcoes" do seu JSON.
 *
 * Define uma 'chave estrangeira' (foreign key) para ligar esta Opção
 * à sua Questão (Question).
 */
@Entity(
    tableName = "opcao_table", // Define o nome da tabela
    // Define a relação com a tabela 'questao_table'
    foreignKeys = [
        ForeignKey(
            entity = Question::class,           // Aponta para a entidade Question...
            parentColumns = ["id"],             // ...na coluna 'id' dela...
            childColumns = ["questaoId"],       // ...usando nossa coluna 'questaoId'.
            onDelete = ForeignKey.CASCADE       // Se a questão for deletada, deleta as opções.
        )
    ],
    // Cria um índice na chave estrangeira
    indices = [Index(value = ["questaoId"])]
)
data class Option(
    // Usa o 'opcodigo' do JSON como chave primária
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    // Armazena a 'opresposta'
    val resposta: String,

    // Armazena a 'opcorreta' (true ou false)
    val correta: Boolean,

    // Armazena o ID da Questão à qual esta opção pertence
    val questaoId: Int
)