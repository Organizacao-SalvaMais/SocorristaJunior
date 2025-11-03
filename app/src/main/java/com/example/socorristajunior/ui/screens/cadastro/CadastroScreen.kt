package com.example.socorristajunior.ui.screens.cadastro

/*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.livedata.observeAsState
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class) // Necessário para o ExposedDropdownMenuBox
@Composable
fun CadastroScreen(
    navController: NavController,
    viewModel: CadastroViewModel = hiltViewModel()
) {
    // --- Estados para os campos de texto ---
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var nascimento by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    // --- Estado para o Gênero (Spinner) ---
    val generos = listOf("Selecione", "Feminino", "Masculino", "Outro")
    var generoSelecionado by remember { mutableStateOf(generos[0]) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // --- Observa os estados do ViewModel ---
    val isLoading by viewModel.loading.observeAsState(initial = false)
    val registerStatus by viewModel.registerStatus.observeAsState()

    val context = LocalContext.current

    // Mostra o Toast quando o 'registerStatus' mudar
    LaunchedEffect(registerStatus) {
        registerStatus?.let { statusMsg ->
            Toast.makeText(context, statusMsg, Toast.LENGTH_SHORT).show()
            if (statusMsg == "Cadastro realizado com sucesso!") {
                navController.popBackStack() // Volta para a tela anterior
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Criar Conta") })
        }
    ) { padding ->
        // O ScrollView é traduzido para um Column com 'verticalScroll'
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // Padding do Scaffold
                .padding(horizontal = 24.dp, vertical = 16.dp) // Padding do seu LinearLayout
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Título (do seu XML)
            Text(
                text = "Criar Conta",
                color = MaterialTheme.colorScheme.onBackground, // Equivalente a @color/black
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Nome Completo
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Digite seu nome completo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("seu@email.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Telefone
            OutlinedTextField(
                value = telefone,
                onValueChange = {
                    // Você pode adicionar sua lógica de máscara aqui
                    telefone = it
                },
                label = { Text("(00) 00000-0000") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Gênero (Substituindo o Spinner)
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                OutlinedTextField(
                    value = generoSelecionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gênero") },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Abrir Gêneros")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor() // Importante para o Dropdown
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    generos.forEach { genero ->
                        DropdownMenuItem(
                            text = { Text(genero) },
                            onClick = {
                                generoSelecionado = genero
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Data de Nascimento
            // NOTA: O ideal aqui é usar um DatePickerDialog
            OutlinedTextField(
                value = nascimento,
                onValueChange = { nascimento = it },
                label = { Text("dd/mm/aaaa") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Senha
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("Mínimo 6 caracteres") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Confirmar Senha
            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                label = { Text("Digite a senha novamente") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )

            // Botão de Cadastrar
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        viewModel.register(
                            nomeCompleto = nome,
                            email = email,
                            telefone = telefone,
                            genero = generoSelecionado,
                            dataNascimento = nascimento,
                            senha = senha,
                            confirmarSenha = confirmarSenha
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Cadastrar")
                }
            }

            // Link para login
            TextButton(
                onClick = {
                    // TODO: Navegar para a tela de login
                    // navController.navigate("login_route")
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Já tem uma conta? Fazer login")
            }
        }
    }
}*/