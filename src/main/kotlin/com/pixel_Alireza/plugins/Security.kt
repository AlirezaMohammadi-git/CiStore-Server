package com.pixel_Alireza.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.pixel_Alireza.security.token.TokenConfig
import com.pixel_Alireza.session.MySession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlin.collections.set

fun Application.configureSecurity(config: TokenConfig) {

    authentication {
        jwt {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }





    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    intercept( ApplicationCallPipeline.Features ) {
        if (call.sessions.get<MySession>() == null) {
            val username = call.parameters["username"] ?: "Guest"
            call.sessions.set(MySession(userName = username, sessionId = generateNonce()))
        }
    }
}
