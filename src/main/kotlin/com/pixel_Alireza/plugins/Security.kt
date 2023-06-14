package com.pixel_Alireza.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.pixel_Alireza.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
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




    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
//    routing {
//        get("/session/increment") {
//                val session = call.sessions.get<MySession>() ?: MySession()
//                call.sessions.set(session.copy(count = session.count + 1))
//                call.respondText("Counter is ${session.count}. Refresh to increment.")
//            }
//    }
}
