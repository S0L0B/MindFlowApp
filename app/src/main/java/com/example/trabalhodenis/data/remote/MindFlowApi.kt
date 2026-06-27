package com.example.trabalhodenis.data.remote

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class AuthResponse(val status: String? = null, val token: String? = null)

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RegisterRequest(val name: String, val email: String, val password: String)

object MindFlowApi {
    private const val BASE_URL = "http://10.0.2.2:8086" // IP do host para o emulador Android

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    suspend fun login(email: String, password: String): Boolean {
        return try {
            val response = client.post("$BASE_URL/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }

    suspend fun register(name: String, email: String, password: String): Boolean {
        return try {
            val response = client.post("$BASE_URL/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(name, email, password))
            }
            response.status == HttpStatusCode.Created
        } catch (e: Exception) {
            false
        }
    }
}
