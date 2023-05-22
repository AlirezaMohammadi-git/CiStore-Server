package com.pixel_Alireza.routing.socket

import com.pixel_Alireza.data.model.roomInfo.RoomInfo
import com.pixel_Alireza.globalRoom.ChatRoomController
import com.pixel_Alireza.globalRoom.MemberAlreadyExistException
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


fun Route.globalChat(
    chatRoomController: ChatRoomController
) {

    webSocket("/globalChat") {

//        val session = call.sessions.get<MySession>() // getting a MySession form android
        val session = this.call.sessions.get<ChatSession>()

        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
            return@webSocket
        }

        try {
            chatRoomController.onJoin(
                sessionId = session.sessionId,
                username = session.userName,
                sockets = this
            )


            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    chatRoomController.sendMessage(
                        session.userName,
                        frame.readText()
                    )
                }
            }


        } catch (e: MemberAlreadyExistException) {
            call.respond(HttpStatusCode.Conflict, "Member already exist!")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            chatRoomController.tryDisconnect(session.userName)
        }

    }


}