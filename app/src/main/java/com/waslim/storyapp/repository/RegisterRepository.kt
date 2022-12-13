package com.waslim.storyapp.repository

import com.waslim.storyapp.model.response.register.RegisterResponse
import com.waslim.storyapp.model.service.ApiService
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun registerUser(name: String, email: String, password: String) : RegisterResponse =
        apiService.registerUser(name, email, password)
}