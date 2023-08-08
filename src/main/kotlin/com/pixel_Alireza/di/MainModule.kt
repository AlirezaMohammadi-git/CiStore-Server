package com.pixel_Alireza.di

import com.pixel_Alireza.data.model.repository.chat.ChatDatasource
import com.pixel_Alireza.data.model.repository.chat.KMongoChat
import com.pixel_Alireza.data.model.repository.discounts.DiscountDataSource
import com.pixel_Alireza.data.model.repository.discounts.KMonogoDiscount
import com.pixel_Alireza.data.model.repository.store.StoreDataSource
import com.pixel_Alireza.data.model.repository.store.KMongoStore
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
        KMongo.createClient()
            .coroutine
            .getDatabase(userDatabaseName)
    }

    val chatDatabase = "chatDatabase"
    single(named(chatDatabase)) {
        KMongo.createClient()
            .coroutine
            .getDatabase(chatDatabase)
    }

    val storeDatabase = "StoreDatabase"
    single(named(storeDatabase)) {
        KMongo.createClient()
            .coroutine
            .getDatabase(storeDatabase)
    }

    val discountDatabase = "DiscountDatabase"
    single(named(discountDatabase)) {
        KMongo.createClient()
            .coroutine
            .getDatabase(discountDatabase)
    }

    // user authentication database
    single<UserDataManager> { KMongoUserDataManager(get(named(userDatabaseName)), get()) }

    // related to auth service :
    single<TokenService> { JWTtokenService() }

    // related to auth service :
    single<HashingService> { SHA256HashingService() }

    // all chats text will save in this database
    single<ChatDatasource> { KMongoChat(get(named(chatDatabase))) }

    // controller interface of chat room
    single { ChatRoomController(get()) }

    // old system of showing data to user (maybe need to delete)
    single<StoreDataSource> { KMongoStore(get(named(storeDatabase))) }

    // discount database for discount
    single<DiscountDataSource> { KMonogoDiscount(get(named(discountDatabase))) }


}