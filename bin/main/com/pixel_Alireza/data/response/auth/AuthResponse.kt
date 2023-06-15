package com.pixel_Alireza.data.response.auth

import kotlinx.serialization.Serializable


@Serializable
data class SignInResponse(
    val res: Boolean,
    val token: String? = null,
    val message: String? = null
)

@Serializable
data class SignUpResponse(
    val res: Boolean,
    val message: String
)

@Serializable
data class SecretInfo(
    val userId : String ,
    val username: String ,
    val email : String
)