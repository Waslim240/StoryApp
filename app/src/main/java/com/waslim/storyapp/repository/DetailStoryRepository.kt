package com.waslim.storyapp.repository

import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.model.service.ApiService
import javax.inject.Inject

class DetailStoryRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getDetailStory(token: String, id: String) : Story = apiService.getDetailStories(token, id)
}