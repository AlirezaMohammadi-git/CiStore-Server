package com.pixel_Alireza.security.token

interface TokenService {
    fun generate(
        config : TokenConfig,
        vararg claims : TokenClaim
    ) : String
}