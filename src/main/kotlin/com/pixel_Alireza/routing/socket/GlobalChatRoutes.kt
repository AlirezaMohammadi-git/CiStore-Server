package com.pixel_Alireza.routing.socket

import com.pixel_Alireza.data.model.request.CloseSession
import com.pixel_Alireza.data.model.response.CommonResponse
import com.pixel_Alireza.data.model.response.auth.SignUpResponse
import com.pixel_Alireza.globalRoom.ChatRoomController
import com.pixel_Alireza.globalRoom.MemberAlreadyExistException
import com.pixel_Alireza.session.ChatSession
import io.ktor.http.*
import io.ktor.server.application.*
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
        val close = CloseSession()
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
                        return@webSocket
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


fun Route.getAllMessages(
    chatRoomController: ChatRoomController
) {
    get("getMessages") {
        try {
            chatRoomController.autoDeleteChat()
            val page = call.parameters["page"]?.toInt()
            if (page == null || page <= 0) {
                call.respond(SignUpResponse(false, "wrong count"))
            } else {
                val messages = chatRoomController.getAllMessages(page)
                call.respond(CommonResponse(res = true, data = messages))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}

fun Route.deleteAllMessages(
    chatRoomController: ChatRoomController
) {
    delete("deleteAllMessages") {
        try {
            chatRoomController.deleteAllMessages()
            call.respond(CommonResponse(res = true, message = "Messages deleted", data = null))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Route.disconnect(chatRoomController: ChatRoomController){
    delete ("tryDisconnect"){
        val username = call.parameters["username"].toString()
        try {
            if (username.isNotBlank()){
                chatRoomController.tryDisconnect(username)
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(HttpStatusCode.ExpectationFailed)
            }
        }catch (e:Exception){
            e.printStackTrace()
            call.respond(HttpStatusCode.ExpectationFailed)
        }
    }
}