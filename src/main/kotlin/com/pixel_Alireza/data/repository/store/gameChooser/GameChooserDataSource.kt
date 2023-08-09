package com.pixel_Alireza.data.repository.store.gameChooser

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.model.response.storeItems.discounts.ProductData
import com.pixel_Alireza.data.model.response.storeItems.gameChooser.GameProductData

interface GameChooserDataSource {
    suspend fun getAllGameProducts(): List<GameProductData>
    suspend fun addNewGame(newGame: GameProductData): Resource<Unit>
    suspend fun updateGame(newGame : GameProductData): Resource<Unit>
    suspend fun deleteGame(id: String): Resource<Unit>

    // when on gameProduct clicked the id of it will send here and then
    // related product data will send to client :

    // gameProductData -> ProductData

    suspend fun getProductDataByTag (gameProductDataTag: String) : List<ProductData>
    suspend fun addProductData (productData: ProductData) : Resource<Unit>
    suspend fun updateProductData (productData: ProductData) : Resource<Unit>
    suspend fun deleteProductData (id: String) : Resource<Unit>
}