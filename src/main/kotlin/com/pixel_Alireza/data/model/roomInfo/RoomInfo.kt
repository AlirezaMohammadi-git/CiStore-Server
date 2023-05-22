package com.pixel_Alireza.data.model.roomInfo

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId



@Serializable
data class RoomInfo(
    val senderUsername : String,
    val profileId : Int,
    val game : String,
    val mode : String,
    val private : Boolean,
    val pass : String? = null ,
    val timestamp : Long = System.currentTimeMillis() ,
    val id : String = ObjectId().toString()
)
