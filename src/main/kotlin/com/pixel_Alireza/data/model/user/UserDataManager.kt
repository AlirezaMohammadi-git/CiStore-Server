package com.pixel_Alireza.data.model.user

import com.example.chatapp.utils.Resource

interface UserDataManager {

    suspend fun insertNewUser(user: User): Resource<Boolean>
    suspend fun deleteUser(user: User): Boolean
    suspend fun updateUsername(email: String, newUsername: String): Boolean
    suspend fun updatePassword(email: String,newPassword: String) : Boolean
    suspend fun getUserByEmail(email: String): User?

}