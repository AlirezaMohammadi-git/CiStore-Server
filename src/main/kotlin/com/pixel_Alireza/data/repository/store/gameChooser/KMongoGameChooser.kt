package com.pixel_Alireza.data.repository.store.gameChooser

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.model.response.storeItems.discounts.NewDiscounts
import com.pixel_Alireza.data.model.response.storeItems.discounts.ProductData
import com.pixel_Alireza.data.model.response.storeItems.gameChooser.GameProductData
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class KMongoGameChooser(db: CoroutineDatabase) : GameChooserDataSource {
    private val gameChooserTable = db.getCollection<GameProductData>()
    private val productData = db.getCollection<ProductData>()
    override suspend fun getAllGameProducts(): List<GameProductData> {
        return try {
            gameChooserTable.find().toList()
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun addNewGame(newGame: GameProductData): Resource<Unit> {
        return try {
            val tagChecker = gameChooserTable.find(filter = GameProductData::gameTag eq newGame.gameTag).toList()
            if (tagChecker.isEmpty()) {
                val res = gameChooserTable.insertOne(newGame).wasAcknowledged()
                if (res)
                    Resource.Success(Unit) else
                    Resource.Error("item not added! something went wrong!")
            } else {
                Resource.Error("there is another item with same game tag.")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun updateGame(newGame: GameProductData): Resource<Unit> {
        return try {
            val res = gameChooserTable
                .updateOne(filter = NewDiscounts::id eq newGame.id, target = newGame).wasAcknowledged()
            if (res) Resource.Success(Unit) else Resource.Error("item not updated! check server")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun deleteGame(id: String): Resource<Unit> {
        return try {
            val res = gameChooserTable.deleteOne(GameProductData::id eq id).wasAcknowledged()
            if (res) Resource.Success(Unit) else Resource.Error("item not deleted! check server")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun getProductDataByTag(gameProductDataTag: String): List<ProductData> {
        return try {
            productData.find(filter = ProductData::productTag eq gameProductDataTag).toList()
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }

    override suspend fun addProductData(productData: ProductData): Resource<Unit> {
        return try {
            val insert = this.productData.insertOne(productData).wasAcknowledged()
            if (insert) {
                Resource.Success(Unit)
            } else {
                Resource.Error("item not added! something went wrong!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun updateProductData(productData: ProductData): Resource<Unit> {
        return try {
            val res = this.productData
                .updateOne(filter = ProductData::gameTag eq productData.gameTag, target = productData).wasAcknowledged()
            if (res) Resource.Success(Unit) else Resource.Error("item not updated! check server")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

    override suspend fun deleteProductData(id: String): Resource<Unit> {
        return try {
            val res = this
                .productData.deleteOne(filter = ProductData::id eq id).wasAcknowledged()
            if (res) Resource.Success(Unit) else Resource.Error("item not deleted! check server")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message.toString())
        }
    }

}