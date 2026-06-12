package com.example.trabalhodenis.data.local.repository

import com.example.trabalhodenis.data.local.dao.CalendarDao
import com.example.trabalhodenis.data.mapper.toCalendarEvent
import com.example.trabalhodenis.data.mapper.toEntity
import com.example.trabalhodenis.ui.CalendarEvent
import kotlinx.coroutines.flow.map

class CalendarRepository(
    private val calendarDao: CalendarDao
) {

    fun getAllEvents() =
        calendarDao.getAllEvents().map { list -> list.map { it.toCalendarEvent() } }

    suspend fun addEvent(event: CalendarEvent) {
        calendarDao.insertEvent(event.toEntity())
    }

    suspend fun deleteEvent(event: CalendarEvent) {
        calendarDao.deleteEvent(event.toEntity())
    }

    suspend fun deleteEventById(id: Int) {
        calendarDao.deleteEventById(id)
    }
}
