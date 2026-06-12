package com.example.trabalhodenis.data.mapper

import androidx.compose.ui.graphics.Color
import com.example.trabalhodenis.data.local.entity.*
import com.example.trabalhodenis.ui.*

fun TaskEntity.toTask() = Task(
    id,
    title,
    subject,
    priority,
    isCompleted
)

fun Task.toEntity() = TaskEntity(
    id,
    title,
    subject,
    priority,
    isCompleted
)

fun CalendarEntity.toCalendarEvent() = CalendarEvent(
    id,
    title,
    subject,
    day,
    month,
    year,
    time,
    type
)

fun CalendarEvent.toEntity() = CalendarEntity(
    id,
    title,
    subject,
    day,
    month,
    year,
    time,
    type
)

fun NotebookEntity.toNotebook() = Notebook(
    id,
    name,
    Color(colorValue.toULong())
)

fun Notebook.toEntity() = NotebookEntity(
    id,
    name,
    color.value.toLong()
)

fun NoteEntity.toNote() = Note(
    id,
    notebookId,
    title,
    content,
    date,
    imageUri
)

fun Note.toEntity() = NoteEntity(
    id,
    notebookId,
    title,
    content,
    date,
    imageUri
)

fun PresentationEntity.toPresentation() = Presentation(
    id,
    title,
    subject,
    dueDate,
    progress,
    steps,
    fileName,
    fileUri
)

fun Presentation.toEntity() = PresentationEntity(
    id,
    title,
    subject,
    dueDate,
    progress,
    steps,
    fileName,
    fileUri
)

fun SlideEntity.toSlide() = Slide(
    id,
    title,
    subject,
    dueDate,
    fileName,
    fileUri
)

fun Slide.toEntity() = SlideEntity(
    id,
    title,
    subject,
    dueDate,
    fileName,
    fileUri
)
