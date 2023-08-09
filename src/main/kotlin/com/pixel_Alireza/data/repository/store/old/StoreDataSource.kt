package com.pixel_Alireza.data.repository.store.old

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.model.response.storeItems.StoreData

interface StoreDataSource {
    suspend fun getAllItems() : List<StoreData>
    suspend fun addItem(storeData: StoreData) : Resource<Unit>
    suspend fun deleteItem(id:String): Resource<Unit>
    suspend fun updateItem(storeData: StoreData): Resource<Unit>
}