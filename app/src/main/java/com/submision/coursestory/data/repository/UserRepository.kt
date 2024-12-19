package com.submision.coursestory.data.repository

import android.util.Log
import com.submision.coursestory.data.api.ApiService
import com.submision.coursestory.data.pref.UserPreference
import com.submision.coursestory.data.response.AllStoriesResponse
import com.submision.coursestory.data.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun register(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getStories(): AllStoriesResponse {
        val token = userPreference.getSession().first().token
        return apiService.getStories("Bearer $token")
    }

    suspend fun getDetailStory(storyId: String) = try {
        val token = userPreference.getSession().first().token
        apiService.getDetailStory("Bearer $token", storyId)
    } catch (e: HttpException) {
        Log.e("UserRepository", "getDetailStory: ${e.message()}")
        null
    }

    suspend fun saveSession(user: com.submision.coursestory.data.pref.UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<com.submision.coursestory.data.pref.UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}