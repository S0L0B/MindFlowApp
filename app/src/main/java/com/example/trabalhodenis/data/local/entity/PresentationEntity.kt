package com.example.trabalhodenis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "presentations")
data class PresentationEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val subject: String,

    val dueDate: String,

    val progress: Float,

    val steps: String,

    val fileName: String?,

    val fileUri: String?
)