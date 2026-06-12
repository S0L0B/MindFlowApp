package com.example.trabalhodenis.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trabalhodenis.data.database.DatabaseProvider
import com.example.trabalhodenis.data.local.repository.*
import com.example.trabalhodenis.ui.theme.BackgroundGray
import com.example.trabalhodenis.ui.theme.PrimaryBlack
import com.example.trabalhodenis.ui.theme.SecondaryGray
import kotlinx.coroutines.launch

private const val PREFS_NAME = "mindflow_prefs"
private const val KEY_DARK_MODE = "dark_mode"
private const val KEY_NOTIFICATIONS = "notifications"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    // Repositórios
    val db = remember { DatabaseProvider.getDatabase(context) }
    val taskRepo = remember { TaskRepository(db.taskDao()) }
    val calendarRepo = remember { CalendarRepository(db.calendarDao()) }
    val presentationRepo = remember { PresentationRepository(db.presentationDao()) }
    val notebookRepo = remember { NotebookRepository(db.notebookDao()) }
    val noteRepo = remember { NoteRepository(db.noteDao()) }

    // Estados de UI
    var currentScreen by remember { mutableStateOf(AppScreen.Login) }
    var isDarkMode by remember { mutableStateOf(prefs.getBoolean(KEY_DARK_MODE, false)) }
    var notificationsEnabled by remember { mutableStateOf(prefs.getBoolean(KEY_NOTIFICATIONS, true)) }
    var showSettings by remember { mutableStateOf(false) }

    // Dados observados do Banco de Dados
    val tasks by taskRepo.getAllTasks().collectAsState(initial = emptyList())
    val calendarEvents by calendarRepo.getAllEvents().collectAsState(initial = emptyList())
    val presentations by presentationRepo.getAllPresentations().collectAsState(initial = emptyList())
    val notebooks by notebookRepo.getAllNotebooks().collectAsState(initial = emptyList())
    val notes by noteRepo.getAllNotes().collectAsState(initial = emptyList())

    // Inicializar cadernos padrão se estiver vazio
    LaunchedEffect(notebooks) {
        if (notebooks.isEmpty()) {
            notebookRepo.addNotebook(Notebook(0, "Matemática", Color(0xFFE57373)))
            notebookRepo.addNotebook(Notebook(0, "Física", Color(0xFF4DB6AC)))
            notebookRepo.addNotebook(Notebook(0, "História", Color(0xFF283593)))
        }
    }

    if (currentScreen == AppScreen.Login) {
        LoginScreen { currentScreen = AppScreen.Tasks }
    } else if (showSettings) {
        SettingsScreen(
            isDarkMode = isDarkMode,
            onDarkModeChange = { 
                isDarkMode = it
                prefs.edit().putBoolean(KEY_DARK_MODE, it).apply()
            },
            notificationsEnabled = notificationsEnabled,
            onNotificationsEnabledChange = { 
                notificationsEnabled = it
                prefs.edit().putBoolean(KEY_NOTIFICATIONS, it).apply()
            },
            onBack = { showSettings = false }
        )
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("MindFlow", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    },
                    navigationIcon = {
                        IconButton(onClick = { showSettings = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "Configurações")
                        }
                    },
                    actions = {
                        IconButton(onClick = { currentScreen = AppScreen.Login }) {
                            Icon(Icons.AutoMirrored.Filled.Logout, modifier = Modifier.size(20.dp), contentDescription = "Sair")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (isDarkMode) Color(0xFF1A1A1A) else PrimaryBlack,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                BottomNavigationBar(currentScreen, isDarkMode) { currentScreen = it }
            }
        ) { innerPadding ->
            val bgColor = if (isDarkMode) Color(0xFF121212) else BackgroundGray
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(bgColor)) {
                when (currentScreen) {
                    AppScreen.Calendar -> CalendarScreen(
                        events = calendarEvents,
                        isDarkMode = isDarkMode,
                        onAddEvent = { title, subject, day, month, year, time, type ->
                            scope.launch {
                                calendarRepo.addEvent(CalendarEvent(0, title, subject, day, month, year, time, type))
                            }
                        },
                        onDeleteEvent = { eventId ->
                            scope.launch { calendarRepo.deleteEventById(eventId) }
                        }
                    )

                    AppScreen.Tasks -> TasksScreen(
                        tasks = tasks,
                        isDarkMode = isDarkMode,
                        onAddTask = { title, subject, priority ->
                            scope.launch {
                                taskRepo.addTask(Task(0, title, subject, priority, false))
                            }
                        },
                        onToggleTask = { taskId, checked ->
                            scope.launch { taskRepo.updateTaskStatus(taskId, checked) }
                        },
                        onDeleteTask = { taskId ->
                            scope.launch { taskRepo.deleteTaskById(taskId) }
                        }
                    )

                    AppScreen.Notebooks -> NotebooksScreen(
                        notebooks = notebooks,
                        notes = notes,
                        isDarkMode = isDarkMode,
                        onAddNotebook = { name, color ->
                            scope.launch { notebookRepo.addNotebook(Notebook(0, name, color)) }
                        },
                        onAddNote = { notebookId, title, content, imageUri ->
                            scope.launch {
                                noteRepo.addNote(Note(0, notebookId, title, content, "24/05/2026", imageUri))
                            }
                        },
                        onDeleteNote = { noteId ->
                            scope.launch {
                                notes.find { it.id == noteId }?.let { noteRepo.deleteNote(it) }
                            }
                        },
                        onDeleteNotebook = { notebookId ->
                            scope.launch {
                                notebooks.find { it.id == notebookId }?.let { notebookRepo.deleteNotebook(it) }
                            }
                        }
                    )

                    AppScreen.Slides -> SlidesScreen(
                        presentations = presentations,
                        isDarkMode = isDarkMode,
                        onAddPresentation = { title, subject, dueDate, fileName, fileUri ->
                            scope.launch {
                                presentationRepo.addPresentation(
                                    Presentation(0, title, subject, dueDate, 0f, "0/5", fileName, fileUri)
                                )
                            }
                        },
                        onDeletePresentation = { presentationId ->
                            scope.launch { presentationRepo.deletePresentationById(presentationId) }
                        }
                    )
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(currentScreen: AppScreen, isDarkMode: Boolean, onNavigate: (AppScreen) -> Unit) {
    NavigationBar(containerColor = if (isDarkMode) Color(0xFF1A1A1A) else Color.White) {
        val items = listOf(
            Triple("Calendário", Icons.Outlined.CalendarMonth, AppScreen.Calendar),
            Triple("Tarefas", Icons.Outlined.AssignmentTurnedIn, AppScreen.Tasks),
            Triple("Cadernos", Icons.AutoMirrored.Outlined.MenuBook, AppScreen.Notebooks),
            Triple("Slides", Icons.Outlined.Description, AppScreen.Slides),
        )
        items.forEach { (label, icon, screen) ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onNavigate(screen) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = if (isDarkMode) Color.White else PrimaryBlack,
                    selectedTextColor = if (isDarkMode) Color.White else PrimaryBlack,
                    unselectedIconColor = SecondaryGray,
                    unselectedTextColor = SecondaryGray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
