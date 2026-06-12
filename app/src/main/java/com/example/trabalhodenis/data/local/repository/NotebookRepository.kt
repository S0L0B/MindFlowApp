package com.example.trabalhodenis.data.local.repository

import com.example.trabalhodenis.data.local.dao.NotebookDao
import com.example.trabalhodenis.data.mapper.toEntity
import com.example.trabalhodenis.data.mapper.toNotebook
import com.example.trabalhodenis.ui.Notebook
import kotlinx.coroutines.flow.map

class NotebookRepository(
    private val notebookDao: NotebookDao
) {

    fun getAllNotebooks() =
        notebookDao.getAllNotebooks().map { list -> list.map { it.toNotebook() } }

    suspend fun addNotebook(notebook: Notebook) {
        notebookDao.insertNotebook(notebook.toEntity())
    }

    suspend fun deleteNotebook(notebook: Notebook) {
        notebookDao.deleteNotebook(notebook.toEntity())
    }
}
