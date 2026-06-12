package com.example.trabalhodenis.data.local.repository

import com.example.trabalhodenis.data.local.dao.PresentationDao
import com.example.trabalhodenis.data.mapper.toEntity
import com.example.trabalhodenis.data.mapper.toPresentation
import com.example.trabalhodenis.ui.Presentation
import kotlinx.coroutines.flow.map

class PresentationRepository(
    private val presentationDao: PresentationDao
) {

    fun getAllPresentations() =
        presentationDao.getAllPresentations().map { list -> list.map { it.toPresentation() } }

    suspend fun addPresentation(
        presentation: Presentation
    ) {
        presentationDao.insertPresentation(presentation.toEntity())
    }

    suspend fun deletePresentation(
        presentation: Presentation
    ) {
        presentationDao.deletePresentation(presentation.toEntity())
    }

    suspend fun deletePresentationById(
        id: Int
    ) {
        presentationDao.deletePresentationById(id)
    }
}
