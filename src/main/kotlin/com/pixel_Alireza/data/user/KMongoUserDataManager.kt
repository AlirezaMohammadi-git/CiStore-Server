package com.pixel_Alireza.data.user

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class KMongoUserDataManager(
    db: CoroutineDatabase
) : UserDataManager {

    val userDatabase = db.getCollection<User>()

    override suspend fun insertNewUser(user: User): Boolean {
        return try {
            userDatabase.insertOne(user).wasAcknowledged()
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

    override suspend fun updateUser(oldUser: User, newUser: User): Boolean {
        return try {
            userDatabase.replaceOne(filter = User::id eq oldUser.id, replacement = newUser).wasAcknowledged()
        }catch (e:Exception){
            e.printStackTrace()
            false
        }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return try {
            userDatabase.findOne( User::username eq username )
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        return try {
            userDatabase.findOne( User::email eq email )
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun sameUsernameChecker(username: String): Boolean {
        val usernameFromDatabase = userDatabase.findOne(User::username eq username)?.username
        return usernameFromDatabase.equals(username)
    }

    override suspend fun sameEmailChecker(email: String): Boolean {
        val usernameFromDatabase = userDatabase.findOne(User::email eq email)?.username
        return usernameFromDatabase.equals(email)
    }
}