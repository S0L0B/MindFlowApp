package com.example.trabalhodenis.data.local.repository

import com.example.trabalhodenis.data.local.dao.NoteDao
import com.example.trabalhodenis.data.mapper.toEntity
import com.example.trabalhodenis.data.mapper.toNote
import com.example.trabalhodenis.ui.Note
import kotlinx.coroutines.flow.map

class NoteRepository(
    private val noteDao: NoteDao
) {

    fun getAllNotes() =
        noteDao.getAllNotes().map { list -> list.map { it.toNote() } }

    suspend fun addNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }
}
