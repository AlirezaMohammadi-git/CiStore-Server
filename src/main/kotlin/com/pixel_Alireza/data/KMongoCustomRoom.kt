package com.pixel_Alireza.data

import com.mongodb.client.model.Filters.*
import com.pixel_Alireza.data.model.roomInfo.RoomInfo
import com.pixel_Alireza.globalRoom.Member
import com.pixel_Alireza.globalRoom.MemberAlreadyExistException
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import java.util.concurrent.ConcurrentHashMap


class KMongoCustomRoom(
    db: CoroutineDatabase
) : CustomRoomDataSource {

    private val roomDatabase = db.getCollection<RoomInfo>()

    private val activeMembersList =
        ConcurrentHashMap<String, Member>() // key , value -> this is something like List<Map> in android

    override fun onJoin(sessionId: String, username: String, sockets: DefaultWebSocketServerSession) {
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


    override suspend fun sendRoom(roomInfo: String) {
        try {
            activeMembersList.values.forEach { member ->
                val parsedString = Json.decodeFromString<RoomInfo>(roomInfo)
                roomDatabase.insertOne(parsedString)
                member.socket.send(Frame.Text(roomInfo))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun tryDisconnect(username: String) {
        activeMembersList[username]?.socket?.close()
        if (activeMembersList.containsKey(username)){
            activeMembersList.remove(username)
        }
    }

    override suspend fun getRooms(count: Int) : List<RoomInfo>{
        return try {
            roomDatabase.find().sort(descending()).limit(count).toList()
        }catch (e:Exception){
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun deleteRoom() {
         try {
             // 1hour = 60L * 60L * 1000
             val timestamp = System.currentTimeMillis() - 10000
             val filter = where("this.timestamp < $timestamp")
             roomDatabase.deleteMany( filter )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteAllRooms() {
        roomDatabase.deleteMany()
    }


}