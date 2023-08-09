package com.pixel_Alireza.data.model.response.storeItems.discounts

import com.pixel_Alireza.data.model.response.storeItems.CoinPrice
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


// every new item can choose between GameProductData


@Serializable
data class ProductData(
    val name: String,
    val isAvailable: Boolean,
    val coinPrice: List<CoinPrice>,
    val image: String,
    // how much will take to complete order :
    val completingTime: String,
    // use to show same products in related products row :
    val gameTag: String,
    // for choosing related GameProductData in second app :
    var productTag: String,
    // use for items that are not coin
    val price: Int?,
    val id: String = ObjectId().toString()
)
