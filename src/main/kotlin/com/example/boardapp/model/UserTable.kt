package com.example.boardapp.model

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val password = varchar("password", 255)
    val nickname = varchar("nickname", 100)
    val age = integer("age").nullable()
    val phoneNumber = varchar("phone_number", 20).nullable()
}
