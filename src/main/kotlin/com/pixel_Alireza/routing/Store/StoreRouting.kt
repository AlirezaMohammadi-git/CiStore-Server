package com.pixel_Alireza.routing.Store

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.model.repository.store.StoreDataSource
import com.pixel_Alireza.data.model.response.CommonResponse
import com.pixel_Alireza.data.model.response.storeItems.StoreData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.StoreGetAllItems(
    storeDataSource: StoreDataSource
) {
    get("getStoreItems") {
        try {
            call.respond(CommonResponse(true, null, storeDataSource.getAllItems()))
        }catch (e:Exception){
            call.respond(CommonResponse(false, e.message, null))
        }
    }
}

fun Route.postStoreItem(
    storeDataSource: StoreDataSource
) {
    //inserting
    post<StoreData>("postnewItem") {
        val newData = it
        val result = storeDataSource.addItem(newData)
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

fun Route.deleteitem(
    storeDataSource: StoreDataSource
) {
    delete("8vIFT(RAuYx@n&jh") {
        val id = call.parameters["id"]
        val result = storeDataSource.deleteItem(id ?: "-1")
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

fun Route.updatingItems(
    storeDataSource: StoreDataSource
) {
    //updating
    put("qvIjT(R#uYx1n&jh") {
        val newData = call.receive<StoreData>()
        val result = storeDataSource.updateItem(newData)
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