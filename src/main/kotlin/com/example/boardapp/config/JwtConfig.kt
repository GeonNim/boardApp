package com.example.boardapp.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.cdimascio.dotenv.Dotenv
import java.util.*

object JwtConfig {
    private val dotenv = Dotenv.configure().ignoreIfMissing().load()
    private val secret = dotenv["JWT_SECRET"] ?: "default_secret"
    private val issuer = dotenv["JWT_ISSUER"] ?: "boardApp"
    private val audience = dotenv["JWT_AUDIENCE"] ?: "boardAudience"
    private val expirationTime = (dotenv["JWT_EXPIRATION"]?.toIntOrNull() ?: 3600) * 1000 // 밀리초 변환

    private val algorithm = Algorithm.HMAC256(secret)

    fun generateToken(userId: Int): String {
        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + expirationTime))
            .sign(algorithm)
    }

    fun verifier() = JWT.require(algorithm)
        .withIssuer(issuer)
        .withAudience(audience)
        .build()
}
