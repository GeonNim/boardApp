package com.example.boardapp

import com.example.boardapp.config.DatabaseConfig
import com.example.boardapp.config.configureSecurity
import com.example.boardapp.config.configureSerialization
import com.example.boardapp.controller.UserController
import com.example.boardapp.controller.AuthController
import com.example.boardapp.controller.OAuthController
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {
    DatabaseConfig.connect()
    configureSecurity()
    configureSerialization()

    routing {
        get("/") {
            call.respondText("Ktor Server is Running!")
        }
        this@module.UserController()  // ✅ 명시적 호출
        this@module.AuthController()  // ✅ 명시적 호출
        this@module.OAuthController() // ✅ 명시적 호출
    }
}


