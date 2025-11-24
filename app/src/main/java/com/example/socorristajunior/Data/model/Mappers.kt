package com.example.socorristajunior.Data.model

import com.example.socorristajunior.Data.DTO.AlternativaDto
import com.example.socorristajunior.Data.DTO.EmergenciaApiDto
import com.example.socorristajunior.Data.DTO.PassoApiDto
import com.example.socorristajunior.Data.DTO.QuestaoDto
import com.example.socorristajunior.Data.DTO.QuizDto

/*
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
}*/

fun EmergenciaApiDto.toEntity(): Emergencia {
    return Emergencia(
        emercodigo = this.emercodigo,
        emernome = this.emernome,
        emerdesc = this.emerdesc,
        emerimagem = this.emerimagem,
        gravidadeNome = this.gravidade?.gravnome ?: "Não definida",
        gravidadeCor = this.gravidade?.gravicor,
        fonteNome = this.fontes?.fonnome ?: "Não definida",
        fonteUrl = this.fontes?.url
    )
}

fun PassoApiDto.toEntity(): Passo {
    return Passo(
        pascodigo = this.pascodigo,
        pasnome = this.pasnome,
        pasimagem = this.pasimagem,
        pasdescricao = this.pasdescricao,
        pasordem = this.pasordem,
        fk_emercodigo = this.fk_emercodigo
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

// Converte QuizDto (Supabase) -> QuizCategory (Room)
fun QuizDto.toEntity(): QuizCategory {
    return QuizCategory(
        id = this.id, // ID vindo do Supabase
        nome = this.nome,
        descricao = this.descricao ?: "" // Se for nulo, salva string vazia
    )
}

fun QuestaoDto.toEntity(): Question {
    return Question(
        id = this.id,
        enunciado = this.enunciado,
        imagemUrl = null, // Seu SQL não tinha imagem na tabela questaoquizz, ajustamos aqui
        quizCategoriaId = this.quizId
    )
}

fun AlternativaDto.toEntity(): Option {
    return Option(
        id = this.id,
        resposta = this.texto,
        correta = this.correta,
        questaoId = this.questaoId
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