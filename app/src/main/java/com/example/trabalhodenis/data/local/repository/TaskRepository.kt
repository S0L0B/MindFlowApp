package com.example.trabalhodenis.data.local.repository

import com.example.trabalhodenis.data.local.dao.TaskDao
import com.example.trabalhodenis.data.mapper.toEntity
import com.example.trabalhodenis.data.mapper.toTask
import com.example.trabalhodenis.ui.Task
import kotlinx.coroutines.flow.map

class TaskRepository(
    private val taskDao: TaskDao
) {

    fun getAllTasks() = taskDao.getAllTasks().map { list -> list.map { it.toTask() } }

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    suspend fun updateTaskStatus(
        taskId: Int,
        completed: Boolean
    ) {
        taskDao.updateTaskStatus(taskId, completed)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    suspend fun deleteTaskById(taskId: Int) {
        taskDao.deleteTaskById(taskId)
    }
}
