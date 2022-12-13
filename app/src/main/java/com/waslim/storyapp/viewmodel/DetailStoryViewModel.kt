package com.waslim.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.repository.DetailStoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailStoryViewModel @Inject constructor(private val detailStoryRepository: DetailStoryRepository) : ViewModel() {

    private val _detailStories = MutableLiveData<Story?>()
    val detailStories: LiveData<Story?> = _detailStories

    fun getDetailStory(token: String, id: String) {
        viewModelScope.launch {
            try {
                _detailStories.value = detailStoryRepository.getDetailStory(BEARER + token, id)
            } catch (e: Exception) {
                Log.e(TAG, "OnFailure: ${e.message.toString()}")
            }
        }
    }

    companion object {
        private const val TAG = "DetailViewModel"
        private const val BEARER = "Bearer "
    }
}