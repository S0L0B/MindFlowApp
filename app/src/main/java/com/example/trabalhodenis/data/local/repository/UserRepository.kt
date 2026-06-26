package com.example.trabalhodenis.data.local.repository

import com.example.trabalhodenis.data.local.dao.UserDao
import com.example.trabalhodenis.data.local.entity.UserEntity

class UserRepository(private val userDao: UserDao) {
    suspend fun login(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email)
        return user != null && user.passwordHash == password
    }

    suspend fun register(name: String, email: String, password: String): Boolean {
        return try {
            val existing = userDao.getUserByEmail(email)
            if (existing != null) return false
            
            userDao.registerUser(UserEntity(email, name, password))
            true
        } catch (e: Exception) {
            false
        }
    }
}
