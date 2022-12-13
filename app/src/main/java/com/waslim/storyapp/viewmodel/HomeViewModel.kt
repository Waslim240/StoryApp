package com.waslim.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.model.response.story.Story
import com.waslim.storyapp.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : ViewModel() {

    private val _stories = MutableLiveData<List<Story>?>()
    val stories: LiveData<List<Story>?> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStories(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            delay(DELAY)
            try {
                _stories.value = homeRepository.getALlStories(BEARER + token).listStory
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e(TAG, "OnFailure: ${e.message.toString()}")
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
        private const val BEARER = "Bearer "
        private const val DELAY = 2000L
    }
}