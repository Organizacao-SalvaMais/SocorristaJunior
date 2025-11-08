package com.example.socorristajunior.ui.screens.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.socorristajunior.ui.components.BottomNavigationBar
import kotlinx.coroutines.launch


// Define as rotas do aplicativo.
const val LOGIN_ROUTE = "login"
const val MAIN_SCREEN_ROUTE = "home" // Tela principal após o login

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    // Coleta o estado da UI (derivado do Flow do Room)
    val state by viewModel.uiState.collectAsState()

    // Variáveis de estado para os campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estado para o Snackbar (mensagens de erro)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- INÍCIO DA LÓGICA DO GOOGLE ---

    // 1. Crie o "ouvinte" que espera a resposta do Google
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            // A tela do Google respondeu. Vamos ver o que ela disse.
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Sucesso! Pegamos a conta do Google
                val account = task.getResult(ApiException::class.java)

                // 2. ENVIE O RESULTADO PARA O VIEWMODEL
                if (account != null) {
                    viewModel.handleGoogleSignIn(account)
                }
            } catch (e: ApiException) {
                // Falha no login (usuário cancelou, erro de rede, etc.)
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Falha no login com Google: ${e.statusCode}",
                    )
                }
            }
        }
    )

    // 3. Crie o cliente de Login do Google
    val googleSignInClient = remember {
        // Você PRECISA configurar isso no Firebase e no google-services.json
        // Este ID é o "Web client ID" (tipo 3) que fica no seu console do Firebase.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("426536441799-aha830v7o118g7bqheg1s1el8ngesn93.apps.googleusercontent.com") // <-- MUITO IMPORTANTE
            .requestEmail()
            .build()
        GoogleSignIn.getClient(navController.context, gso)
    }

    // --- FIM DA LÓGICA DO GOOGLE ---

    // 1. Lógica de Navegação em caso de SUCESSO
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            // Navega para a tela principal e limpa a pilha de navegação
            navController.navigate(MAIN_SCREEN_ROUTE) {
                popUpTo(LOGIN_ROUTE) { inclusive = true }
            }
        }
    }

    // 2. Lógica de Mensagem de Erro
    LaunchedEffect(state.errorMessage) {
        val currentError = state.errorMessage
        if (state.errorMessage != null) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = currentError!!,
                    actionLabel = "Tentar novamente"
                )
                // Limpa o erro após mostrar para que ele não reapareça
                viewModel.clearError()
            }
        }
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Lógica para focar no campo de email ao carregar a tela
    LaunchedEffect(Unit) {
        // Garantimos que a tela está pronta antes de solicitar o foco
        // Um pequeno delay ajuda em alguns dispositivos mais lentos
        kotlinx.coroutines.delay(100)
        focusRequester.requestFocus()
        keyboardController?.show() // Força a exibição do teclado
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar para Home")
                    }
                }
            )
        },
        bottomBar = {
            // Função da NavBar | | Label
            BottomNavigationBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bem-vindo ao Salvar+",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    // ⭐️ AQUI É A CHAVE: Aplica o FocusRequester
                    .focusRequester(focusRequester)
            )
            // Campo de Senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Senha") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
            )

            // Botão de Login
            Button(
                onClick = {
                    // Chama a função signIn do ViewModel
                    viewModel.signIn(email, password)
                },
                enabled = !state.isLoading, // Desativa o botão enquanto carrega
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("ENTRAR")
                }
            }

            // --- Botão de LOGIN SOCIAL ---

            // Divisor visual
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Divider(modifier = Modifier.weight(1f))
                Text(
                    text = "OU",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Divider(modifier = Modifier.weight(1f))
            }

            // Botão de Login com Google
            OutlinedButton(
                onClick = {
                    // É ISSO QUE O BOTÃO FAZ:
                    // Ele não chama o ViewModel, ele CHAMA A TELA DO GOOGLE.
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                // Você pode adicionar um ícone do Google aqui depois
                // Icon(painter = painterResource(id = R.drawable.ic_google_logo), contentDescription = null)
                Text("ENTRAR COM GOOGLE")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- FIM DA ADIÇÃO ---

            Button(
                onClick = {
                    // É ASSIM QUE VOCÊ CHAMA A TELA:
                    navController.navigate("cadastro")
                }
            ) {
                Text("Criar nova conta")
            }
        }
    }
}
