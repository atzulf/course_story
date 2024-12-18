package com.submision.coursestory.data

import android.util.Log
import com.google.gson.Gson
import com.submision.coursestory.data.api.ApiService
import com.submision.coursestory.data.pref.UserModel
import com.submision.coursestory.data.pref.UserPreference
import com.submision.coursestory.data.response.LoginResponse
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

    suspend fun login(email: String, password: String): LoginResponse {
        Log.d("UserRepository", "Login initiated for email: $email")
        return try {
            val response = apiService.login(email, password)
            if (response.error == false && response.loginResult != null) {
                val loginResult = response.loginResult
                val userModel = UserModel(
                    email = email,
                    token = loginResult.token!!,
                    isLogin = true
                )
                saveSession(userModel)
                Log.d("UserRepository", "Login successful for email: $email")
            } else {
                // Tangani jika login gagal karena error atau response kosong
                throw Exception(response.message ?: "Login failed, please check your credentials")
            }
            response
        } catch (e: HttpException) {
            // Tangani error jika server mengirim status 401 Unauthorized
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ErrorResponse::class.java).message
            } catch (ex: Exception) {
                "Invalid credentials or server error"
            }
            Log.e("UserRepository", "Login error: $errorMessage", e)
            throw Exception(errorMessage ?: "Login failed")
        } catch (e: Exception) {
            Log.e("UserRepository", "Unexpected error during login", e)
            throw Exception("Unexpected error: ${e.localizedMessage}")
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