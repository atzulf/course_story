package com.picodiploma.coursestory.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.picodiploma.coursestory.data.UserRepository
import com.picodiploma.coursestory.di.Injection
import com.picodiploma.coursestory.view.login.LoginViewModel
import com.picodiploma.coursestory.view.main.MainViewModel

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
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: com.picodiploma.coursestory.view.ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): com.picodiploma.coursestory.view.ViewModelFactory {
            if (com.picodiploma.coursestory.view.ViewModelFactory.Companion.INSTANCE == null) {
                synchronized(com.picodiploma.coursestory.view.ViewModelFactory::class.java) {
                    com.picodiploma.coursestory.view.ViewModelFactory.Companion.INSTANCE =
                        com.picodiploma.coursestory.view.ViewModelFactory(
                            Injection.provideRepository(context)
                        )
                }
            }
            return com.picodiploma.coursestory.view.ViewModelFactory.Companion.INSTANCE as com.picodiploma.coursestory.view.ViewModelFactory
        }
    }
}