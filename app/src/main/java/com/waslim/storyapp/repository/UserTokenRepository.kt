package com.waslim.storyapp.repository

import com.waslim.storyapp.model.datastore.UserTokenPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserTokenRepository @Inject constructor(private val userTokenPreferences: UserTokenPreferences){
    suspend fun setToken(token: String) = userTokenPreferences.setToken(token)

    suspend fun removeToken() = userTokenPreferences.removeToken()

    fun getToken() : Flow<String> = userTokenPreferences.getToken()
}