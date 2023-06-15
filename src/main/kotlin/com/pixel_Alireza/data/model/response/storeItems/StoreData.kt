package com.pixel_Alireza.data.model.response.storeItems

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


@Serializable
data class StoreData(
    val name: String,
    val amount: Int,
    val price: Int,
    val imageURL: String,
    val priority: Int,
    val id: String = ObjectId().toString()
)
