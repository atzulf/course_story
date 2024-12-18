package com.submision.coursestory.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.submision.coursestory.data.UserRepository
import com.submision.coursestory.di.Injection
import com.submision.coursestory.view.login.LoginViewModel
import com.submision.coursestory.view.main.MainViewModel
import com.submision.coursestory.view.signup.SignUpViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: com.submision.coursestory.view.ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): com.submision.coursestory.view.ViewModelFactory {
            if (com.submision.coursestory.view.ViewModelFactory.Companion.INSTANCE == null) {
                synchronized(com.submision.coursestory.view.ViewModelFactory::class.java) {
                    com.submision.coursestory.view.ViewModelFactory.Companion.INSTANCE =
                        com.submision.coursestory.view.ViewModelFactory(
                            Injection.provideRepository(context)
                        )
                }
            }
            return com.submision.coursestory.view.ViewModelFactory.Companion.INSTANCE as com.submision.coursestory.view.ViewModelFactory
        }
    }
}