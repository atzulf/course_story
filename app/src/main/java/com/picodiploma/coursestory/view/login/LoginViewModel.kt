package com.picodiploma.coursestory.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picodiploma.coursestory.data.UserRepository
import com.picodiploma.coursestory.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: com.picodiploma.coursestory.data.pref.UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}