package com.pixel_Alireza.plugins

import com.pixel_Alireza.data.user.UserDataManager
import com.pixel_Alireza.routing.auth.*
import com.pixel_Alireza.security.hashing.HashingService
import com.pixel_Alireza.security.token.TokenConfig
import com.pixel_Alireza.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import org.litote.kmongo.second

fun Application.configureRouting(
    hashingService: HashingService,
    userDataManager: UserDataManager,
    tokenService: TokenService,
    config: TokenConfig
) {
    
    routing {

        signUp(
            hashingService ,
            userDataManager
        )


        signIn(
            hashingService ,
            userDataManager ,
            tokenService ,
            config
        )


        authenticateUser()

        getSecretInfo()


        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
