package com.picodiploma.coursestory.data

import com.picodiploma.coursestory.data.pref.UserModel
import com.picodiploma.coursestory.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: com.picodiploma.coursestory.data.pref.UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<com.picodiploma.coursestory.data.pref.UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}