package com.example.trabalhodenis.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val email: String,
    val name: String,
    val passwordHash: String // Em um app real, nunca salve em texto plano, mas para o trabalho usaremos direto
)
