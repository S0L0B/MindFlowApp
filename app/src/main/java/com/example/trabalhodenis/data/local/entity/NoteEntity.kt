package com.example.trabalhodenis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val notebookId: Int,

    val title: String,

    val content: String,

    val date: String,

    val imageUri: String?
)