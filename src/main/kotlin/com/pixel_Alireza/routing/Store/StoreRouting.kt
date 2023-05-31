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


fun Route.StoreRouts(
    storeDataSource: StoreDataSource
) {
    get("getStoreItems") {
        call.respond(HttpStatusCode.OK, storeDataSource.getAllItems())
    }


    //inserting
    post("wA(G@m4#&JaLSc6Q") {

        val newData = call.receive<StoreData>()
        val result = storeDataSource.addItem(newData)
        when (result) {
            is Resource.Error -> call.respond(
                HttpStatusCode.ExpectationFailed,
                CommonResponse(false, result.message, null)
            )

            is Resource.Success -> call.respond(
                HttpStatusCode.OK ,
                CommonResponse(true,"item added!",null)
            )
        }


    }
    delete("8vIFT(RAuYx@n&jh") {
        val id = call.parameters["id"]
        val result = storeDataSource.deleteItem(id?:"-1")
        when (result) {
            is Resource.Error -> call.respond(
                HttpStatusCode.ExpectationFailed,
                CommonResponse(false, result.message, null)
            )

            is Resource.Success -> call.respond(
                HttpStatusCode.OK ,
                CommonResponse(true,"item deleted!",null)
            )
        }
    }
    //updating
    put(")wP+Q%k4zagI3nNf") {
        val newData = call.receive<StoreData>()
        val result = storeDataSource.updateItem(newData)
        when (result) {
            is Resource.Error -> call.respond(
                HttpStatusCode.ExpectationFailed,
                CommonResponse(false, result.message, null)
            )

            is Resource.Success -> call.respond(
                HttpStatusCode.OK ,
                CommonResponse(true,"item updated!",null)
            )
        }
    }

}