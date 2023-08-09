package com.pixel_Alireza.plugins

import com.pixel_Alireza.data.model.user.UserDataManager
import com.pixel_Alireza.data.repository.chat.ChatDatasource
import com.pixel_Alireza.data.repository.store.discounts.DiscountDataSource
import com.pixel_Alireza.data.repository.store.gameChooser.GameChooserDataSource
import com.pixel_Alireza.data.repository.store.old.StoreDataSource
import com.pixel_Alireza.globalRoom.ChatRoomController
import com.pixel_Alireza.routing.Store.StoreGetAllItems
import com.pixel_Alireza.routing.Store.deleteitem
import com.pixel_Alireza.routing.Store.discounts.GetAllDiscountItems
import com.pixel_Alireza.routing.Store.discounts.deleteDiscount
import com.pixel_Alireza.routing.Store.discounts.postNewDiscount
import com.pixel_Alireza.routing.Store.discounts.updateDiscount
import com.pixel_Alireza.routing.Store.gameChooser.*
import com.pixel_Alireza.routing.Store.postStoreItem
import com.pixel_Alireza.routing.Store.updatingItems
import com.pixel_Alireza.routing.auth.*
import com.pixel_Alireza.routing.socket.disconnect
import com.pixel_Alireza.security.hashing.HashingService
import com.pixel_Alireza.security.token.TokenConfig
import com.pixel_Alireza.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    userDataManager: UserDataManager,
    tokenService: TokenService,
    config: TokenConfig,
    chatRoomController: ChatRoomController,
    chatDatasource: ChatDatasource,
    storeDataSource: StoreDataSource,
    discountDataSource: DiscountDataSource,
    gameChooserDataSource: GameChooserDataSource
) {

    routing {

        //region User authentication
        signUp(
            hashingService,
            userDataManager
        )
        signIn(
            hashingService,
            userDataManager,
            tokenService,
            config
        )
        authenticateUser()
        getSecretInfo()
//        globalChat(chatRoomController)
        updateUsername(userDataManager)
        updatePass(userDataManager, hashingService)
        //endregion

        //region Chat routing
        //        getAllMessages(chatRoomController)
//        deleteAllMessages(chatRoomController)
        //endregion

        //region store screen routing
        StoreGetAllItems(storeDataSource)

        postStoreItem(storeDataSource)

        deleteitem(storeDataSource)

        updatingItems(storeDataSource)
        //endregion

        //region Discount routing
        GetAllDiscountItems(discountDataSource)
        postNewDiscount(discountDataSource)
        deleteDiscount(discountDataSource)
        updateDiscount(discountDataSource)
        //endregion



        //game products :
        getAllGameProducts(gameChooserDataSource)
        postNewGame(gameChooserDataSource)
        deleteGame(gameChooserDataSource)
        updateGame(gameChooserDataSource)
        //product data ( will sow up after game data )
        getProductDataByTag(gameChooserDataSource)
        postProductData(gameChooserDataSource)
        deleteProductData(gameChooserDataSource)
        updateProductData(gameChooserDataSource)


        disconnect(chatRoomController)


        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
