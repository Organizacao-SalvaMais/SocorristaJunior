package com.example.socorristajunior.ui.components

import androidx.compose.ui.graphics.Color

fun getCorPorNome(nomeCor: String?): Color {
    // Normaliza o texto para minúsculas para evitar erros com "Vermelho" vs "vermelho"
    return when (nomeCor?.lowercase()) {
        "vermelho" -> Color(0xFFE57373) // Vermelho suave (para não agredir a vista no modo escuro)
        "laranja" -> Color(0xFFFFB74D)  // Laranja
        "amarelo" -> Color(0xFFFFF176)  // Amarelo
        "verde" -> Color(0xFF81C784)    // Verde
        "azul" -> Color(0xFF64B5F6)     // Azul
        else -> Color.Gray              // Cor padrão caso não encontre (Cinza)
    }
}