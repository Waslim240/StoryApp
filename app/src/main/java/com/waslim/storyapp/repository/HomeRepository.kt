package com.waslim.storyapp.repository

import com.waslim.storyapp.model.response.story.GetAllStoryResponse
import com.waslim.storyapp.model.service.ApiService
import javax.inject.Inject

class HomeRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getALlStories(token: String) : GetAllStoryResponse = apiService.getAllStories(token)
}