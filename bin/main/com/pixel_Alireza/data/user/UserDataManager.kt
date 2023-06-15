package com.pixel_Alireza.data.user

interface UserDataManager {

    suspend fun insertNewUser(user: User): Boolean
    suspend fun deleteUser(user: User): Boolean
    suspend fun updateUser(oldUser: User, newUser: User): Boolean
    suspend fun getUserByUsername(username: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun sameUsernameChecker(username: String): Boolean
    suspend fun sameEmailChecker(email: String): Boolean

}