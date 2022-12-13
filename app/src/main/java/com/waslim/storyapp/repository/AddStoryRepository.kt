package com.waslim.storyapp.repository

import com.waslim.storyapp.model.response.story.AddNewStoryResponse
import com.waslim.storyapp.model.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddStoryRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun addNewStory(token: String, imageFile: MultipartBody.Part, description: RequestBody) : AddNewStoryResponse =
        apiService.addStory(token, imageFile, description)
}