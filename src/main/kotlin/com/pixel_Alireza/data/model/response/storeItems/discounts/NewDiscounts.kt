package com.pixel_Alireza.data.model.response.storeItems.discounts

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


@Serializable
data class NewDiscounts(
    val name: String,
    val productTag: String,
    val mainPrice: Int,
    val discountedPrice: String,
    val imageURL: String,
)
