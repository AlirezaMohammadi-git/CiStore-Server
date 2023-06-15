package com.pixel_Alireza.data.request

import kotlinx.serialization.Serializable


@Serializable
data class AuthenticationReq(
    val username : String? = null ,
    val email : String ,
    val password :String
)
