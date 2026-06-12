package com.example.trabalhodenis.ui

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trabalhodenis.ui.theme.PrimaryBlack
import com.example.trabalhodenis.ui.theme.SecondaryGray

@Composable
fun SlidesScreen(
    presentations: List<Presentation>,
    isDarkMode: Boolean = false,
    onAddPresentation: (String, String, String, String?, String?) -> Unit,
    onDeletePresentation: (Int) -> Unit,
) {
    var showCreateScreen by remember { mutableStateOf(false) }
    var selectedPresentationId by remember { mutableStateOf<Int?>(null) }
    var currentSlidePage by remember { mutableIntStateOf(0) }

    val selectedPresentation = presentations.find { it.id == selectedPresentationId }
    val context = LocalContext.current
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    val secondaryTextColor = if (isDarkMode) Color.Gray else SecondaryGray

    if (showCreateScreen) {
        CreateSlideScreen(
            isDarkMode = isDarkMode,
            onBack = { showCreateScreen = false },
            onConfirm = { titulo, materia, entrega, nome, uri ->
                onAddPresentation(titulo, materia, entrega, nome, uri)
                showCreateScreen = false
            }
        )
    } else {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Lista Horizontal de Apresentações
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                presentations.forEach { presentation ->
                    val isSelected = selectedPresentationId == presentation.id
                    Card(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .width(180.dp)
                            .clickable {
                                selectedPresentationId = presentation.id
                                currentSlidePage = 0
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) (if (isDarkMode) Color(0xFF333333) else Color(0xFFE0E0E0)) else (if (isDarkMode) Color(0xFF1E1E1E) else Color.White)
                        ),
                        border = BorderStroke(1.dp, if (isSelected) textColor else (if (isDarkMode) Color.DarkGray else Color(0xFFEEEEEE)))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(presentation.title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), maxLines = 1, color = textColor)
                                IconButton(onClick = {
                                    if (isSelected) selectedPresentationId = null
                                    onDeletePresentation(presentation.id)
                                }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE57373), modifier = Modifier.size(16.dp))
                                }
                            }
                            Text(presentation.subject, color = secondaryTextColor, fontSize = 12.sp)
                            Text("Entrega: ${presentation.dueDate}", fontSize = 12.sp, color = textColor.copy(alpha = 0.7f))
                        }
                    }
                }

                FloatingActionButton(
                    onClick = { showCreateScreen = true },
                    containerColor = if (isDarkMode) Color.White else PrimaryBlack,
                    contentColor = if (isDarkMode) PrimaryBlack else Color.White
                ) { Icon(Icons.Default.Add, contentDescription = null) }
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (selectedPresentation != null) {
                // Visualizador de Slides
                Text("Visualizando: ${selectedPresentation.title}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
                Spacer(modifier = Modifier.height(16.dp))

                var pageCount by remember { mutableIntStateOf(1) }
                var bitmap by remember { mutableStateOf<Bitmap?>(null) }

                // Lógica de Renderização de PDF
                LaunchedEffect(selectedPresentation, currentSlidePage) {
                    if (selectedPresentation.fileUri != null && selectedPresentation.fileName?.endsWith(".pdf", true) == true) {
                        try {
                            val uri = Uri.parse(selectedPresentation.fileUri)
                            context.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
                                val renderer = PdfRenderer(descriptor)
                                pageCount = renderer.pageCount
                                val page = renderer.openPage(currentSlidePage.coerceIn(0, pageCount - 1))
                                val newBitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                                page.render(newBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                                bitmap = newBitmap
                                page.close()
                                renderer.close()
                            }
                        } catch (e: Exception) {
                            bitmap = null
                        }
                    } else {
                        bitmap = null
                        pageCount = 5 // Mock para outros tipos
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White),
                    border = BorderStroke(1.dp, if (isDarkMode) Color.DarkGray else Color(0xFFEEEEEE))
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (bitmap != null) {
                            Image(bitmap!!.asImageBitmap(), contentDescription = "Slide Content", modifier = Modifier.fillMaxSize().padding(8.dp))
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Outlined.Description, contentDescription = null, modifier = Modifier.size(64.dp), tint = if (isDarkMode) Color.Gray else Color.LightGray)
                                Text(selectedPresentation.fileName ?: "Sem arquivo", color = secondaryTextColor)
                                Text("Página ${currentSlidePage + 1}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { if (currentSlidePage > 0) currentSlidePage-- },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color.White else PrimaryBlack, contentColor = if (isDarkMode) PrimaryBlack else Color.White)
                    ) { Text("Voltar") }
                    Text("Página ${currentSlidePage + 1}/$pageCount", fontWeight = FontWeight.Bold, color = textColor)
                    Button(
                        onClick = { if (currentSlidePage < pageCount - 1) currentSlidePage++ },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color.White else PrimaryBlack, contentColor = if (isDarkMode) PrimaryBlack else Color.White)
                    ) { Text("Avançar") }
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Description, contentDescription = null, modifier = Modifier.size(80.dp), tint = if (isDarkMode) Color.DarkGray else Color.LightGray)
                        Text("Selecione um slide para visualizar", color = secondaryTextColor)
                    }
                }
            }
        }
    }
}

@Composable
fun CreateSlideScreen(
    isDarkMode: Boolean,
    onBack: () -> Unit,
    onConfirm: (String, String, String, String?, String?) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var entrega by remember { mutableStateOf("") }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var selectedFileUri by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val textColor = if (isDarkMode) Color.White else PrimaryBlack

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedFileUri = it.toString()
            val cursor = context.contentResolver.query(it, null, null, null, null)
            cursor?.use { c ->
                val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (c.moveToFirst()) selectedFileName = c.getString(nameIndex)
            }
            if (selectedFileName == null) selectedFileName = "arquivo_selecionado"
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).background(if (isDarkMode) Color(0xFF121212) else Color.White)) {
        Text("Novo Slide", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = textColor)
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)),
            border = if (isDarkMode) BorderStroke(0.5.dp, Color.DarkGray) else null
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(selectedFileName ?: "Faça upload do arquivo (PDF recomendado)", fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = textColor)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { launcher.launch("*/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color.White else PrimaryBlack, contentColor = if (isDarkMode) PrimaryBlack else Color.White)
                ) { Text("Selecionar arquivo") }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = if (isDarkMode) Color.White else PrimaryBlack,
                unfocusedBorderColor = if (isDarkMode) Color.Gray else Color.LightGray
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = materia,
            onValueChange = { materia = it },
            label = { Text("Matéria") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = if (isDarkMode) Color.White else PrimaryBlack,
                unfocusedBorderColor = if (isDarkMode) Color.Gray else Color.LightGray
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = entrega,
            onValueChange = { entrega = it },
            label = { Text("Entrega") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedBorderColor = if (isDarkMode) Color.White else PrimaryBlack,
                unfocusedBorderColor = if (isDarkMode) Color.Gray else Color.LightGray
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { if (titulo.isNotBlank()) onConfirm(titulo, materia, entrega, selectedFileName, selectedFileUri) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color.White else PrimaryBlack, contentColor = if (isDarkMode) PrimaryBlack else Color.White)
        ) { Text("Cadastrar Slide") }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = if (isDarkMode) Color.White else PrimaryBlack)
        ) { Text("Voltar") }
    }
}
