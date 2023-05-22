package com.pixel_Alireza.data

import com.pixel_Alireza.data.model.roomInfo.RoomInfo
import io.ktor.server.websocket.*

interface CustomRoomDataSource {


    fun onJoin(
        sessionId: String,
        username: String,
        sockets: DefaultWebSocketServerSession
    )

    suspend fun getRooms(count: Int) : List<RoomInfo> //method -> get
    suspend fun sendRoom(roomInfo: String)
    // ws -> every user when try to make an custom should send it as Frame.Text
    suspend fun deleteRoom()
    //ws -> every user when try to delete an custom should send it as Frame.Text

    suspend fun deleteAllRooms()

    suspend fun tryDisconnect(username: String)


}