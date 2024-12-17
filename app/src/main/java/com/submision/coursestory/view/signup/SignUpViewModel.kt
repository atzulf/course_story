package com.submision.coursestory.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submision.coursestory.data.Result
import com.submision.coursestory.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    private val signupState = MutableStateFlow<Result<String>>(Result.Loading)
    val registerState: StateFlow<Result<String>> = signupState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            signupState.value = Result.Loading
            try {
                val message = repository.register(name, email, password)
                signupState.value = Result.Success(message)
            } catch (e: Exception) {
                signupState.value = Result.Error(e.message ?: "Unknown Error")
            }
        }
    }
}