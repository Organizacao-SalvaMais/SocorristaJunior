package com.example.socorristajunior.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

// Define uma classe de dados para representar um serviço de emergência.
// Isso torna o código mais limpo e fácil de gerenciar.
data class EmergencyService(
    val number: String, // O número de telefone do serviço.
    val label: String,  // O nome do serviço (ex: "SAMU").
    val color: Color    // A cor de fundo do círculo.
)

/**
 * Componente reutilizável para exibir um único botão de serviço de emergência.
 *
 * @param service O objeto EmergencyService contendo os dados a serem exibidos.
 * @param modifier O modificador a ser aplicado a este componente.
 */
@Composable
fun EmergencyServiceButton(service: EmergencyService, modifier: Modifier = Modifier) {
    // Column organiza os elementos verticalmente (o círculo acima do texto).
    Column(
        // Aplica o modificador passado para este componente.
        modifier = modifier,
        // Alinha todos os itens da coluna no centro horizontalmente.
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Box é usado para sobrepor elementos. Aqui, ele cria o círculo e coloca o texto do número dentro dele.
        Box(
            // Define o tamanho do círculo.
            modifier = Modifier
                .size(65.dp)
                // Aplica uma forma de círculo ao fundo.
                .clip(CircleShape)
                // Define a cor de fundo do círculo.
                .background(service.color),
            // Centraliza o conteúdo (o texto do número) dentro da Box.
            contentAlignment = Alignment.Center
        ) {
            // Exibe o texto do número de emergência.
            Text(
                // Pega o número do objeto de serviço.
                text = service.number,
                // Define a cor do texto como branca.
                color = Color.White,
                // Define o tamanho da fonte.
                fontSize = 20.sp,
                // Define o peso da fonte como negrito para melhor legibilidade.
                fontWeight = FontWeight.Bold
            )
        }
        // Adiciona um pequeno espaço vertical entre o círculo e o texto do rótulo.
        Spacer(modifier = Modifier.height(8.dp))
        // Exibe o texto do rótulo (nome do serviço).
        Text(
            // Pega o rótulo do objeto de serviço.
            text = service.label,
            // Define a cor do texto como preta.
            color = Color.Black,
            // Define o tamanho da fonte.
            fontSize = 12.sp,
            // Alinha o texto no centro, caso ele tenha mais de uma linha.
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Componente que monta o grid completo de contatos de emergência.
 */
@Composable
fun EmergencyContactsGrid() {
    // Lista dos serviços de emergência a serem exibidos.
    val services = listOf(
        EmergencyService("181", "Polícia Civil", Color(0xFFD3D3D3)), // Cinza claro
        EmergencyService("199", "Defesa Civil", Color(0xFFD7B681)), // Dourado/Bege
        EmergencyService("192", "SAMU", Color(0xFFEE4949)),         // Vermelho claro
        EmergencyService("190", "Polícia Militar", Color(0xFF517BCA)), // Azul
        EmergencyService("193", "BOMBEIROS", Color(0xFFA40000))     // Vermelho escuro
    )

    // ConstraintLayout permite posicionar componentes de forma relativa uns aos outros, ideal para layouts complexos.
    ConstraintLayout(
        // Aplica modificadores ao layout principal.
        modifier = Modifier
            // Define um padding (espaçamento interno) ao redor de todo o grid.
            .padding(16.dp)
            // Aplica um fundo com cantos arredondados.
            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(20.dp))
            // Adiciona um padding interno para os itens não ficarem colados nas bordas.
            .padding(16.dp)
    ) {
        // Cria referências para cada um dos 5 botões para poder posicioná-los.
        val (policiaCivil, defesaCivil, samu, policiaMilitar, bombeiros) = createRefs()

        // Cria o botão da Polícia Civil e o associa à sua referência.
        EmergencyServiceButton(
            service = services[0],
            modifier = Modifier.constrainAs(policiaCivil) {
                // Posiciona o topo do botão no topo do container pai.
                top.linkTo(parent.top)
            }
        )

        // Cria o botão da Defesa Civil e o associa à sua referência.
        EmergencyServiceButton(
            service = services[1],
            modifier = Modifier.constrainAs(defesaCivil) {
                // Posiciona o topo do botão no topo do container pai.
                top.linkTo(parent.top)
            }
        )

        // Cria o botão do SAMU e o associa à sua referência.
        EmergencyServiceButton(
            service = services[2],
            modifier = Modifier.constrainAs(samu) {
                // Posiciona o topo do botão no topo do container pai.
                top.linkTo(parent.top)
            }
        )

        // Cria uma "corrente" horizontal para os três botões da primeira linha.
        // Isso os distribui igualmente no espaço disponível.
        createHorizontalChain(policiaCivil, defesaCivil, samu)

        // Cria o botão da Polícia Militar e o associa à sua referência.
        EmergencyServiceButton(
            service = services[3],
            modifier = Modifier.constrainAs(policiaMilitar) {
                // Posiciona o topo deste botão abaixo do botão da Defesa Civil, com uma margem.
                top.linkTo(defesaCivil.bottom, margin = 16.dp)
                // Alinha o início deste botão com o início do botão da Polícia Civil.
                start.linkTo(policiaCivil.start)
                // Alinha o fim deste botão com o fim do botão da Defesa Civil.
                end.linkTo(defesaCivil.end)
            }
        )

        // Cria o botão dos Bombeiros e o associa à sua referência.
        EmergencyServiceButton(
            service = services[4],
            modifier = Modifier.constrainAs(bombeiros) {
                // Posiciona o topo deste botão abaixo do botão da Defesa Civil, com uma margem.
                top.linkTo(defesaCivil.bottom, margin = 16.dp)
                // Alinha o início deste botão com o início do botão da Defesa Civil.
                start.linkTo(defesaCivil.start)
                // Alinha o fim deste botão com o fim do botão do SAMU.
                end.linkTo(samu.end)
            }
        )
    }
}

/**
 * Preview para visualizar o componente no Android Studio sem precisar executar o app.
 */
@Preview(showBackground = true)
@Composable
fun EmergencyContactsGridPreview() {
    // Box para centralizar o componente na tela de preview.
    Box(
        modifier = Modifier
            .fillMaxSize() // Ocupa a tela inteira.
            .background(Color.White), // Define um fundo branco para o preview.
        contentAlignment = Alignment.Center // Centraliza o conteúdo.
    ) {
        // Chama o componente principal para ser exibido no preview.
        EmergencyContactsGrid()
    }
}