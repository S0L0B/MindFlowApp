package com.example

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*

fun main() {
    println("--- Iniciando Servidor MindFlow na porta 8086 ---")

    embeddedServer(
        Netty,
        port = 8086,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Servidor MindFlow Online!")
        }

        swaggerUI(
            path = "swagger",
            swaggerFile = "openapi/documentation.yaml"
        )

        post("/auth/google") {
            call.respondText(
                text = """
                    {
                      "userId": "google-user-id",
                      "name": "Usuário Google",
                      "email": "usuario@gmail.com"
                    }
                """.trimIndent(),
                contentType = ContentType.Application.Json,
                status = HttpStatusCode.OK
            )
        }

        route("/tasks") {
            get {
                call.respondText(
                    text = """
                        [
                          {
                            "id": 1,
                            "title": "Estudar Kotlin",
                            "subject": "Programação",
                            "priority": "Alta",
                            "isCompleted": false
                          },
                          {
                            "id": 2,
                            "title": "Revisar apresentação",
                            "subject": "Projeto Mobile",
                            "priority": "Média",
                            "isCompleted": false
                          }
                        ]
                    """.trimIndent(),
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.OK
                )
            }

            post {
                call.respondText(
                    text = """
                        {
                          "status": "Tarefa criada com sucesso"
                        }
                    """.trimIndent(),
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.Created
                )
            }
        }
    }
}