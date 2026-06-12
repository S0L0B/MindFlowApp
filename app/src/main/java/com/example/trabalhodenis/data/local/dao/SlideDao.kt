package com.example.trabalhodenis.data.local.dao

import androidx.room.*
import com.example.trabalhodenis.data.local.entity.SlideEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SlideDao {

    @Insert
    suspend fun insert(
        slide: SlideEntity
    )

    @Delete
    suspend fun delete(
        slide: SlideEntity
    )

    @Query("SELECT * FROM slides")
    fun getAll(): Flow<List<SlideEntity>>
}