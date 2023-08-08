package com.pixel_Alireza.data.model.repository.discounts

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.model.response.storeItems.discounts.NewDiscounts

interface DiscountDataSource {
    suspend fun getAllDiscounts(): List<NewDiscounts>
    suspend fun addNewDiscount(discountItem: NewDiscounts): Resource<Unit>
    suspend fun updateDiscount( newDiscountItem: NewDiscounts): Resource<Unit>
    suspend fun deleteDiscount(id : String): Resource<Unit>

}