package com.pixel_Alireza.data.model.user

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId

@Serializable
data class User(
    val username: String,
    @BsonId val email: String,
    val password: String,
    val salt: String,
)
