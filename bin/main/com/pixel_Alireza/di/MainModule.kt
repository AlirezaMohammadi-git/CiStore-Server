package com.pixel_Alireza.di

import com.pixel_Alireza.data.user.KMongoUserDataManager
import com.pixel_Alireza.data.user.UserDataManager
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
            val dbname = "Products"
            KMongo.createClient()
                .coroutine
                .getDatabase(dbname)
        }

    single <UserDataManager> { KMongoUserDataManager(get(named(userDatabaseName))) }

    single <TokenService> { JWTtokenService() }

    single <HashingService> { SHA256HashingService() }


}