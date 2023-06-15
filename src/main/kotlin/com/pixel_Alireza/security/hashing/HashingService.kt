package com.pixel_Alireza.security.hashing






interface HashingService {
    fun generateSaltedHash(userPass: String , saltLength : Int = 32 ) : SaltedHash
    fun verify( userPass: String , saltedHash: SaltedHash ) : Boolean
}