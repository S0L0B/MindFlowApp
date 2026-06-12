package com.example.trabalhodenis.data.local.repository

import com.example.trabalhodenis.data.local.dao.SlideDao
import com.example.trabalhodenis.data.mapper.toEntity
import com.example.trabalhodenis.data.mapper.toSlide
import com.example.trabalhodenis.ui.Slide
import kotlinx.coroutines.flow.map

class SlideRepository(
    private val slideDao: SlideDao
) {

    fun getAllSlides() =
        slideDao.getAll().map { list -> list.map { it.toSlide() } }

    suspend fun addSlide(slide: Slide) {
        slideDao.insert(slide.toEntity())
    }

    suspend fun deleteSlide(slide: Slide) {
        slideDao.delete(slide.toEntity())
    }
}
