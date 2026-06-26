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
        SlideEntity::class,
        UserEntity::class
    ],
    version = 2, // Incrementei a versão para 2 pois adicionei uma nova tabela
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun notebookDao(): NotebookDao
    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao
    abstract fun calendarDao(): CalendarDao
    abstract fun presentationDao(): PresentationDao
    abstract fun slideDao(): SlideDao
    abstract fun userDao(): UserDao
}
