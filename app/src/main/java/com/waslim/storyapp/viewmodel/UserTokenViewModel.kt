package com.waslim.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.repository.UserTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserTokenViewModel @Inject constructor(private val userTokenRepository: UserTokenRepository) : ViewModel() {

    fun setToken(token: String) {
        viewModelScope.launch {
            userTokenRepository.setToken(token)
        }
    }

    fun removeToken() {
        viewModelScope.launch {
            userTokenRepository.removeToken()
        }
    }

    fun getToken(): LiveData<String> = userTokenRepository.getToken().asLiveData()

}