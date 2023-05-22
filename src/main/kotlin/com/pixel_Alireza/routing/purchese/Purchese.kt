package com.pixel_Alireza.routing.purchese

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking


fun Route.purchese(){

    get ("/purchese"){

        runBlocking {

            val client = HttpClient()
            val message = client.post {
                
            }

        }

    }

}