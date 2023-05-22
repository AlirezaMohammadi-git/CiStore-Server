package com.pixel_Alireza.data.model.request

import kotlinx.serialization.Serializable


@Serializable
data class UpdatePassword(
    val email : String ,
    val oldPass : String ,
    val newPassword: String
)
