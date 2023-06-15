package com.pixel_Alireza.globalRoom

import com.pixel_Alireza.data.model.repository.chat.ChatDatasource
import com.pixel_Alireza.data.model.message.Message
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class ChatRoomController(
    val chatDatasource: ChatDatasource
) {

    private val activeMembersList =
        ConcurrentHashMap<String, Member>() // key , value -> this is something like List<Map> in android


    fun onJoin(
        sessionId: String,
        username: String,
        sockets: WebSocketSession
    ) {
        if (activeMembersList.containsKey(username)) {
            throw MemberAlreadyExistException()
        }
        //the username is hashmap key and Member is it's value(adding new member)
        activeMembersList[username] = Member(
            username = username,
            sessionId = sessionId,
            socket = sockets
        )

    }

    suspend fun sendMessage(
        senderUsername: String, message: String
    ) {
        activeMembersList.values.forEach { member ->
            val userMessage = Message(
                text = message,
                username = senderUsername,
                timestamp = System.currentTimeMillis()
            )
            val parsedMessage = Json.encodeToString(userMessage)
            member.socket.send(Frame.Text(parsedMessage))
        }
        chatDatasource.insertMessage(Message(
            text = message,
            username = senderUsername,
            timestamp = System.currentTimeMillis()
        ))
    }



    suspend fun getAllMessages(page : Int) : List<Message>{
        return try {
             chatDatasource.getAllMessages( page = page )
        }catch (e:Exception){
            e.printStackTrace()
            listOf()
        }
    }

    suspend fun autoDeleteChat(){
        chatDatasource.autoDeleteChat()
    }


    suspend fun deleteAllMessages(){
        chatDatasource.deleteAllMessages()
    }

    suspend fun deleteById(id:String){
        chatDatasource.deleteMessageById(id)
    }

    suspend fun tryDisconnect(username: String){
        activeMembersList[username]?.socket?.close()
        if (activeMembersList.containsKey(username)){
            activeMembersList.remove(username)
        }
    }

}