package com.example.socorristajunior.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun BotaoLigar(
    numero: String,    // O número para discar (ex: "192")
    cor: Color         // A cor do container
) {
    val context = LocalContext.current

    FloatingActionButton(
        onClick = {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$numero")
            }
            context.startActivity(intent)
        },
        containerColor = cor,
        contentColor = Color.White, // Mantemos branco para garantir contraste
    ) {
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Ligar para $numero",
        )
    }
}