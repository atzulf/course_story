package com.submision.coursestory.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.submision.coursestory.data.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: com.submision.coursestory.data.pref.UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}