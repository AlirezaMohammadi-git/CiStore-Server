package com.pixel_Alireza

import com.pixel_Alireza.data.user.UserDataManager
import com.pixel_Alireza.di.mainModule
import com.pixel_Alireza.plugins.*
import com.pixel_Alireza.security.hashing.HashingService
import com.pixel_Alireza.security.token.TokenConfig
import com.pixel_Alireza.security.token.TokenService
import io.ktor.server.application.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    install(Koin) {
        slf4jLogger()
        modules(mainModule)
    }


    // 1hour = 60L * 60L * 1000
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 60L * 60L * 1000 * 24L * 180,
        secret = System.getenv("JWT_ENV")
    )


    val userDataSource: UserDataManager by inject()
    val hashingService: HashingService by inject()
    val tokenService: TokenService by inject()


    configureSecurity(config = tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting(
        hashingService ,
        userDataSource ,
        tokenService ,
        tokenConfig
    )
}
