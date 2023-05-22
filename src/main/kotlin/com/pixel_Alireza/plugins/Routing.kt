package com.pixel_Alireza.plugins

import com.pixel_Alireza.data.CustomRoomDataSource
import com.pixel_Alireza.data.model.user.UserDataManager
import com.pixel_Alireza.globalRoom.ChatRoomController
import com.pixel_Alireza.routing.auth.*
import com.pixel_Alireza.routing.socket.globalChat
import com.pixel_Alireza.routing.socket.globalRoom
import com.pixel_Alireza.security.hashing.HashingService
import com.pixel_Alireza.security.token.TokenConfig
import com.pixel_Alireza.security.token.TokenService
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*

fun Application.configureRouting(
    hashingService: HashingService,
    userDataManager: UserDataManager,
    tokenService: TokenService,
    config: TokenConfig,
    chatRoomController: ChatRoomController ,
    customRoomDataSource: CustomRoomDataSource
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

        globalChat(chatRoomController)

        updateUsername(userDataManager)

        updatePass(userDataManager , hashingService)

        globalRoom(customRoomDataSource)


        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
