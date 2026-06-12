package com.example.trabalhodenis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "slides")
data class SlideEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val subject: String,
    val dueDate: String,
    val fileName: String? = null,
    val fileUri: String? = null
)