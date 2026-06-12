package com.example.trabalhodenis.ui

import androidx.compose.ui.graphics.Color

// --- Data Models ---
data class Task(
    val id: Int,
    val title: String,
    val subject: String,
    val priority: String,
    val isCompleted: Boolean
)

data class CalendarEvent(
    val id: Int,
    val title: String,
    val subject: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val time: String,
    val type: String
)

data class Notebook(
    val id: Int,
    val name: String,
    val color: Color
)

data class Note(
    val id: Int,
    val notebookId: Int,
    val title: String,
    val content: String,
    val date: String,
    val imageUri: String? = null
)

data class Presentation(
    val id: Int,
    val title: String,
    val subject: String,
    val dueDate: String,
    val progress: Float,
    val steps: String,
    val fileName: String? = null,
    val fileUri: String? = null
)

data class Slide(
    val id: Int,
    val title: String,
    val subject: String,
    val dueDate: String,
    val fileName: String? = null,
    val fileUri: String? = null
)

// --- Navigation ---
enum class AppScreen {
    Login, Calendar, Tasks, Notebooks, Slides
}
