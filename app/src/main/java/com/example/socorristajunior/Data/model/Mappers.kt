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