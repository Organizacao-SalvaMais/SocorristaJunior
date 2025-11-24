package com.example.socorristajunior.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

/**
 * Um botão reutilizável que encapsula toda a lógica de UI
 * para o fluxo de login social do Google.
 *
 * @param webClientId O Web Client ID (Tipo 3) do seu console Firebase/Google.
 * @param onTokenReceived Lambda chamada em caso de SUCESSO. Retorna a [GoogleSignInAccount].
 * @param onSignInFailed Lambda chamada em caso de FALHA. Retorna uma [String] com a mensagem de erro.
 */
@Composable
fun GoogleSignInButton(
    webClientId: String,
    onTokenReceived: (GoogleSignInAccount) -> Unit,
    onSignInFailed: (String) -> Unit
) {
    // Pega o contexto atual, necessário para o GoogleSignInClient
    val context = LocalContext.current

    // 1. Cria o "ouvinte" que espera a resposta da Activity do Google
    val googleSignInLauncher = rememberLauncherForActivityResult(
        // Define o contrato: "Vou iniciar uma activity e quero um resultado"
        contract = ActivityResultContracts.StartActivityForResult(),
        // Define o que fazer quando o resultado chegar
        onResult = { result ->
            // Pega a intenção (Intent) do resultado
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Tenta pegar a conta do Google a partir da tarefa
                val account = task.getResult(ApiException::class.java)

                // Se a conta não for nula (SUCESSO)
                if (account != null) {
                    // 2. Chama a lambda de SUCESSO, devolvendo a conta
                    onTokenReceived(account)
                } else {
                    // Se a conta for nula (FALHA)
                    onSignInFailed("Erro: A conta do Google retornou nula.")
                }
            } catch (e: ApiException) {
                // 3. Se houver uma exceção (FALHA, ex: usuário cancelou)
                // Chama a lambda de FALHA, devolvendo a mensagem de erro
                onSignInFailed("Falha no login com Google: ${e.statusCode}")
            }
        }
    )

    // 3. Cria o cliente de Login do Google
    // 'remember' garante que o cliente não seja recriado em cada recomposição
    val googleSignInClient = remember(webClientId, context) {
        // Configura as opções de login
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            // Pede o idToken usando o ID do seu app (O MAIS IMPORTANTE)
            .requestIdToken(webClientId)
            // Pede o email do usuário
            .requestEmail()
            .build()

        // Cria o cliente usando o contexto e as opções
        GoogleSignIn.getClient(context, gso)
    }

    // 4. O Composable do Botão (a UI)
    OutlinedButton(
        onClick = {
            // Ação de clique: Pede ao 'ouvinte' para INICIAR a activity de login
            // O 'ouvinte' usará a 'signInIntent' do cliente para saber qual activity chamar
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        // O texto do botão
        Text("ENTRAR COM GOOGLE")
        // Você pode adicionar o ícone do Google aqui se quiser
    }
}