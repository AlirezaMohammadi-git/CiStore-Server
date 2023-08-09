package com.pixel_Alireza.routing.Store.discounts

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.data.repository.store.discounts.DiscountDataSource
import com.pixel_Alireza.data.model.response.CommonResponse
import com.pixel_Alireza.data.model.response.storeItems.discounts.NewDiscounts
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.GetAllDiscountItems(
    discountDataSource: DiscountDataSource
) {
    get("getAllDiscountItems") {
        try {
            call.respond(CommonResponse(true, null, discountDataSource.getAllDiscounts()))
        } catch (e: Exception) {
            call.respond(CommonResponse(false, e.message, null))
        }
    }
}

///////////////////////////////////////////////////
// below routing is used for second app that control the first
///////////////////////////////////////////////////
fun Route.postNewDiscount(
    discountDataSource: DiscountDataSource
) {
    //inserting
    post<NewDiscounts>("postNewDiscount") {
        val newData = it
        val result = discountDataSource.addNewDiscount(newData)
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

fun Route.deleteDiscount(
    discountDataSource: DiscountDataSource
) {
    delete("deleteDiscount") {
        val id = call.parameters["id"]
        val result = discountDataSource.deleteDiscount(id ?: "-1")
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

fun Route.updateDiscount(
    discountDataSource: DiscountDataSource
) {
    //updating
    put("putDiscount") {
        val newData = call.receive<NewDiscounts>()
        val result = discountDataSource.updateDiscount(newData)
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