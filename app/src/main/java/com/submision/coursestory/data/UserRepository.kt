package com.submision.coursestory.data

import com.google.gson.Gson
import com.submision.coursestory.data.api.ApiService
import com.submision.coursestory.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun register(name: String, email: String, password: String): String {
        return try {
            val response = apiService.register(name, email, password)
            response.message ?: "Registration successful"
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            errorBody.message ?: "Ada sedikit error"
        }
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