package com.example.trabalhodenis.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.trabalhodenis.R
import com.example.trabalhodenis.ui.theme.InputBackground
import com.example.trabalhodenis.ui.theme.PrimaryBlack
import com.example.trabalhodenis.ui.theme.SecondaryGray
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }


    val webClientId = "1072037048343-ir0piiv8kl014bm6sc2ncuor1mcot544.apps.googleusercontent.com"

    // Tentativa de Auto-Login ao abrir a tela
    LaunchedEffect(Unit) {
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(true) // Crucial para entrar sem clicar
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                onLoginSuccess()
            }
        } catch (e: Exception) {
            // Falha silenciosa no auto-login (usuário precisa clicar no botão)
            Log.d("OAuth", "Auto-login não disponível: ${e.message}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_mindflow),
                contentDescription = "Logo MindFlow",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "MindFlow", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryBlack)
        Text(text = "organize seus estudos", fontSize = 14.sp, color = SecondaryGray)

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(InputBackground)
                        .padding(4.dp)
                ) {
                    Button(
                        onClick = { isLoginMode = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLoginMode) PrimaryBlack else Color.Transparent,
                            contentColor = if (isLoginMode) Color.White else SecondaryGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = null
                    ) { Text(text = "Entrar") }
                    
                    Button(
                        onClick = { isLoginMode = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isLoginMode) PrimaryBlack else Color.Transparent,
                            contentColor = if (!isLoginMode) Color.White else SecondaryGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = null
                    ) { Text(text = "Criar Conta") }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (!isLoginMode) {
                    Text(text = "Nome Completo", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlack)
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text(text = "Seu nome", color = SecondaryGray) },
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                        colors = TextFieldDefaults.colors(focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(text = "Email", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlack)
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(text = "seu@email.com", color = SecondaryGray) },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Senha", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = PrimaryBlack)
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(text = ".........", color = SecondaryGray) },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Outlined.Visibility, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp)
                )



                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onLoginSuccess,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlack),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = if (isLoginMode) "Entrar" else "Cadastrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                // Botão do Google (OAuth)
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            try {
                                val googleIdOption = GetGoogleIdOption.Builder()
                                    .setFilterByAuthorizedAccounts(false)
                                    .setServerClientId(webClientId)
                                    .setAutoSelectEnabled(true) // Ativa o login automático na próxima vez
                                    .build()

                                val request = GetCredentialRequest.Builder()
                                    .addCredentialOption(googleIdOption)
                                    .build()

                                val result = credentialManager.getCredential(context, request)
                                val credential = result.credential

                                // Verificação mais robusta do tipo de credencial
                                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                                    Log.d("OAuth", "Login com sucesso: ${googleIdTokenCredential.displayName}")
                                    onLoginSuccess() // Entra no app imediatamente
                                }
                            } catch (e: Exception) {
                                Log.e("OAuth", "Erro detalhado: ${e.message}", e)
                                val errorMsg = when {
                                    e.message?.contains("7") == true -> "Erro 7: Google Play Services desatualizado ou SHA-1 não cadastrado."
                                    e.message?.contains("10") == true -> "Erro 10: Client ID incorreto ou erro de configuração no console."
                                    e.message?.contains("16") == true -> "Erro 16: Autenticação cancelada pelo usuário ou falha de rede."
                                    else -> "Falha: ${e.localizedMessage}"
                                }
                                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Continuar com Google", color = Color(0xFF1F1F1F), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.clickable { isLoginMode = !isLoginMode }) {
            Text(text = if (isLoginMode) "Novo por aqui? " else "Já tem uma conta? ", color = SecondaryGray)
            Text(text = if (isLoginMode) "Criar conta" else "Entrar", fontWeight = FontWeight.Bold, color = PrimaryBlack)
        }
    }
}
