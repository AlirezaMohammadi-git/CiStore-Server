package com.pixel_Alireza.routing.Store.gameChooser

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.model.response.CommonResponse
import com.pixel_Alireza.data.model.response.storeItems.discounts.ProductData
import com.pixel_Alireza.data.model.response.storeItems.gameChooser.GameProductData
import com.pixel_Alireza.data.repository.store.gameChooser.GameChooserDataSource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.getAllGameProducts(
    gameChooserDataSource: GameChooserDataSource
) {
    get("getAllGameProducts") {
        try {
            call.respond(CommonResponse(true, null, gameChooserDataSource.getAllGameProducts()))
        } catch (e: Exception) {
            call.respond(CommonResponse(false, e.message, null))
        }
    }
}


fun Route.getProductDataByTag(
    gameChooserDataSource: GameChooserDataSource
) {
    get("getProductDataByTag") {
        try {
            val pram = call.parameters["tag"].toString()

            call.respond(CommonResponse(true, null, gameChooserDataSource.getProductDataByTag(pram)))
        } catch (e: Exception) {
            call.respond(CommonResponse(false, e.message, null))
        }
    }
}


///////////////////////////////////////////////////
// below routing is used for second app that control the first
///////////////////////////////////////////////////
fun Route.postNewGame(
    gameChooserDataSource: GameChooserDataSource
) {
    //inserting
    post<GameProductData>("postNewGame") {
        val newData = it
        val result = gameChooserDataSource.addNewGame(newData)
        when (result) {
            is Resource.Error -> call.respond(
                HttpStatusCode.ExpectationFailed,
                CommonResponse(false, result.message, null)
            )

            is Resource.Success -> call.respond(
                HttpStatusCode.OK,
                CommonResponse(true, "item added!", null)
            )
        }


    }
}

fun Route.deleteGame(
    gameChooserDataSource: GameChooserDataSource
) {
    delete("deleteGame") {
        val id = call.parameters["id"]
        val result = gameChooserDataSource.deleteGame(id ?: "-1")
        when (result) {
            is Resource.Error -> call.respond(
                HttpStatusCode.ExpectationFailed,
                CommonResponse(false, result.message, null)
            )

            is Resource.Success -> call.respond(
                HttpStatusCode.OK,
                CommonResponse(true, "item deleted!", null)
            )
        }
    }
}

fun Route.updateGame(
    gameChooserDataSource: GameChooserDataSource
) {
    //updating
    put("updateGame") {
        val newData = call.receive<GameProductData>()
        val result = gameChooserDataSource.updateGame(newData)
        when (result) {
            is Resource.Error -> call.respond(
                HttpStatusCode.ExpectationFailed,
                CommonResponse(false, result.message, null)
            )

            is Resource.Success -> call.respond(
                HttpStatusCode.OK,
                CommonResponse(true, "item updated!", null)
            )
        }
    }
}


/////////////////////////
//  productData routing
/////////////////////////

fun Route.postProductData(
    gameChooserDataSource: GameChooserDataSource
) {
    //inserting
    post<ProductData>("postProductData") {
        val newData = it
        when (val result = gameChooserDataSource.addProductData(newData)) {
            is Resource.Error -> call.respond(
                HttpStatusCode.ExpectationFailed,
                CommonResponse(false, result.message, null)
            )

            is Resource.Success -> call.respond(
                HttpStatusCode.OK,
                CommonResponse(true, "item added!", null)
            )
        }
    }
}

fun Route.deleteProductData(
    gameChooserDataSource: GameChooserDataSource
) {
    delete("deleteProductData") {
        val id = call.parameters["id"]
        val result = gameChooserDataSource.deleteProductData(id ?: "-1")
        when (result) {
            is Resource.Error -> call.respond(
                HttpStatusCode.ExpectationFailed,
                CommonResponse(false, result.message, null)
            )

            is Resource.Success -> call.respond(
                HttpStatusCode.OK,
                CommonResponse(true, "item deleted!", null)
            )
        }
    }
}

fun Route.updateProductData(
    gameChooserDataSource: GameChooserDataSource
) {
    //updating
    put("updateProductData") {
        val newData = call.receive<ProductData>()
        when (val result = gameChooserDataSource.updateProductData(newData)) {
            is Resource.Error -> call.respond(
                HttpStatusCode.ExpectationFailed,
                CommonResponse(false, result.message, null)
            )

            is Resource.Success -> call.respond(
                HttpStatusCode.OK,
                CommonResponse(true, "item updated!", null)
            )
        }
    }
}