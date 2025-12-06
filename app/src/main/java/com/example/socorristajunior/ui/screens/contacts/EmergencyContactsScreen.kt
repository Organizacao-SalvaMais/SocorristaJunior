package com.example.socorristajunior.ui.screens.contacts

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.BottomNavigationBar

// Modelo para contatos de emergência
data class EmergencyContact(
    val name: String,
    val phone: String,
    val icon: ImageVector,
    val iconColor: Color,
    val backgroundColor: Color,
    val description: String
)

// Estado da tela
data class EmergencyContactsState(
    val contacts: List<EmergencyContact> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyContactsScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val uiState = remember {
        EmergencyContactsState(
            contacts = getEmergencyContacts(),
            isLoading = false
        )
    }

    // Função para abrir o discador
    fun openDialer(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        ContextCompat.startActivity(context, intent, null)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color(0xFFE62727))) {
                                append("SALVAR")
                            }
                            append(" +")
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF3F2EC),
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF3F2EC)),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Contatos de Emergência",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE62727),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Serviços de Emergência",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Clique em qualquer serviço para discar automaticamente",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            items(uiState.contacts.size) { index ->
                EmergencyContactItem(
                    contact = uiState.contacts[index],
                    onContactClick = { phoneNumber ->
                        openDialer(phoneNumber)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Em caso de emergência, mantenha a calma e forneça informações claras sobre a localização e situação.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Função auxiliar para obter os contatos
private fun getEmergencyContacts(): List<EmergencyContact> {
    return listOf(
        EmergencyContact(
            name = "SAMU",
            phone = "192",
            icon = Icons.Default.LocalHospital,
            iconColor = Color.White,
            backgroundColor = Color(0xFFE62727),
            description = "Serviço de Atendimento Móvel de Urgência - Atendimento médico de emergência"
        ),
        EmergencyContact(
            name = "BOMBEIROS",
            phone = "193",
            icon = Icons.Default.LocalFireDepartment,
            iconColor = Color.White,
            backgroundColor = Color(0xFFA40000),
            description = "Corpo de Bombeiros Militar - Atendimento a incêndios e resgates"
        ),
        EmergencyContact(
            name = "POLÍCIA MILITAR",
            phone = "190",
            icon = Icons.Default.Security,
            iconColor = Color.White,
            backgroundColor = Color(0xFF517BCA),
            description = "Polícia Militar - Segurança pública e emergências policiais"
        ),
        EmergencyContact(
            name = "POLÍCIA CIVIL",
            phone = "181",
            icon = Icons.Default.Security,
            iconColor = Color.White,
            backgroundColor = Color(0xFF6B7280),
            description = "Polícia Civil - Investigações criminais e registros de ocorrência"
        ),
        EmergencyContact(
            name = "DEFESA CIVIL",
            phone = "199",
            icon = Icons.Default.Public,
            iconColor = Color.White,
            backgroundColor = Color(0xFFD7B681),
            description = "Defesa Civil - Prevenção e atendimento a desastres naturais"
        )
    )
}

@Composable
fun EmergencyContactItem(
    contact: EmergencyContact,
    onContactClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onContactClick(contact.phone) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(contact.backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = contact.icon,
                        contentDescription = "${contact.name} ícone",
                        tint = contact.iconColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = contact.phone,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = contact.backgroundColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = contact.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        lineHeight = 18.sp
                    )
                }
            }

            Text(
                text = contact.phone,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = contact.backgroundColor,
                fontSize = 22.sp
            )
        }
    }
}