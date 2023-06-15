package com.pixel_Alireza.data.model.repository.store

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.model.response.storeItems.StoreData
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class KMongoStore( db: CoroutineDatabase) : StoreDataSource {
    private val storeDatabase = db.getCollection<StoreData>()
    override suspend fun getAllItems(): List<StoreData> {
        return try {
            storeDatabase.find().toList()
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun addItem(storeData: StoreData): Resource<Unit> {
        return try {
            val res = storeDatabase.insertOne(storeData).wasAcknowledged()
            if (res) Resource.Success(Unit) else Resource.Error("item not added! check server")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun deleteItem(id: String): Resource<Unit> {
        return try {
            val res = storeDatabase.deleteOne(StoreData::id eq id).wasAcknowledged()
            if (res) Resource.Success(Unit) else Resource.Error("item not deleted! check server")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun updateItem(storeData: StoreData): Resource<Unit> {
        return try {
            val res = storeDatabase.updateOne(StoreData::id eq storeData.id, storeData).wasAcknowledged()
            if (res) Resource.Success(Unit) else Resource.Error("item not added! check server")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }
}