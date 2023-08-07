package com.pixel_Alireza.data.model.response.storeItems.banners

import kotlinx.serialization.Serializable


@Serializable
data class BannerData(
    val imageURL: String,
    val mainURL: String
)
