package com.waslim.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.model.response.login.LoginResponse
import com.waslim.storyapp.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginUser = MutableLiveData<LoginResponse>()
    val loginUser: LiveData<LoginResponse> = _loginUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _loginUser.value = loginRepository.loginUser(email, password)
                _error.value = false
                _isLoading.value = false
            } catch (e: Exception) {
                delay(DELAY)
                Log.e(TAG, "onFailure: ${e.message.toString()}")
                _error.value = true
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
        private const val DELAY = 2000L
    }
}