package com.waslim.storyapp.repository

import com.waslim.storyapp.model.response.login.LoginResponse
import com.waslim.storyapp.model.service.ApiService
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun loginUser(email: String, password: String) : LoginResponse =
        apiService.loginUser(email, password)
}