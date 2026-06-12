package com.example.trabalhodenis.data.local.dao

import androidx.room.*
import com.example.trabalhodenis.data.local.entity.PresentationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PresentationDao {

    @Query("SELECT * FROM presentations ORDER BY id DESC")
    fun getAllPresentations(): Flow<List<PresentationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPresentation(presentation: PresentationEntity)

    @Delete
    suspend fun deletePresentation(presentation: PresentationEntity)

    @Query("DELETE FROM presentations WHERE id = :presentationId")
    suspend fun deletePresentationById(presentationId: Int)
}
