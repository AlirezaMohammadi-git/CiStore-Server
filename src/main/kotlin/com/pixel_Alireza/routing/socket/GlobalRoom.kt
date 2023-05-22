package com.pixel_Alireza.routing.socket

import com.pixel_Alireza.data.CustomRoomDataSource
import com.pixel_Alireza.data.model.roomInfo.RoomInfo
import com.pixel_Alireza.session.ChatSession
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach


fun Route.globalRoom(
    customRoomDataSource: CustomRoomDataSource,
) {

    webSocket("/globalCustomRooms") {
        val session = call.sessions.get<ChatSession>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "no session"))
        }
        try {
            customRoomDataSource.onJoin(
                session!!.sessionId,
                session.userName,
                this
            )
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    customRoomDataSource.sendRoom(frame.readText())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            if (session != null) {
                customRoomDataSource.tryDisconnect(session.userName)
            }
        }
    }

    get ("/getRooms"){
        customRoomDataSource.deleteRoom()
        call.respond(HttpStatusCode.OK , customRoomDataSource.getRooms(25))
    }

    delete ("/deleteAllCustom"){
        customRoomDataSource.deleteRoom()
        call.respond(HttpStatusCode.OK)
    }

}