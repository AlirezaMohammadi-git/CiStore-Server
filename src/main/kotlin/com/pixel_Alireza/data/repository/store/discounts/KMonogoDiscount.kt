package com.pixel_Alireza.data.repository.store.discounts

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.model.response.storeItems.discounts.NewDiscounts
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class KMonogoDiscount(db: CoroutineDatabase) : DiscountDataSource {
    private val discountDatabase = db.getCollection<NewDiscounts>()
    override suspend fun getAllDiscounts(): List<NewDiscounts> {
        return try {
            discountDatabase.find().toList()
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun addNewDiscount(discountItem: NewDiscounts): Resource<Unit> {
        return try {
            val res = discountDatabase.insertOne(discountItem).wasAcknowledged()
            if (res)
                Resource.Success(Unit) else
                Resource.Error("item not added! something went wrong!")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun updateDiscount( newDiscountItem: NewDiscounts): Resource<Unit> {
        return try {
            val res = discountDatabase
                .updateOne(filter = NewDiscounts::id eq newDiscountItem.id, target = newDiscountItem).wasAcknowledged()
            if (res) Resource.Success(Unit) else Resource.Error("item not updated! check server")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun deleteDiscount(id: String): Resource<Unit> {
        return try {
            val res = discountDatabase.deleteOne(NewDiscounts::id eq id).wasAcknowledged()
            if (res) Resource.Success(Unit) else Resource.Error("item not deleted! check server")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

}