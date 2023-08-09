package com.pixel_Alireza.data.repository.chat

import com.pixel_Alireza.data.model.message.Message

interface ChatDatasource {
    suspend fun getAllMessages(pageSize : Int = 25 , page : Int) : List<Message>
    suspend fun insertMessage( message: Message )
    suspend fun deleteMessageById(id : String)
    suspend fun deleteAllMessages()
    suspend fun autoDeleteChat()
}