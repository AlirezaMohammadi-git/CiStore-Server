package com.pixel_Alireza.routing.globalChat

import com.pixel_Alireza.globalRoom.MemberAlreadyExistException
import com.pixel_Alireza.globalRoom.RoomController
import com.pixel_Alireza.session.MySession
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach


fun Route.chat(
    roomController: RoomController
) {

    webSocket("/globalChat") {

//        val session = call.sessions.get<MySession>() // getting a MySession form android
        val session = this.call.sessions.get<MySession>()

        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
            return@webSocket
        }

        try {
            roomController.onJoin(
                sessionId = session.sessionId,
                username = session.userName,
                sockets = this
            )


            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    roomController.sendMessage(
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
            roomController.tryDisconnect(session.userName)
        }

    }

}