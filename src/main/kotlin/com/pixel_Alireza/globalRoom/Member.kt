package com.pixel_Alireza.globalRoom

import io.ktor.server.websocket.*


data class Member(
    val username : String ,
    val sessionId : String ,
    val socket : DefaultWebSocketServerSession
)
