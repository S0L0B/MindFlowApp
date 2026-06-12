package com.example.trabalhodenis.data.local.dao

import androidx.room.*
import com.example.trabalhodenis.data.local.entity.NotebookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotebookDao {

    @Insert
    suspend fun insertNotebook(
        notebook: NotebookEntity
    )

    @Delete
    suspend fun deleteNotebook(
        notebook: NotebookEntity
    )

    @Query("SELECT * FROM notebooks")
    fun getAllNotebooks():
            Flow<List<NotebookEntity>>
}