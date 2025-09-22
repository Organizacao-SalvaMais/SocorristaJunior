package com.example.socorristajunior.ui.components

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun BotaoEmergencia(navController: NavController, modifier: Modifier = Modifier){
    Button(
        onClick = {
            Log.d("BotaoEmergencia", "Botão clicado!")
            navController.navigate("emergencies")
                  },
        modifier = Modifier
            .width(349.dp)
            .height(114.dp),
        shape = RoundedCornerShape(size = 10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE62727))
    ) {
        Text(text = "EMERGÊNCIA",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun BotaoEmergenciaFinalPreview() {
    // Para a preview, usamos um NavController "falso".
    BotaoEmergencia(navController = rememberNavController())
}