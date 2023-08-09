package com.pixel_Alireza.data.model.response.storeItems.gameChooser


import kotlinx.serialization.Serializable
import org.bson.types.ObjectId


@Serializable
data class GameProductData (
    val name : String ,
    var isNewProduct : Boolean ,
    val image : String ,
    val gameTag : String ,
    // most be different for each item
    val productTag : String ,
    val id : String = ObjectId().toString()
)
