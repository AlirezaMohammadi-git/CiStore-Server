package com.pixel_Alireza.data.model.user

import com.example.chatapp.utils.Resource
import com.pixel_Alireza.security.hashing.HashingService
import com.pixel_Alireza.security.hashing.SaltedHash
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class KMongoUserDataManager(
    db: CoroutineDatabase,
    val hashingService: HashingService
) : UserDataManager {

    val userDatabase = db.getCollection<User>()

    override suspend fun insertNewUser(user: User): Resource<Boolean> {
        return try {
            val sameEmail = userDatabase.findOne(User::email eq user.email)?.email
            val sameUsername = userDatabase.findOne(User::username eq user.username)?.username
            if (sameUsername != null) {
                Resource.Error(message = "Another user is using this username")
            } else if (sameEmail != null) {
                Resource.Error(message = "This email already registered, please login.")
            } else {
                userDatabase.insertOne(user)
                Resource.Success(true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(message = e.message ?: "something went wrong from server")
        }
    }

    override suspend fun updatePassword(email: String, newPassword: String): Boolean {

        return try {
            val oldUser = userDatabase.findOne(User::email eq email)
            if (oldUser != null) {
                    val saltedHash = hashingService.generateSaltedHash(newPassword)
                    val newUser = User(
                        oldUser.username,
                        oldUser.email,
                        saltedHash.hash,
                        saltedHash.salt
                    )
                    userDatabase.replaceOne(User::email eq email, newUser).wasAcknowledged()
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }

    override suspend fun deleteUser(user: User): Boolean {
        return try {
            userDatabase.deleteOne(User::email eq user.email).wasAcknowledged()
        } catch (e: Exception) {
            try {
                userDatabase.deleteOne(User::username eq user.username).wasAcknowledged()
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    override suspend fun updateUsername(email: String, newUsername: String): Boolean {
        return try {
            val oldUser = userDatabase.findOne(User::email eq email)
            if (oldUser != null) {
                val newUser = User(
                    newUsername,
                    oldUser.email,
                    oldUser.password,
                    oldUser.salt
                )
                userDatabase.replaceOne(User::email eq email, newUser).wasAcknowledged()
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return try {
            userDatabase.findOne(User::email eq email)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}