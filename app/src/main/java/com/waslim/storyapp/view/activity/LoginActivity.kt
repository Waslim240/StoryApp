package com.waslim.storyapp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.waslim.storyapp.R
import com.waslim.storyapp.databinding.ActivityLoginBinding
import com.waslim.storyapp.model.showToast
import com.waslim.storyapp.viewmodel.LoginViewModel
import com.waslim.storyapp.viewmodel.UserTokenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    private val userTokenViewModel by viewModels<UserTokenViewModel>()
    private var doubleBackToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        checkUser()
        checkError()
        goToRegister()
        doubleBackExit()
        checkLength()

    }

    private fun checkError() = loginViewModel.error.observe(this) {
        showLoading(true)
        if (it) {
            showLoading(false)
            showToast(getString(R.string.login_gagal))
        }
    }

    private fun checkUser() = binding.btnLogin.setOnClickListener {
        val email = binding.etEmailLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()
        showLoading(true)

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 6) {
            loginViewModel.loginUser.observe(this) {
                if (it != null) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    saveToken(it.loginResult?.token.toString())
                    finish()
                    showToast(it.message.toString())
                }
            }
        }
        loginViewModel.loginUser(email, password)
        closedKeyboard()
    }

    private fun showLoading(isLoading: Boolean) = loginViewModel.isLoading.observe(this) {
        binding.progressBarLogin.visibility = when {
            isLoading -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun closedKeyboard() {
        val view: View? = currentFocus
        val inputMethodManager: InputMethodManager
        when {
            view != null -> {
                inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    private fun checkLength() = binding.apply {
        etPasswordLogin.doAfterTextChanged { text ->
            when {
                text!!.length in 1..5 -> textInputLayoutPasswordLogin.isEndIconVisible = false
                text.length >= 5 || text.isEmpty() -> textInputLayoutPasswordLogin.isEndIconVisible = true
            }
        }
    }

    private fun saveToken(token: String) = userTokenViewModel.setToken(token)

    private fun goToRegister() = binding.registerHere.setOnClickListener {
        startActivity(Intent(this, RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun doubleBackExit() = onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            when {
                doubleBackToExit -> {
                    finish()
                }
                else -> {
                    doubleBackToExit = true
                    showToast(getString(R.string.tekan_lagi_untuk_keluar))
                    Handler(Looper.getMainLooper()).postDelayed({
                        kotlin.run {
                            doubleBackToExit = false
                        }
                    }, DELAY)
                }
            }
        }
    })

    companion object {
        private const val DELAY = 2000L
    }

}