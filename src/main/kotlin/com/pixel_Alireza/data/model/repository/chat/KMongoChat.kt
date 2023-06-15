package com.pixel_Alireza.data.model.repository.chat

import com.mongodb.client.model.Filters.where
import com.pixel_Alireza.data.model.message.Message
import com.pixel_Alireza.data.model.request.CloseSession
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq


class KMongoChat(
    db: CoroutineDatabase
) : ChatDatasource {

    private val roomDatabase = db.getCollection<Message>()


    override suspend fun autoDeleteChat() {
        try {
            // 1hour = 60L * 60L * 1000
            val timestamp = System.currentTimeMillis() - 10000 * 60L * 60L * 24 * 7
            val filter = where("this.timestamp < $timestamp")
            roomDatabase.deleteMany(filter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getAllMessages(pageSize: Int, page: Int): List<Message> {
        return try {
            val skip = (page - 1) * pageSize
            val sortCriteria = Document("timestamp", -1)
            roomDatabase.find()
                .sort(sortCriteria)
                .skip(skip)
                .limit(pageSize)
                .toList()

        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun insertMessage(message: Message) {
        val close = CloseSession()
        try {
            if (message.text != close.closeSession){
                roomDatabase.insertOne(message)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteMessageById(id: String) {
        try {
            roomDatabase.deleteOne(Message::id eq id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteAllMessages() {
        roomDatabase.deleteMany()
    }


}