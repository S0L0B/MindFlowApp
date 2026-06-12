plugins {
    alias(libs.plugins.kotlinJvm)
    id("io.ktor.plugin") version "3.0.3"
    application // Adiciona o plugin de aplicação
}

application {
    mainClass.set("com.example.ApplicationKt") // Aponta para o seu arquivo Application.kt
}

ktor {
    fatJar {
        archiveFileName.set("server.jar")
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:3.0.3")
    implementation("io.ktor:ktor-server-netty-jvm:3.0.3")
    implementation("io.ktor:ktor-server-openapi:3.0.3")
    implementation("io.ktor:ktor-server-swagger:3.0.3")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.0.3")
    implementation("ch.qos.logback:logback-classic:1.5.6")
}
