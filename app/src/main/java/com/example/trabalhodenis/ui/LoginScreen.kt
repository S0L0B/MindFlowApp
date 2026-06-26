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
import com.example.trabalhodenis.data.local.repository.UserRepository
import com.example.trabalhodenis.ui.theme.InputBackground
import com.example.trabalhodenis.ui.theme.PrimaryBlack
import com.example.trabalhodenis.ui.theme.SecondaryGray
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    userRepository: UserRepository,
    onLoginSuccess: () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }

    val webClientId = "1072037048343-ir0piiv8kl014bm6sc2ncuor1mcot544.apps.googleusercontent.com"

    LaunchedEffect(Unit) {
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                onLoginSuccess()
            }
        } catch (e: Exception) {
            Log.d("OAuth", "Auto-login não disponível: ${e.message}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = R.drawable.logo_mindflow), contentDescription = "Logo MindFlow", modifier = Modifier.fillMaxWidth())
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
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(InputBackground).padding(4.dp)
                ) {
                    Button(
                        onClick = { isLoginMode = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isLoginMode) PrimaryBlack else Color.Transparent, contentColor = if (isLoginMode) Color.White else SecondaryGray),
                        shape = RoundedCornerShape(8.dp),
                        elevation = null
                    ) { Text(text = "Entrar") }
                    
                    Button(
                        onClick = { isLoginMode = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = if (!isLoginMode) PrimaryBlack else Color.Transparent, contentColor = if (!isLoginMode) Color.White else SecondaryGray),
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
                        placeholder = { Text(text = "Seu nome") },
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
                    placeholder = { Text(text = "seu@email.com") },
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
                    placeholder = { Text(text = ".........") },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                    trailingIcon = { Icon(Icons.Outlined.Visibility, contentDescription = null) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(focusedContainerColor = InputBackground, unfocusedContainerColor = InputBackground, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        
                        scope.launch {
                            if (isLoginMode) {
                                val success = userRepository.login(email, password)
                                if (success) onLoginSuccess()
                                else Toast.makeText(context, "Email ou senha incorretos", Toast.LENGTH_SHORT).show()
                            } else {
                                if (name.isBlank()) {
                                    Toast.makeText(context, "Preencha seu nome", Toast.LENGTH_SHORT).show()
                                    return@launch
                                }
                                val success = userRepository.register(name, email, password)
                                if (success) {
                                    Toast.makeText(context, "Conta criada! Faça o login.", Toast.LENGTH_SHORT).show()
                                    isLoginMode = true
                                } else {
                                    Toast.makeText(context, "Email já cadastrado", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlack),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = if (isLoginMode) "Entrar" else "Cadastrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            try {
                                val googleIdOption = GetGoogleIdOption.Builder().setFilterByAuthorizedAccounts(false).setServerClientId(webClientId).setAutoSelectEnabled(true).build()
                                val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
                                val result = credentialManager.getCredential(context, request)
                                if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                    onLoginSuccess()
                                }
                            } catch (e: Exception) {
                                Log.e("OAuth", "Erro Google: ${e.message}")
                                Toast.makeText(context, "Falha na autenticação Google", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Image(painter = painterResource(id = R.drawable.ic_google_logo), contentDescription = null, modifier = Modifier.size(18.dp))
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
