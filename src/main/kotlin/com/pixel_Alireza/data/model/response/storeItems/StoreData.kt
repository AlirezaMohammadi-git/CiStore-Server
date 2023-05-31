package com.pixel_Alireza.data.model.response.storeItems

import org.bson.types.ObjectId

data class StoreData(
    val name: String,
    val count: Int,
    val price: String,
    val imageURL: String,
    val priority: Int,
    val id: String = ObjectId().toString()
)
