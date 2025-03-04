package com.example.boardapp.controller

import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val dotenv = Dotenv.configure().ignoreIfMissing().load()

fun Application.OAuthController() {
    routing {
        get("/auth/kakao") {
            val clientId = dotenv["KAKAO_CLIENT_ID"] ?: error("Missing KAKAO_CLIENT_ID")
            val redirectUri = dotenv["KAKAO_REDIRECT_URI"] ?: error("Missing KAKAO_REDIRECT_URI")

            val kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize?client_id=$clientId&redirect_uri=$redirectUri&response_type=code"
            call.respondRedirect(kakaoAuthUrl)
        }

        get("/auth/google") {
            val clientId = dotenv["GOOGLE_CLIENT_ID"] ?: error("Missing GOOGLE_CLIENT_ID")
            val redirectUri = dotenv["GOOGLE_REDIRECT_URI"] ?: error("Missing GOOGLE_REDIRECT_URI")

            val googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=$clientId&redirect_uri=$redirectUri&response_type=code&scope=email%20profile"
            call.respondRedirect(googleAuthUrl)
        }
    }
}
