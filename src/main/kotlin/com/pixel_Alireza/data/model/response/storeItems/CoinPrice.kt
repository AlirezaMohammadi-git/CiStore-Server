package com.pixel_Alireza.data.model.response.storeItems

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


@Serializable
data class CoinPrice(
    val displayName: String,
    val coinCount: Int,
    val coinPrice: Int,
    val gameTag: String?,
    val id: String = ObjectId().toString()
)
