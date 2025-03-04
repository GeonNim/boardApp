package com.example.boardapp.controller

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.boardapp.config.JwtConfig
import com.example.boardapp.model.UserTable
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

data class AuthRequest(val email: String, val password: String)

fun Application.AuthController() {
    routing {
        post("/register") {
            val request = call.receive<AuthRequest>()
            val hashedPassword = BCrypt.withDefaults().hashToString(12, request.password.toCharArray())

            transaction {
                UserTable.insert {
                    it[email] = request.email
                    it[password] = hashedPassword
                }
            }
            call.respondText("User registered successfully!")
        }

        post("/login") {
            val request = call.receive<AuthRequest>()

            val user = transaction {
                UserTable.select { UserTable.email eq request.email }.singleOrNull()
            }

            if (user == null) {
                call.respondText("User not found", status = io.ktor.http.HttpStatusCode.NotFound)
                return@post
            }

            val storedHash = user[UserTable.password]
            val passwordVerification = BCrypt.verifyer().verify(request.password.toCharArray(), storedHash)

            if (!passwordVerification.verified) {
                call.respondText("Invalid credentials", status = io.ktor.http.HttpStatusCode.Unauthorized)
                return@post
            }

            val token = JwtConfig.generateToken(user[UserTable.id].value)
            call.respond(mapOf("token" to token))
        }

        post("/logout") {
            call.respondText("Logged out successfully")
        }
    }
}
