package com.pixel_Alireza.data.model.response.storeItems.gameChooser

import org.bson.types.ObjectId

data class GameProductData(
    val name : String ,
    val isNewProduct : Boolean ,
    val image : String ,
    val id : String = ObjectId().toString()
)
