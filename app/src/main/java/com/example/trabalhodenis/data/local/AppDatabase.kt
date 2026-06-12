package com.example.trabalhodenis.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trabalhodenis.data.local.dao.*
import com.example.trabalhodenis.data.local.entity.*

@Database(
    entities = [
        NotebookEntity::class,
        NoteEntity::class,
        TaskEntity::class,
        CalendarEntity::class,
        PresentationEntity::class,
        SlideEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // NOTEBOOKS
    abstract fun notebookDao(): NotebookDao

    // NOTES
    abstract fun noteDao(): NoteDao

    // TASKS
    abstract fun taskDao(): TaskDao

    // CALENDAR EVENTS
    abstract fun calendarDao(): CalendarDao

    // PRESENTATIONS
    abstract fun presentationDao(): PresentationDao

    // SLIDES
    abstract fun slideDao(): SlideDao
}
