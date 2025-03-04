package com.example.boardapp.config

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson { }
    }
}
