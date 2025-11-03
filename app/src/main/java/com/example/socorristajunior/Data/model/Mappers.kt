package com.example.socorristajunior.Data.model

fun EmergenciaJson.toEntity(): Emergencia {
    // Lógica para converter o código de gravidade em texto.
    val gravidadeString = when (this.emergravicodigo) {
        1 -> "Altíssima"
        2 -> "Alta"
        3 -> "Média"
        4 -> "Baixa"
        else -> "Não definida"
    }

    // Retorna uma nova instância da Entidade 'Emergencia' com os dados mapeados.
    return Emergencia(
        emernome = this.emernome,
        emerdesc = this.emerdesc,
        emergravidade = gravidadeString,
        emerimagem = this.emerimagem
        // 'categoria' e 'duracaoEstimada' serão nulos por padrão, como definido na entidade.
    )
}

fun QuizCategoriaDto.toEntity(): QuizCategory {
    // Converte de 'qzcodigo' para 'id', 'qznome' para 'nome', etc.
    return QuizCategory(
        id = this.qzcodigo,
        nome = this.qznome,
        descricao = this.qzdesc
    )
}

fun QuestaoDto.toEntity(categoriaId: Int): Question {
    // Converte 'qtcodigo' para 'id', etc., e armazena o ID da categoria pai
    return Question(
        id = this.qtcodigo,
        enunciado = this.qtenunciado,
        imagemUrl = this.qtimagem,
        quizCategoriaId = categoriaId // Salva a chave estrangeira (relação)
    )
}

fun OpcaoDto.toEntity(questaoId: Int): Option {
    // Converte 'opcodigo' para 'id', etc., e armazena o ID da questão pai
    return Option(
        id = this.opcodigo,
        resposta = this.opresposta,
        correta = this.opcorreta,
        questaoId = questaoId // Salva a chave estrangeira (relação)
    )
}