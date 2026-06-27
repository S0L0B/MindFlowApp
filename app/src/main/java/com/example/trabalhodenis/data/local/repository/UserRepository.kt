package com.example.trabalhodenis.data.local.repository

import com.example.trabalhodenis.data.local.dao.UserDao
import com.example.trabalhodenis.data.local.entity.UserEntity
import com.example.trabalhodenis.data.remote.MindFlowApi

class UserRepository(private val userDao: UserDao) {
    suspend fun login(email: String, password: String): Boolean {
        // Tenta login no servidor (Swagger)
        val serverSuccess = MindFlowApi.login(email, password)
        
        // Se o servidor aceitar, consideramos logado. 
        // Também verificamos localmente para manter a persistência offline se desejar.
        val localUser = userDao.getUserByEmail(email)
        val localSuccess = localUser != null && localUser.passwordHash == password
        
        return serverSuccess || localSuccess
    }

    suspend fun register(name: String, email: String, password: String): Boolean {
        // Registra no servidor (Swagger)
        val serverSuccess = MindFlowApi.register(name, email, password)
        
        // Sempre salva localmente para permitir login offline depois
        return try {
            val existing = userDao.getUserByEmail(email)
            if (existing == null) {
                userDao.registerUser(UserEntity(email, name, password))
            }
            // Retorna true se conseguiu registrar em pelo menos um lugar (prioridade para o banco local no trabalho)
            true 
        } catch (e: Exception) {
            serverSuccess
        }
    }
}
