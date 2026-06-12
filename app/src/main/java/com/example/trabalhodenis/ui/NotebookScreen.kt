package com.example.trabalhodenis.ui

import android.content.ContentValues
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.trabalhodenis.ui.theme.InputBackground
import com.example.trabalhodenis.ui.theme.NotebookRed
import com.example.trabalhodenis.ui.theme.NotebookTeal
import com.example.trabalhodenis.ui.theme.NotebookDarkBlue
import com.example.trabalhodenis.ui.theme.PrimaryBlack
import com.example.trabalhodenis.ui.theme.SecondaryGray

@Composable
fun NotebooksScreen(
    notebooks: List<Notebook>,
    notes: List<Note>,
    isDarkMode: Boolean = false,
    onAddNotebook: (String, Color) -> Unit,
    onAddNote: (Int, String, String, String?) -> Unit,
    onDeleteNote: (Int) -> Unit,
    onDeleteNotebook: (Int) -> Unit,
) {
    var selectedNotebookId by remember { mutableStateOf<Int?>(notebooks.firstOrNull()?.id) }
    var showOptionsDialog by remember { mutableStateOf(false) }
    var showNewNotebookDialog by remember { mutableStateOf(false) }
    var showNewNoteDialog by remember { mutableStateOf(false) }
    var viewingNote by remember { mutableStateOf<Note?>(null) }

    val filteredNotes = notes.filter { it.notebookId == selectedNotebookId }
    val selectedNotebook = notebooks.find { it.id == selectedNotebookId }
    
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    val secondaryTextColor = if (isDarkMode) Color.Gray else SecondaryGray

    if (viewingNote != null) {
        NoteViewScreen(
            note = viewingNote!!,
            color = notebooks.find { it.id == viewingNote!!.notebookId }?.color ?: PrimaryBlack,
            isDarkMode = isDarkMode,
            onBack = { viewingNote = null }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    "Meus Cadernos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                    color = textColor
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    notebooks.forEach { notebook ->
                        val isSelected = selectedNotebookId == notebook.id
                        NotebookChip(
                            notebook = notebook,
                            isSelected = isSelected,
                            isDarkMode = isDarkMode,
                            onClick = { selectedNotebookId = notebook.id }
                        ) { onDeleteNotebook(notebook.id) }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (selectedNotebook != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Notas em ${selectedNotebook.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
                    }

                    if (filteredNotes.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null, modifier = Modifier.size(64.dp), tint = if (isDarkMode) Color.DarkGray else Color.LightGray)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Não há nenhuma nota neste caderno.", color = secondaryTextColor, fontSize = 14.sp)
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredNotes) { note ->
                                NoteItem(
                                    note = note,
                                    color = selectedNotebook.color,
                                    isDarkMode = isDarkMode,
                                    onClick = { viewingNote = note },
                                    onDelete = { onDeleteNote(note.id) }
                                )
                            }
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text("Crie ou selecione um caderno para ver as notas.", color = secondaryTextColor)
                    }
                }
            }

            FloatingActionButton(
                onClick = { showOptionsDialog = true },
                modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
                containerColor = if (isDarkMode) Color.White else PrimaryBlack,
                contentColor = if (isDarkMode) PrimaryBlack else Color.White
            ) { Icon(Icons.Default.Add, contentDescription = "Adicionar") }
        }
    }

    if (showOptionsDialog) {
        AlertDialog(
            onDismissRequest = { showOptionsDialog = false },
            title = { Text("O que deseja criar?", fontWeight = FontWeight.Bold, color = textColor) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { showOptionsDialog = false; showNewNoteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color.White else PrimaryBlack, contentColor = if (isDarkMode) PrimaryBlack else Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.NoteAdd, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Nova Nota")
                    }
                    Button(
                        onClick = { showOptionsDialog = false; showNewNotebookDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color(0xFF333333) else Color.White, contentColor = if (isDarkMode) Color.White else PrimaryBlack),
                        border = BorderStroke(1.dp, if (isDarkMode) Color.White else PrimaryBlack),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Novo Caderno")
                    }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { showOptionsDialog = false }) { Text("Cancelar", color = secondaryTextColor) } },
            containerColor = if (isDarkMode) Color(0xFF222222) else Color.White
        )
    }

    if (showNewNotebookDialog) {
        NewNotebookDialog(isDarkMode = isDarkMode, onDismiss = { showNewNotebookDialog = false }, onConfirm = { name, color -> onAddNotebook(name, color); showNewNotebookDialog = false })
    }

    if (showNewNoteDialog) {
        NewNoteDialog(notebooks = notebooks, initialNotebookId = selectedNotebookId, isDarkMode = isDarkMode, onDismiss = { showNewNoteDialog = false }, onConfirm = { notebookId, title, content, imageUri -> onAddNote(notebookId, title, content, imageUri); selectedNotebookId = notebookId; showNewNoteDialog = false })
    }
}

@Composable
fun NoteViewScreen(note: Note, color: Color, isDarkMode: Boolean, onBack: () -> Unit) {
    val context = LocalContext.current
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    val secondaryTextColor = if (isDarkMode) Color.Gray else SecondaryGray

    Column(modifier = Modifier.fillMaxSize().background(if (isDarkMode) Color(0xFF121212) else Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Voltar", tint = textColor) }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = note.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = note.date, fontSize = 12.sp, color = secondaryTextColor)
                    }
                }
            }
            IconButton(onClick = { downloadNoteAsPdf(context, note) }) { Icon(Icons.Outlined.Download, contentDescription = "Baixar PDF", tint = textColor) }
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(if (isDarkMode) Color.DarkGray else Color(0xFFEEEEEE)))

        LazyColumn(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            if (note.imageUri != null) {
                item {
                    Image(
                        painter = rememberAsyncImagePainter(note.imageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            item {
                Text(text = note.content, fontSize = 16.sp, lineHeight = 24.sp, color = textColor)
            }
        }
    }
}

fun downloadNoteAsPdf(context: android.content.Context, note: Note) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas
    val paint = Paint()

    paint.textSize = 24f
    paint.isFakeBoldText = true
    canvas.drawText("MindFlow - Nota", 50f, 50f, paint)

    paint.textSize = 18f
    paint.isFakeBoldText = true
    canvas.drawText("Título: ${note.title}", 50f, 100f, paint)

    paint.isFakeBoldText = false
    paint.textSize = 12f
    canvas.drawText("Data: ${note.date}", 50f, 130f, paint)

    var y = 170f
    note.content.split("\n").forEach { line ->
        canvas.drawText(line, 50f, y, paint)
        y += 20f
    }

    pdfDocument.finishPage(page)

    val fileName = "${note.title.replace(" ", "_")}.pdf"
    try {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/MindFlow_Notas")
            }
        }
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Downloads.EXTERNAL_CONTENT_URI else MediaStore.Files.getContentUri("external")
        val uri = resolver.insert(collection, contentValues)
        if (uri != null) {
            resolver.openOutputStream(uri)?.use { pdfDocument.writeTo(it) }
            Toast.makeText(context, "PDF salvo em Downloads/MindFlow_Notas", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Erro ao baixar PDF", Toast.LENGTH_SHORT).show()
    } finally {
        pdfDocument.close()
    }
}

@Composable
fun NotebookChip(notebook: Notebook, isSelected: Boolean, isDarkMode: Boolean, onClick: () -> Unit, onDelete: () -> Unit) {
    val cardBgColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    val borderColor = if (isDarkMode) Color.DarkGray else Color(0xFFEEEEEE)
    Card(
        modifier = Modifier.width(140.dp).height(100.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) notebook.color.copy(alpha = if (isDarkMode) 0.4f else 0.2f) else cardBgColor),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) notebook.color else borderColor)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.size(16.dp).background(notebook.color, CircleShape))
                IconButton(onClick = onDelete, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFE57373), modifier = Modifier.size(16.dp)) }
            }
            Text(notebook.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1, color = textColor)
        }
    }
}

@Composable
fun NoteItem(note: Note, color: Color, isDarkMode: Boolean, onClick: () -> Unit, onDelete: () -> Unit) {
    val cardBgColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    val secondaryTextColor = if (isDarkMode) Color.Gray else SecondaryGray
    val borderColor = if (isDarkMode) Color.DarkGray else Color(0xFFEEEEEE)
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(note.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) { Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFE57373), modifier = Modifier.size(20.dp)) }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (note.imageUri != null) {
                Image(painter = rememberAsyncImagePainter(note.imageUri), contentDescription = null, modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(note.content, fontSize = 14.sp, color = secondaryTextColor, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
                Spacer(modifier = Modifier.width(8.dp))
                Text(note.date, fontSize = 12.sp, color = if (isDarkMode) Color.Gray else Color.LightGray)
            }
        }
    }
}

@Composable
fun NewNotebookDialog(isDarkMode: Boolean, onDismiss: () -> Unit, onConfirm: (String, Color) -> Unit) {
    var name by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(NotebookRed) }
    val colors = listOf(NotebookRed, NotebookTeal, NotebookDarkBlue, Color(0xFF9C27B0), Color(0xFFFF9800))
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Caderno", fontWeight = FontWeight.Bold, color = textColor) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Nome do Caderno", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                TextField(value = name, onValueChange = { name = it }, placeholder = { Text("Ex: Biologia") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground, unfocusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = textColor, unfocusedTextColor = textColor), shape = RoundedCornerShape(12.dp))
                Text("Escolha uma cor", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    colors.forEach { color -> Box(modifier = Modifier.size(32.dp).background(color, CircleShape).border(width = if (selectedColor == color) 2.dp else 0.dp, color = if (selectedColor == color) (if (isDarkMode) Color.White else PrimaryBlack) else Color.Transparent, shape = CircleShape).clickable { selectedColor = color }) }
                }
            }
        },
        confirmButton = { Button(onClick = { if (name.isNotBlank()) onConfirm(name, selectedColor) }, colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color.White else PrimaryBlack, contentColor = if (isDarkMode) PrimaryBlack else Color.White)) { Text("Criar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = if (isDarkMode) Color.Gray else SecondaryGray) } },
        containerColor = if (isDarkMode) Color(0xFF222222) else Color.White
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteDialog(notebooks: List<Notebook>, initialNotebookId: Int?, isDarkMode: Boolean, onDismiss: () -> Unit, onConfirm: (Int, String, String, String?) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedNotebookId by remember { mutableIntStateOf(initialNotebookId ?: notebooks.firstOrNull()?.id ?: 0) }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> selectedImageUri = uri?.toString() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Nota", fontWeight = FontWeight.Bold, color = textColor) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    val selectedNotebook = notebooks.find { it.id == selectedNotebookId }
                    OutlinedTextField(value = selectedNotebook?.name ?: "Selecione", onValueChange = {}, readOnly = true, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textColor, unfocusedTextColor = textColor, focusedBorderColor = if (isDarkMode) Color.White else PrimaryBlack, unfocusedBorderColor = if (isDarkMode) Color.Gray else Color.LightGray))
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, containerColor = if (isDarkMode) Color(0xFF333333) else Color.White) {
                        notebooks.forEach { notebook -> DropdownMenuItem(text = { Text(notebook.name, color = textColor) }, onClick = { selectedNotebookId = notebook.id; expanded = false }) }
                    }
                }
                TextField(value = title, onValueChange = { title = it }, placeholder = { Text("Título da nota") }, modifier = Modifier.fillMaxWidth(), colors = TextFieldDefaults.colors(focusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground, unfocusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = textColor, unfocusedTextColor = textColor), shape = RoundedCornerShape(12.dp))
                TextField(value = content, onValueChange = { content = it }, placeholder = { Text("Escreva aqui...") }, modifier = Modifier.fillMaxWidth().height(100.dp), colors = TextFieldDefaults.colors(focusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground, unfocusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent, focusedTextColor = textColor, unfocusedTextColor = textColor), shape = RoundedCornerShape(12.dp))
                
                Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color(0xFF444444) else Color(0xFFEEEEEE), contentColor = textColor)) {
                    Icon(Icons.Outlined.Image, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (selectedImageUri == null) "Adicionar Imagem" else "Imagem Selecionada")
                }
            }
        },
        confirmButton = { Button(onClick = { if (title.isNotBlank() && content.isNotBlank()) onConfirm(selectedNotebookId, title, content, selectedImageUri) }, colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color.White else PrimaryBlack, contentColor = if (isDarkMode) PrimaryBlack else Color.White)) { Text("Adicionar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = if (isDarkMode) Color.Gray else SecondaryGray) } },
        containerColor = if (isDarkMode) Color(0xFF222222) else Color.White
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotebooksScreenPreview() {
    NotebooksScreen(notebooks = listOf(Notebook(1, "Matemática", NotebookRed)), notes = emptyList(), isDarkMode = false, onAddNotebook = { _, _ -> }, onAddNote = { _, _, _, _ -> }, onDeleteNote = { _ -> }, onDeleteNotebook = { _ -> })
}
