package com.example.trabalhodenis.data.local.dao

import androidx.room.*
import com.example.trabalhodenis.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(
        note: NoteEntity
    )

    @Delete
    suspend fun deleteNote(
        note: NoteEntity
    )

    @Query("SELECT * FROM notes")
    fun getAllNotes():
            Flow<List<NoteEntity>>
}