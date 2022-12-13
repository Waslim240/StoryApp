package com.waslim.storyapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waslim.storyapp.model.response.register.RegisterResponse
import com.waslim.storyapp.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository) : ViewModel() {
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _register = MutableLiveData<RegisterResponse>()
    val register: LiveData<RegisterResponse> = _register

    fun registerUser(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _register.value = registerRepository.registerUser(name, email, password)
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
        private const val TAG = "RegisterViewModel"
        private const val DELAY = 2000L
    }
}