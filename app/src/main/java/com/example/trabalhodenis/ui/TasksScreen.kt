package com.example.trabalhodenis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trabalhodenis.ui.theme.InputBackground
import com.example.trabalhodenis.ui.theme.PrimaryBlack
import com.example.trabalhodenis.ui.theme.PriorityHigh
import com.example.trabalhodenis.ui.theme.PriorityHighText
import com.example.trabalhodenis.ui.theme.PriorityLow
import com.example.trabalhodenis.ui.theme.PriorityLowText
import com.example.trabalhodenis.ui.theme.PriorityMedium
import com.example.trabalhodenis.ui.theme.PriorityMediumText
import com.example.trabalhodenis.ui.theme.SecondaryGray

@Composable
fun TasksScreen(
    tasks: List<Task>,
    isDarkMode: Boolean = false,
    onAddTask: (String, String, String) -> Unit,
    onToggleTask: (Int, Boolean) -> Unit,
    onDeleteTask: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    val secondaryTextColor = if (isDarkMode) Color.Gray else SecondaryGray

    val pendingTasks = tasks.filter { !it.isCompleted }
    val completedTasks = tasks.filter { it.isCompleted }

    if (showDialog) {
        NewTaskDialog(
            isDarkMode = isDarkMode,
            onDismiss = { showDialog = false },
            onAddTask = { title, subject, priority ->
                onAddTask(title, subject, priority)
                showDialog = false
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Lista de Tarefas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkMode) Color.White else PrimaryBlack,
                        contentColor = if (isDarkMode) PrimaryBlack else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text("Nova Tarefa", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Pendentes (${pendingTasks.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textColor
            )
        }

        if (pendingTasks.isEmpty()) {
            item {
                Text(
                    "Nenhuma tarefa pendente.",
                    modifier = Modifier.padding(top = 12.dp),
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )
            }
        }

        items(pendingTasks) { task ->
            TaskItem(
                task = task,
                isDarkMode = isDarkMode,
                onCheckedChange = { checked ->
                    onToggleTask(task.id, checked)
                },
                onDelete = {
                    onDeleteTask(task.id)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Concluídas (${completedTasks.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textColor
            )
        }

        if (completedTasks.isEmpty()) {
            item {
                Text(
                    "Nenhuma tarefa concluída.",
                    modifier = Modifier.padding(top = 12.dp),
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )
            }
        }

        items(completedTasks) { task ->
            TaskItem(
                task = task,
                isDarkMode = isDarkMode,
                onCheckedChange = { checked ->
                    onToggleTask(task.id, checked)
                },
                onDelete = {
                    onDeleteTask(task.id)
                }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    isDarkMode: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val textColor = if (isDarkMode) Color.White else PrimaryBlack
    val secondaryTextColor = if (isDarkMode) Color.LightGray else SecondaryGray
    val cardBgColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val borderColor = if (isDarkMode) Color.DarkGray else Color(0xFFEEEEEE)

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = if (isDarkMode) Color.White else SecondaryGray,
                    uncheckedColor = if (isDarkMode) Color.Gray else PrimaryBlack
                )
            )
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (bgColor, badgeTextColor) = when (task.priority) {
                        "Alta" -> (if (isDarkMode) Color(0xFF4D2C2C) else PriorityHigh) to (if (isDarkMode) Color.White else PriorityHighText)
                        "Média" -> (if (isDarkMode) Color(0xFF4D412C) else PriorityMedium) to (if (isDarkMode) Color.White else PriorityMediumText)
                        else -> (if (isDarkMode) Color(0xFF2C4D2E) else PriorityLow) to (if (isDarkMode) Color.White else PriorityLowText)
                    }
                    Badge(containerColor = bgColor, contentColor = badgeTextColor) {
                        Text(task.priority, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(task.subject, fontSize = 12.sp, color = secondaryTextColor)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    task.title,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    color = if (task.isCompleted) secondaryTextColor else textColor
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Excluir", tint = Color(0xFFE57373), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun NewTaskDialog(
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onAddTask: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Média") }
    val textColor = if (isDarkMode) Color.White else PrimaryBlack

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Tarefa", fontWeight = FontWeight.Bold, color = textColor) },
        text = {
            Column {
                Text("Título da tarefa", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Ex: Estudar capítulo 3") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Matéria", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                TextField(
                    value = subject,
                    onValueChange = { subject = it },
                    placeholder = { Text("Ex: Matemática") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF333333) else InputBackground,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Prioridade", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = textColor)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Baixa", "Média", "Alta").forEach { option ->
                        FilterChip(
                            selected = priority == option,
                            onClick = { priority = option },
                            label = { Text(option) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank() && subject.isNotBlank()) onAddTask(title.trim(), subject.trim(), priority) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkMode) Color.White else PrimaryBlack,
                    contentColor = if (isDarkMode) PrimaryBlack else Color.White
                )
            ) { Text("Adicionar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = if (isDarkMode) Color.Gray else SecondaryGray) }
        },
        containerColor = if (isDarkMode) Color(0xFF222222) else Color.White
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, showSystemUi = true)
@Composable
fun TasksScreenPreview() {
    TasksScreen(
        tasks = emptyList(),
        isDarkMode = false,
        onAddTask = { _, _, _ -> },
        onToggleTask = { _, _ -> },
        onDeleteTask = { _ -> }
    )
}
