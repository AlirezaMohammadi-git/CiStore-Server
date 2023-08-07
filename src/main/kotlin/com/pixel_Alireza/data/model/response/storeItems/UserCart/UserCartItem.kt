package com.pixel_Alireza.data.model.response.storeItems.UserCart

import org.bson.types.ObjectId

data class UserCartItem(
    val productName: String,
    val coinCount: Int,
    val price: Int,
    var count: Int,
    val image: String,
    val status: Int,
    val itemId: String = ObjectId().toString()
)

sealed class ProductStatus(val status: Int) {
    object WaitingForPurchase : ProductStatus(100)
    object WaitingForCheck : ProductStatus(101)
    object InProcessByOperator : ProductStatus(102)
    object ProcessCompleted : ProductStatus(102)
    object InformationWasWrong : ProductStatus(104)
}
