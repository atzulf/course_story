package com.picodiploma.coursestory.di

import android.content.Context
import com.picodiploma.coursestory.data.UserRepository
import com.picodiploma.coursestory.data.pref.UserPreference
import com.picodiploma.coursestory.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}