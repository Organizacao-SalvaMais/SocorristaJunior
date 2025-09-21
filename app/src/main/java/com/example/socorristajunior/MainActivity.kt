package com.example.socorristajunior

import android.R.color.black
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.socorristajunior.ui.theme.SocorristaJuniorTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocorristaJuniorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        // 2. O padding do Scaffold é aplicado UMA VEZ no Column.
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        // 3. (Opcional) Adiciona um espaçamento de 8.dp entre cada item.
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        // 4. (Opcional) Centraliza os itens horizontalmente na tela.
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BarraDePesquisa(modifier = Modifier.padding(innerPadding))
                        Greeting(
                            modifier = Modifier.padding(innerPadding)
                        )
                        BotaoEmergencia(navController = rememberNavController())
                        Spacer(modifier = Modifier.width(8.dp))
                        BotaoQuizzes(navController = rememberNavController())
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = "Bem vindo ao\n Salva+\n",
        modifier = modifier,
        color = Color(0xFFE62727)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SocorristaJuniorTheme {
        Greeting()
    }
}



@Composable
fun BotaoEmergencia(navController: NavController, modifier: Modifier = Modifier){
    Button(
        onClick = { /*Vai para area de Emergencia*/ },
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

@Composable
fun BotaoQuizzes(navController: NavController, modifier: Modifier = Modifier){
    Button(
        onClick = { /*Vai para area de Emergencia*/ },
        modifier = Modifier
            .width(349.dp)
            .height(114.dp),
        shape = RoundedCornerShape(size = 10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008B8B))
    ) {
        Text(text = "QUIZZES",
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

@Preview(showBackground = true)
@Composable
fun BotaoQuizzesFinalPreview() {
    // Para a preview, usamos um NavController "falso".
    BotaoQuizzes(navController = rememberNavController())
}

@Composable
fun BarraDePesquisa(modifier: Modifier = Modifier){
    // Para guardar o texto digitado pelo usuário
    var searchText by remember { mutableStateOf("") }

    // Row é o container, para alinhar os elementos
    Row(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0xFFA9A9A9), shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
            .width(428.dp)
            .height(102.dp)
            .background(color = Color(0xFFF3F2EC), shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon da Lupa
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Ícone de Pesquisa",
            tint = Color(0xFF008B8B), // Uma cor similar ao da sua imagem
            modifier = Modifier.size(42.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.weight(1f), // Faz o campo de texto ocupartodo o espaço restante
            placeholder = {
                // O texto que aparece quando o campo está vazio.
                Text("Pesquisar...", fontSize = 28.sp, color = Color(0xFFA9A9A9))
            },
            textStyle = TextStyle(fontSize = 28.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFFA9A9A9),
                focusedTextColor = Color(0xFFA9A9A9),
                unfocusedTextColor = Color(0xFFA9A9A9),
                disabledTextColor = Color(0xFFA9A9A9),
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { /* Ação para abrir o menu */ }) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Ícone de Menu",
                tint = Color(0xFFA9A9A9),
                modifier = Modifier.size(42.dp)
            )
        }
    }

}

@Preview(showBackground = true, backgroundColor = 0xFF808080)
@Composable
fun SearchBarFigmaPreview() {
    // Adicionamos um padding para ver melhor a sombra/borda no preview
    Column(modifier = Modifier.padding(20.dp)) {
        BarraDePesquisa()
    }
}