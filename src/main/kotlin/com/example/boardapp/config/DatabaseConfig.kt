package com.example.boardapp.config

import com.example.boardapp.model.UserTable
import io.github.cdimascio.dotenv.Dotenv
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

object DatabaseConfig {
    fun connect() {
        val dotenv = Dotenv.configure().ignoreIfMissing().load()
        val dbUrl = dotenv["DB_URL"] ?: error("DB_URL is missing in .env")
        val dbUser = dotenv["DB_USER"] ?: error("DB_USER is missing in .env")
        val dbPassword = dotenv["DB_PASSWORD"] ?: error("DB_PASSWORD is missing in .env")

        Database.connect(
            url = "$dbUrl?sslmode=disable", 
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )

        try {
            transaction {
                SchemaUtils.createMissingTablesAndColumns(UserTable)
                println("✅ UserTable created or updated successfully!")
            }
        } catch (e: Exception) {
            println("❌ Failed to create UserTable: ${e.message}")
        }

        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_REPEATABLE_READ
    }
}
