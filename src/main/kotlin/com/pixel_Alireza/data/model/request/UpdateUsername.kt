package com.pixel_Alireza.data.model.request

import kotlinx.serialization.Serializable


@Serializable
data class UpdateUsername(
    val username : String ,
    val email : String
)
