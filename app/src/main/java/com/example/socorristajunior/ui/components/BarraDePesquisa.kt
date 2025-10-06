package com.example.socorristajunior.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BarraDePesquisa(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onSearchSubmit: () -> Unit,
    modifier: Modifier = Modifier
){
    val keyboardController = LocalSoftwareKeyboardController.current

    // Row é o container, para alinhar os elementos
    Row(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0xFFA9A9A9), shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
            .width(428.dp)
            .height(102.dp)
            .background(color = Color(0xFFF3F2EC), shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 20.dp, bottomEnd = 20.dp))
    ) {
        // Icon da Lupa
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Ícone de Pesquisa",
            tint = Color(0xFF008B8B), // Uma cor similar ao da sua imagem
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            modifier = Modifier.weight(1f), // Faz o campo de texto ocupartodo o espaço restante
            placeholder = { Text("Pesquisar") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchSubmit()
                    keyboardController?.hide()
                }
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { /* Ação para abrir o menu */ }) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Ícone de Menu",
                tint = Color(0xFFA9A9A9),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF808080)
@Composable
fun BaraDePesquisaFigmaPreview() {
    Column(modifier = Modifier.padding(20.dp)) {
        BarraDePesquisa(
            searchText = "Texto",
            onSearchTextChanged = {},
            onSearchSubmit = {}
        )
    }
}