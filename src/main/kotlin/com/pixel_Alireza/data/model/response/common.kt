package com.pixel_Alireza.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse<T>(
    val res: Boolean,
    val message: String? = null ,
    val data : T? = null
)