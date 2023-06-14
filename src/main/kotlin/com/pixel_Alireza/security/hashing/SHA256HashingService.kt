package com.pixel_Alireza.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom


class SHA256HashingService : HashingService {
    override fun generateSaltedHash(userPass: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$userPass")
        return SaltedHash(
            salt = saltAsHex,
            hash = hash
        )

    }

    override fun verify(userPass: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + userPass) == (saltedHash.hash)
    }
}