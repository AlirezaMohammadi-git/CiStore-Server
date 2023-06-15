package com.pixel_Alireza.globalRoom

import io.ktor.websocket.*


data class Member(
    val username: String,
    val sessionId: String,
    val socket: WebSocketSession
)
