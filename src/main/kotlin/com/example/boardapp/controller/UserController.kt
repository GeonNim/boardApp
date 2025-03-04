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
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

data class UpdateUserRequest(val nickname: String?, val age: Int?, val phoneNumber: String?)

fun Application.UserController() {
    routing {
        authenticate("auth-jwt") {
            // ✅ 현재 로그인된 사용자 정보 조회
            get("/user/me") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt()

                if (userId == null) {
                    call.respondText("Unauthorized", status = io.ktor.http.HttpStatusCode.Unauthorized)
                    return@get
                }

                val user = transaction {
                    UserTable.select { UserTable.id eq userId }.singleOrNull()
                }

                if (user == null) {
                    call.respondText("User not found", status = io.ktor.http.HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(mapOf(
                    "id" to user[UserTable.id].value,
                    "email" to user[UserTable.email],
                    "nickname" to user[UserTable.nickname],
                    "age" to user[UserTable.age],
                    "phoneNumber" to user[UserTable.phoneNumber]
                ))
            }

            // ✅ 사용자 정보 수정
            put("/user/me") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt()

                if (userId == null) {
                    call.respondText("Unauthorized", status = io.ktor.http.HttpStatusCode.Unauthorized)
                    return@put
                }

                val request = call.receive<UpdateUserRequest>()

                val updatedRows = transaction {
                    UserTable.update({ UserTable.id eq userId }) {
                        request.nickname?.let { nickname -> it[UserTable.nickname] = nickname }
                        request.age?.let { age -> it[UserTable.age] = age }
                        request.phoneNumber?.let { phone -> it[UserTable.phoneNumber] = phone }
                    }
                }

                if (updatedRows > 0) {
                    call.respondText("User updated successfully!")
                } else {
                    call.respondText("Failed to update user", status = io.ktor.http.HttpStatusCode.InternalServerError)
                }
            }

            // ✅ 사용자 계정 삭제
            delete("/user/me") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asInt()

                if (userId == null) {
                    call.respondText("Unauthorized", status = io.ktor.http.HttpStatusCode.Unauthorized)
                    return@delete
                }

                val deletedRows = transaction {
                    UserTable.deleteWhere { UserTable.id eq userId }
                }

                if (deletedRows > 0) {
                    call.respondText("User deleted successfully!")
                } else {
                    call.respondText("Failed to delete user", status = io.ktor.http.HttpStatusCode.InternalServerError)
                }
            }
        }
    }
}
