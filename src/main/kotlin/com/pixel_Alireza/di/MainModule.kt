package com.pixel_Alireza.di

import com.pixel_Alireza.data.CustomRoomDataSource
import com.pixel_Alireza.data.KMongoCustomRoom
import com.pixel_Alireza.data.model.user.KMongoUserDataManager
import com.pixel_Alireza.data.model.user.UserDataManager
import com.pixel_Alireza.globalRoom.ChatRoomController
import com.pixel_Alireza.security.hashing.HashingService
import com.pixel_Alireza.security.hashing.SHA256HashingService
import com.pixel_Alireza.security.token.JWTtokenService
import com.pixel_Alireza.security.token.TokenService
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {

    val userDatabaseName = "userDatabaseName"
        single(named(userDatabaseName)) {
            val dbname = "Users"
            KMongo.createClient()
                .coroutine
                .getDatabase(dbname)
        }

    val customRoom = "customRooms"
    single(named(customRoom)) {
        val dbname = "Rooms"
        KMongo.createClient()
            .coroutine
            .getDatabase(dbname)
    }

    single <UserDataManager> { KMongoUserDataManager(get(named(userDatabaseName)) , get()) }

    single <TokenService> { JWTtokenService() }

    single <HashingService> { SHA256HashingService() }

    single { ChatRoomController() }

    single <CustomRoomDataSource>{ KMongoCustomRoom(get(named(customRoom))) }


}